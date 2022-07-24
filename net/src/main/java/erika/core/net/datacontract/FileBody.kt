package erika.core.net.datacontract

import erika.core.net.ContentType
import java.io.File
import java.io.InputStream

class FileBody(
    private val file: File,
    override val contentType: ContentType = file.getMimeType() ?: ContentType.Binary,
) : Body {

    override fun getContent(): InputStream = file.inputStream()

    override fun length() = file.length()
}