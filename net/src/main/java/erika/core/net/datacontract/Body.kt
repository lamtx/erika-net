package erika.core.net.datacontract

import java.io.InputStream

interface Body {
    val contentType: String
    fun getContent(): InputStream
    val isEmpty: Boolean
    /**
     * Return length of body or -1 if unknown
     */
    fun length(): Long
}