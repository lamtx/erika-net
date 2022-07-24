package erika.core.net.datacontract

import erika.core.net.ContentType
import java.io.InputStream

class StreamBody(
    private val content: InputStream,
    override val contentType: ContentType = ContentType.Binary,
    private val length: Long = -1L
) : Body {

    override fun getContent() = content

    override fun length(): Long = length
}

