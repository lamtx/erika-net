package erika.core.net.datacontract

import erika.core.net.ContentType
import erika.core.net.HttpHeaders
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.SequenceInputStream
import java.util.*

class MultiPart internal constructor(
    private val children: List<Part>,
    override val contentType: ContentType,
    private val boundary: ByteArray,
    private val boundaryEnd: ByteArray,
) : Body {

    override fun getContent(): InputStream {
        val group = Vector<InputStream>()
        for (part in children) {
            group.add(ByteArrayInputStream(boundary))
            group.add(ByteArrayInputStream(part.headers))
            group.add(part.body.getContent())
        }
        group.add(ByteArrayInputStream(boundaryEnd))
        return SequenceInputStream(group.elements())
    }

    override fun length(): Long {
        var size = 0L
        for (part in children) {
            val bodyLength = part.body.length()
            if (bodyLength < 0) {
                error("MultiPart contains none fixed body length")
            }
            size += boundary.size
            size += part.headers.size
            size += bodyLength
        }
        size += boundaryEnd.size
        return size
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (part in children) {
            sb.append(boundary.toString(Charsets.US_ASCII))
            sb.append(part.headers.toString(Charsets.US_ASCII))
            sb.append(part.body.toString())
        }
        sb.append(boundaryEnd.toString(Charsets.US_ASCII))
        return sb.toString()
    }

    companion object {

        val Alternative: ContentType get() = ContentType("multipart", "alternative")

        val ByteRanges: ContentType get() = ContentType("multipart", "byteranges")

        val Digest: ContentType get() = ContentType("multipart", "digest")

        val FormData: ContentType get() = ContentType("multipart", "form-data")

        val Mixed: ContentType get() = ContentType("multipart", "mixed")

        val Parallel: ContentType get() = ContentType("multipart", "parallel")

        val Related: ContentType get() = ContentType("multipart", "related")

    }

}

fun MultiPart(
    children: List<Part>,
    contentType: ContentType = MultiPart.Related
): MultiPart {
    val boundary = "--${UUID.randomUUID()}"
    return MultiPart(
        children = children,
        contentType = contentType.copy(parameters = mapOf("boundary" to boundary)),
        boundary = "\r\n--$boundary\r\n".toByteArray(Charsets.US_ASCII),
        boundaryEnd = "\r\n--$boundary--".toByteArray(Charsets.US_ASCII),
    )
}

class Part internal constructor(
    val body: Body,
    val headers: ByteArray,
)

private fun encodeHeader(body: Body, headers: Map<String, String>): ByteArray {
    val sb = StringBuilder()
    for ((key, value) in headers.entries) {
        sb.writeHeader(key, value)
    }
    sb.writeHeader(HttpHeaders.ContentType, body.contentType.mimeType)
    sb.append("\r\n")
    return sb.toString().toByteArray(Charsets.US_ASCII)
}

private fun StringBuilder.writeHeader(header: String, value: String) {
    append(header)
    append(": ")
    append(value)
    append("\r\n")
}

fun Part(body: Body, headers: Map<String, String> = emptyMap()): Part {
    return Part(
        body = body,
        headers = encodeHeader(body, headers),
    )
}
