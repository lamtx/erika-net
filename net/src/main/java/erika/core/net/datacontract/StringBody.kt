package erika.core.net.datacontract

import erika.core.net.MimeType
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset

class StringBody(content: String, override val contentType: String) : Body {
    private val rawData = content.toByteArray(Charset.forName("UTF-8"))

    override fun getContent(): InputStream {
        return ByteArrayInputStream(rawData)
    }

    override val isEmpty: Boolean
        get() = rawData.isEmpty()

    override fun length() = rawData.size.toLong()
}

@Suppress("FUNCTIONNAME")
fun JsonBody(content: String) = StringBody(content, MimeType.json.contentType)

@Suppress("FUNCTIONNAME")
fun JsonBody(json: JSONObject) = JsonBody(json.toString())

@Suppress("FUNCTIONNAME")
fun JsonBody(json: JSONArray) = JsonBody(json.toString())

@Suppress("FUNCTIONNAME")
fun UrlEncodedBody(content: String) = StringBody(content, "application/x-www-form-urlencoded")

@Suppress("FUNCTIONNAME")
fun UrlEncodedBody(json: JSONObject) = UrlEncodedBody(json.toUrlEncoded())