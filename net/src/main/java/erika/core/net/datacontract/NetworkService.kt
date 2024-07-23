package erika.core.net.datacontract

import android.util.Log
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

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        val Json by lazy {
            Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                explicitNulls = false
            }
        }
    }
}

suspend inline fun NetworkService.getString(
    noinline uploadListener: CopyStreamListener? = null,
): String = getString(null, uploadListener)

suspend fun NetworkService.getString(
    response: HttpHeadersResponse?,
    uploadListener: CopyStreamListener? = null
): String {
    var charset: String? = null
    val stream = download(
        { connection ->
            response?.set(connection)
            val contentLength = connection.contentLength
            if (contentLength > MAX_BUFFER_SIZE) {
                throw IllegalStateException("Too large to create buffer: max=$MAX_BUFFER_SIZE, actual=$contentLength")
            }
            charset = connection.charset
            ByteArrayOutputStream(maxOf(DEFAULT_BUFFER_SIZE, contentLength))
        },
        null,
        uploadListener
    )
    val bytes = stream.toByteArray()
    val s = String(bytes, Charset.forName(charset ?: "utf-8"))
    Log.d("NetClient", "Response: $s")
    return s
}

suspend fun NetworkService.downloadTo(
    outputStream: OutputStream,
    listener: CopyStreamListener? = null,
    uploadListener: CopyStreamListener? = null,
) {
    download({ outputStream }, listener, uploadListener)
}

suspend inline fun <T> NetworkService.get(
    noinline parser: DataParser<T>,
    noinline uploadListener: CopyStreamListener? = null
): T = get(parser, null, uploadListener)

suspend fun <T> NetworkService.get(
    parser: DataParser<T>,
    response: HttpHeadersResponse?,
    uploadListener: CopyStreamListener? = null
): T {
    return parser.parseObject(getString(response, uploadListener))
}

suspend inline fun <T> NetworkService.getList(
    noinline parser: DataParser<T>,
    noinline uploadListener: CopyStreamListener? = null
): List<T> = getList(parser, null, uploadListener)

suspend fun <T> NetworkService.getList(
    parser: DataParser<T>,
    response: HttpHeadersResponse?,
    uploadListener: CopyStreamListener? = null
): List<T> {
    return parser.parseList(getString(response, uploadListener))
}

suspend inline fun <T> NetworkService.get(
    serializer: DeserializationStrategy<T>
): T = get(null, serializer)

suspend fun <T> NetworkService.get(
    response: HttpHeadersResponse?,
    serializer: DeserializationStrategy<T>
): T {
    return NetworkService.Json.decodeFromString(serializer, getString(response))
}

suspend inline fun NetworkService.getBytes(
    noinline listener: CopyStreamListener? = null,
    noinline uploadListener: CopyStreamListener? = null,
): ByteArray = getBytes(null, listener, uploadListener)

suspend fun NetworkService.getBytes(
    response: HttpHeadersResponse?,
    listener: CopyStreamListener? = null,
    uploadListener: CopyStreamListener? = null,
): ByteArray {
    return download(
        { connection ->
            response?.set(connection)
            val contentLength = connection.contentLength
            ByteArrayOutputStream(maxOf(DEFAULT_BUFFER_SIZE, contentLength))
        },
        listener,
        uploadListener,
    ).toByteArray()
}

suspend fun NetworkService.downloadTo(file: File, listener: CopyStreamListener? = null) {
    file.outputStream().use { out ->
        downloadTo(out, listener = listener)
    }
}

