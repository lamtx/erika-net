package erika.core.net.datacontract

import erika.core.net.ContentType
import java.io.InputStream

interface Body {
    val contentType: ContentType

    fun getContent(): InputStream

    /**
     * Return length of body or -1 if unknown
     */
    fun length(): Long
}