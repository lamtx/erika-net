package erika.core.net.datacontract

import erika.core.net.CopyStreamListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream

interface NetworkService {
    suspend fun getString(listener: CopyStreamListener? = null): String

    suspend fun <T> get(parser: DataParser<T>, listener: CopyStreamListener? = null): T {
        return parser.parseObject(getString(listener))
    }

    suspend fun <T> getList(parser: DataParser<T>, listener: CopyStreamListener? = null): List<T> {
        return parser.parseList(getString(listener))
    }

    suspend fun downloadTo(outputStream: OutputStream, listener: CopyStreamListener? = null)

    suspend fun downloadAll(listener: CopyStreamListener? = null): ByteArray {
        return ByteArrayOutputStream().use { out ->
            downloadTo(out, listener)
            out.toByteArray()
        }
    }

    suspend fun download(file: File, listener: CopyStreamListener? = null) {
        file.outputStream().use { out ->
            downloadTo(out, listener)
        }
    }

    suspend fun getHeaders(): Map<String, List<String>>
}