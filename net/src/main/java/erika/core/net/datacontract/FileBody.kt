package erika.core.net.datacontract

import erika.core.net.ContentType
import java.io.File
import java.io.InputStream

class FileBody(
    private val file: File,
    override val contentType: ContentType = file.getMimeType() ?: ContentType.Binary,
) : Body {

    override fun getContent(): InputStream {
        return file.inputStream()
    }

    override fun length(): Long {
        val length = file.length()
        return if (length == 0L) -1 else length
    }
}