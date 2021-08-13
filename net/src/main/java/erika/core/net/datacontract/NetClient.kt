package erika.core.net.datacontract

import android.util.Log
import erika.core.net.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

object NetClient {
    private const val tag = "NetClient"
    private val NoModifier: HttpURLConnection.() -> Unit = {}

    class Request(
        val url: String,
        val method: HttpMethod = HttpMethod.GET,
        val body: Body? = null,
        val params: String? = null,
        val headers: List<Pair<String, String>> = emptyList(),
        val credentials: Credentials? = null,
        val modifier: HttpURLConnection.() -> Unit = NoModifier
    ) : NetworkService {
        val fullUrl: String
            get() {
                return if (params.isNullOrEmpty()) {
                    url
                } else {
                    if (url.contains('?')) {
                        "$url&${params}"
                    } else {
                        "$url?${params}"
                    }
                }
            }

        override suspend fun getString(listener: CopyStreamListener?): String {
            return withContext(Dispatchers.IO) {
                makeRequest(this@Request, listener)
            }
        }

        override suspend fun download(file: File, listener: CopyStreamListener?) {
            return withContext(Dispatchers.IO) {
                val connection = makeConnection(this@Request, null)
                val estimatedSize = connection.getHeaderField("Content-length")?.toLongOrNull()
                    ?: -1
                connection.inputStream.use { ins ->
                    @Suppress("BlockingMethodInNonBlockingContext")
                    FileOutputStream(file).use { out ->
                        ins.copyTo(out, estimatedSize, listener)
                    }
                }
            }
        }
    }

    class Response(
        val statusCode: Int,
        val headers: List<Pair<String, String>>,
        val body: String
    )

    class RequestBuilder(private val url: String) : NetworkService {
        private var method = HttpMethod.GET
        private var body: Body? = null
        private var params: String? = null
        private val headers = mutableListOf<Pair<String, String>>()
        private var credentials: Credentials? = null

        fun authorize(credentials: Credentials?) = apply {
            this.credentials = credentials
        }

        fun body(body: Body?) = apply {
            this.body = body
        }

        fun jsonBody(body: Parameter) = apply {
            this.body = body.toJsonBody()
        }

        fun jsonBody(body: List<Parameter>) = apply {
            this.body = body.toJsonBody()
        }

        fun urlEncodedBody(body: Parameter) = apply {
            this.body = body.toUrlEncodedBody()
        }

        fun jsonBody(p: ParameterCreator) = apply {
            this.body = p.toJsonBody()
        }

        fun urlEncodedBody(p: ParameterCreator) = apply {
            this.body = p.toUrlEncodedBody()
        }

        fun params(params: String?) = apply {
            this.params = params
        }

        fun params(p: ParameterCreator) = apply {
            this.params = p.toJson().toUrlEncoded()
        }

        fun method(method: HttpMethod) = apply {
            this.method = method
        }

        fun headers(h: HeaderBuilder.() -> Unit) = apply {
            h(HeaderBuilder(headers))
        }

        fun build(): Request {
            return Request(
                url = url,
                method = method,
                body = body,
                params = params,
                headers = headers,
                credentials = credentials
            )
        }

        override suspend fun getString(listener: CopyStreamListener?): String {
            return build().getString(listener)
        }

        override suspend fun <T> get(parser: DataParser<T>, listener: CopyStreamListener?): T {
            return build().get(parser, listener)
        }

        override suspend fun <T> getList(
            parser: DataParser<T>,
            listener: CopyStreamListener?
        ): List<T> {
            return build().getList(parser, listener)
        }

        override suspend fun download(file: File, listener: CopyStreamListener?) {
            return build().download(file, listener)
        }
    }

    private fun makeConnection(request: Request, listener: CopyStreamListener?): HttpURLConnection {
        val url = URL(request.fullUrl)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = request.method.name
        connection.instanceFollowRedirects = true
        connection.setRequestProperty(HttpHeaders.ACCEPT_CHARSET, "utf-8")
        request.modifier(connection)
        request.credentials?.prepareRequest(connection)

        for ((key, value) in request.headers) {
            connection.setRequestProperty(key, value)
        }

        connection.connectTimeout = 30_000
        connection.readTimeout = 60_000

        log("${request.method}: $url")
        log("Credentials: ${request.credentials}")
        log("Headers: ${request.headers}")

        writeContent(request, connection, listener)

        val responseCode = connection.responseCode
        log("Status: $responseCode")
        if (responseCode < 200 || responseCode >= 300) {
            val contentStream = try {
                connection.inputStream
            } catch (e: IOException) {
                connection.errorStream
            }

            val content: String
            if (contentStream == null) {
                content = ""
            } else {
                try {
                    content = contentStream.readString(connection.contentEncoding)
                    log("Response: $content")
                } finally {
                    try {
                        contentStream.close()
                    } catch (ignored: IOException) {
                    }
                }
            }
            throw HttpStatusException(content, responseCode, connection.headerFields)
        }
        return connection
    }

    private fun makeRequest(request: Request, uploadListener: CopyStreamListener?): String {
        val connection = makeConnection(request, uploadListener)
        return connection.inputStream.use {
            it.readString(connection.contentEncoding)
        }.also {
            log("Response: $it")
        }
    }

    private fun writeContent(
        request: Request,
        connection: HttpURLConnection,
        uploadListener: CopyStreamListener? = null
    ) {
        if (request.method == HttpMethod.HEAD || request.method == HttpMethod.DELETE ||
            request.body == null || request.body.isEmpty || request.method === HttpMethod.GET
        ) {
            return
        }

        val contentType = request.body.contentType
        val contentLength = request.body.length()

        log("ContentType: $contentType")

        connection.doOutput = true
        connection.setRequestProperty("Content-Type", contentType)
        if (contentLength >= 0) {
            connection.setRequestProperty("Content-Length", contentLength.toString())
        }
        if (uploadListener != null) {
            if (contentLength <= 0) {
                error("Content length is missing. To listen upload progress, content length must be determined")
            }
            connection.setFixedLengthStreamingMode(contentLength)
        } else {
            log("Body: ${request.body.getContent().readString("UTF-8")}")
        }
        request.body.getContent().use { data ->
            DataOutputStream(connection.outputStream).use { writer ->
                data.copyTo(writer, contentLength, uploadListener)
            }
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun log(message: String) {
        Log.d(tag, message)
    }
}
