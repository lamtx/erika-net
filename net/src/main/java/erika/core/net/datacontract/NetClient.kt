package erika.core.net.datacontract

import android.util.Log
import erika.core.net.CopyStreamListener
import erika.core.net.HttpMethod
import erika.core.net.HttpStatusException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object NetClient {
    private const val tag = "NetClient"

    suspend fun <T : OutputStream> download(
        request: Request,
        outputFactory: (HttpURLConnection) -> T,
        listener: CopyStreamListener?,
        uploadListener: CopyStreamListener?,
    ): T {
        return withContext(Dispatchers.IO) {
            val connection = makeConnection(request, uploadListener)
            val contentLength = connection.contentLengthCompat
            connection.inputStream.use { input ->
                outputFactory(connection).also { output ->
                    input.copyTo(output, contentLength, listener)
                }
            }
        }
    }

    private fun CoroutineScope.makeConnection(
        request: Request,
        listener: CopyStreamListener?,
    ): HttpURLConnection {
        val url = URL(request.fullUrl)
        val connection =
            (request.network?.openConnection(url) ?: url.openConnection()) as HttpURLConnection

        connection.requestMethod = request.method.name.uppercase()
        connection.instanceFollowRedirects = true
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

        try {
            writeContent(request, connection, listener)
        } catch (e: Throwable) {
            try {
                connection.disconnect()
            } catch (_: Exception) {
            }
            throw e
        }

        val responseCode = connection.responseCode
        log("Status: $responseCode")
        request.responseHeaders?.let { headers ->
            for ((key, values) in connection.headerFields.entries) {
                if (key != null) {
                    headers[key.lowercase()] = values.last()
                }
            }
        }
        if (responseCode != 200) {
            val contentStream = if (request.method == HttpMethod.Head) {
                null
            } else {
                try {
                    connection.inputStream
                } catch (e: IOException) {
                    connection.errorStream
                }
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
                    } catch (_: IOException) {
                    }
                }
            }
            throw HttpStatusException(content, responseCode)
        }
        return connection
    }

    private fun CoroutineScope.writeContent(
        request: Request,
        connection: HttpURLConnection,
        uploadListener: CopyStreamListener? = null,
    ) {
        if (request.method == HttpMethod.Head || request.method == HttpMethod.Delete ||
            request.body == null || request.method === HttpMethod.Get
        ) {
            return
        }

        val contentType = request.body.contentType
        val contentLength = request.body.length()

        log("ContentType: $contentType")

        connection.doOutput = true
        connection.setRequestProperty("Content-Type", contentType.mimeType)
        if (contentLength >= 0) {
            connection.setRequestProperty("Content-Length", contentLength.toString())
        }
        log("Body: ${request.body}")
        if (uploadListener != null) {
            if (contentLength <= 0) {
                error("Content length is missing. To listen upload progress, content length must be determined")
            }
            connection.setFixedLengthStreamingMode(contentLength)
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