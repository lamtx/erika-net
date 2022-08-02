package erika.core.net.datacontract

import android.util.Log
import erika.core.net.CopyStreamListener
import erika.core.net.HttpMethod
import erika.core.net.HttpStatusException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object NetClient {
    private const val tag = "NetClient"
    suspend fun getString(request: Request, listener: CopyStreamListener?): String {
        return withContext(Dispatchers.IO) {
            makeRequest(request, listener)
        }
    }

    suspend fun downloadTo(
        request: Request,
        outputStream: OutputStream,
        listener: CopyStreamListener?
    ) {
        return withContext(Dispatchers.IO) {
            val connection = makeConnection(request, null)
            val estimatedSize = connection.getHeaderField("Content-length")?.toLongOrNull()
                ?: -1
            connection.inputStream.use { ins ->
                ins.copyTo(outputStream, estimatedSize, listener)
            }
        }
    }

    private fun makeRequest(request: Request, uploadListener: CopyStreamListener?): String {
        val connection = makeConnection(request, uploadListener)
        if (request.method == HttpMethod.Head) {
            return ""
        }
        return connection.inputStream.use {
            it.readString(connection.contentEncoding)
        }.also {
            log("Response: $it")
        }
    }

    private fun makeConnection(request: Request, listener: CopyStreamListener?): HttpURLConnection {
        val url = URL(request.fullUrl)
        val connection = url.openConnection() as HttpURLConnection

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

        writeContent(request, connection, listener)

        val responseCode = connection.responseCode
        log("Status: $responseCode")
        request.responseHeaders?.let { headers ->
            for ((key, values) in connection.headerFields.entries) {
                if (key != null) {
                    headers[key.lowercase()] = values.last()
                }
            }
        }
        if (responseCode < 200 || responseCode >= 300) {
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
                    } catch (ignored: IOException) {
                    }
                }
            }
            throw HttpStatusException(content, responseCode)
        }
        return connection
    }


    private fun writeContent(
        request: Request,
        connection: HttpURLConnection,
        uploadListener: CopyStreamListener? = null
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