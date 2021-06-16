package erika.core.net.datacontract

import android.webkit.MimeTypeMap
import erika.core.net.MimeType
import java.io.File
import java.io.InputStream

class FileBody(private val file: File, private val fileName: String? = null) : Body {
    override val contentType: String
        get() {
            val extension = fileName?.substringAfterLast(".", "") ?: file.extension
            val type = if (extension.isNotEmpty()) {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            } else null
            return type ?: MimeType.bin.contentType
        }

    override fun getContent(): InputStream {
        return file.inputStream()
    }

    override val isEmpty: Boolean
        get() = false

    override fun length(): Long {
        val length = file.length()
        return if (length == 0L) -1 else length
    }
}