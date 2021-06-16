package erika.core.net.datacontract

import java.io.InputStream

class StreamBody(private val content: InputStream, override val contentType: String) : Body {

    override fun getContent() = content

    override val isEmpty: Boolean
        get() = false

    override fun length(): Long {
        return -1
    }
}

