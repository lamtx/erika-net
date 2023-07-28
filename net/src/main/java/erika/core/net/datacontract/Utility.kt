package erika.core.net.datacontract

import android.os.Build
import android.webkit.MimeTypeMap
import erika.core.net.ContentType
import erika.core.net.CopyStreamListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URLEncoder

fun InputStream.readString(encoding: String?) = readBytes().toString(charset(encoding ?: "UTF8"))

fun CoroutineScope.throwsIfCancelled() {
    if (!isActive) {
        throw CancellationException()
    }
}

context(CoroutineScope)
fun InputStream.copyTo(
    out: OutputStream,
    estimatedSize: Long,
    listener: CopyStreamListener?,
) {
    val bufferSize = if (estimatedSize <= 0) {
        DEFAULT_BUFFER_SIZE
    } else {
        estimatedSize.coerceAtMost(DEFAULT_BUFFER_SIZE * 4L).toInt()
    }
    val buffer = ByteArray(bufferSize)
    var len: Int
    var current = 0L
    while (true) {
        len = read(buffer)
        if (len <= 0) {
            break
        }
        throwsIfCancelled()
        current += len
        listener?.invoke(current, estimatedSize, false)
        out.write(buffer, 0, len)
        if (listener != null) {
            out.flush()
        }
    }
    listener?.invoke(current, estimatedSize, true)
}

val JSONObject.isEmpty: Boolean
    get() = length() == 0

fun JSONObject.toUrlEncoded(): String {
    val sb = StringBuilder()
    val keys = keys()
    var key: String
    while (keys.hasNext()) {
        if (sb.isNotEmpty()) {
            sb.append("&")
        }
        key = keys.next()
        val value = get(key)
        if (value is JSONObject || value is JSONArray) {
            throw RuntimeException("Content has object, can not be formatted at urlEncoded")
        }
        sb.append(URLEncoder.encode(key, "utf-8"))
            .append("=")
            .append(URLEncoder.encode(value.toString(), "utf-8"))

    }
    return sb.toString()
}

fun JSONObject.toUrlEncodedBody() = UrlEncodedBody(toUrlEncoded())

fun JSONObject.toJsonBody() = JsonBody(this)

fun JSONArray.toJsonBody() = JsonBody(this)

fun File.getMimeType(): ContentType? {
    val ext = extension
    return if (ext.isEmpty()) null else {
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
        if (mimeType == null) null else {
            ContentType.parse(mimeType)
        }
    }
}

val HttpURLConnection.contentLengthCompat: Long
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentLengthLong
        } else {
            getHeaderField("content-length")?.toLongOrNull() ?: -1L
        }
    }

val HttpURLConnection.charset: String?
    get() {
        val contentType = contentType ?: return null
        for (s in contentType.split(";")) {
            val value = s.trim().lowercase()
            if (value.startsWith("charset=")) {
                return value.substring(8) // length of charset=
            }
        }
        return null
    }