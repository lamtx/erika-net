package erika.core.net.datacontract

import erika.core.net.CopyStreamListener
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream
import java.net.HttpURLConnection
import java.nio.charset.Charset

interface NetworkService {

    suspend fun <T : OutputStream> download(
        outputFactory: (HttpURLConnection) -> T,
        listener: CopyStreamListener? = null,
        uploadListener: CopyStreamListener? = null,
    ): T

    suspend fun getString(uploadListener: CopyStreamListener? = null): String {
        var charset: String? = null
        val stream = download(
            { connection ->
                val contentLength = connection.contentLength
                charset = connection.charset
                ByteArrayOutputStream(maxOf(DEFAULT_BUFFER_SIZE, contentLength))
            },
            null,
            uploadListener
        )
        val bytes = stream.toByteArray()
        return String(bytes, Charset.forName(charset ?: "utf-8"))
    }

    suspend fun downloadTo(
        outputStream: OutputStream,
        listener: CopyStreamListener? = null,
        uploadListener: CopyStreamListener? = null,
    ) {
        download({ outputStream }, listener, uploadListener)
    }

    suspend fun <T> get(
        parser: DataParser<T>,
        uploadListener: CopyStreamListener? = null
    ): T {
        return parser.parseObject(getString(uploadListener))
    }

    suspend fun <T> getList(
        parser: DataParser<T>,
        uploadListener: CopyStreamListener? = null
    ): List<T> {
        return parser.parseList(getString(uploadListener))
    }

    suspend fun downloadAll(
        listener: CopyStreamListener? = null,
        uploadListener: CopyStreamListener? = null,
    ): ByteArray {
        return download(
            { connection ->
                val contentLength = connection.contentLength
                ByteArrayOutputStream(maxOf(DEFAULT_BUFFER_SIZE, contentLength))
            },
            listener,
            uploadListener,
        ).toByteArray()
    }

    suspend fun download(file: File, listener: CopyStreamListener? = null) {
        file.outputStream().use { out ->
            downloadTo(out, listener = listener)
        }
    }

    suspend fun <T> get(serializer: DeserializationStrategy<T>): T {
        return json.decodeFromString(serializer, getString())
    }

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        val json by lazy {
            Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                explicitNulls = false
            }
        }
    }
}