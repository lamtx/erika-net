package erika.core.net.datacontract

import erika.core.net.ContentType
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset

class StringBody(
    text: String,
    override val contentType: ContentType = ContentType.Text
) : Body {
    private val rawData = text.toByteArray(Charset.forName(contentType.charset ?: "utf-8"))

    override fun getContent(): InputStream {
        return ByteArrayInputStream(rawData)
    }

    override fun length() = rawData.size.toLong()

    override fun toString() = String(
        bytes = rawData,
        charset = Charset.forName(contentType.charset ?: "utf-8")
    )
}

@Suppress("FUNCTIONNAME")
fun JsonBody(p: ParameterCreator) = p.toJsonBody()

@Suppress("FUNCTIONNAME")
fun JsonBody(content: String) = StringBody(content, ContentType.Json)

@Suppress("FUNCTIONNAME")
fun JsonBody(json: JSONObject) = JsonBody(json.toString())

@Suppress("FUNCTIONNAME")
fun JsonBody(json: JSONArray) = JsonBody(json.toString())

@Suppress("FUNCTIONNAME")
fun UrlEncodedBody(content: String) =
    StringBody(content, ContentType("application", "x-www-form-urlencoded"))

@Suppress("FUNCTIONNAME")
fun UrlEncodedBody(json: JSONObject) = UrlEncodedBody(json.toUrlEncoded())