package erika.core.net.datacontract

import erika.core.net.CopyStreamListener
import erika.core.net.Credentials
import erika.core.net.HttpMethod
import java.io.OutputStream

class RequestBuilder(private val url: String) : NetworkService {
    private var method = HttpMethod.Get
    private var body: Body? = null
    private var params: String? = null
    private val headers = mutableListOf<Pair<String, String>>()
    private var credentials: Credentials? = null
    private var responseHeaders: MutableMap<String, String>? = null

    fun authorize(credentials: Credentials?) = apply {
        this.credentials = credentials
    }

    fun body(body: Body) = apply {
        this.body = body
    }

    fun jsonBody(body: JsonObject) = apply {
        this.body = body.toJsonBody()
    }

    fun jsonBody(body: List<JsonObject>) = apply {
        this.body = body.toJsonBody()
    }

    fun urlEncodedBody(body: JsonObject) = apply {
        this.body = body.toUrlEncodedBody()
    }

    fun jsonBody(p: ParameterCreator) = apply {
        this.body = p.toJsonBody()
    }

    fun urlEncodedBody(p: ParameterCreator) = apply {
        this.body = p.toUrlEncodedBody()
    }

    fun params(params: String?) = apply {
        this.params = params
    }

    inline fun params(p: ParameterCreator) = apply {
        params(p.toJson().toUrlEncoded())
    }

    fun method(method: HttpMethod) = apply {
        this.method = method
    }

    fun headers(h: HeaderBuilder.() -> Unit) = apply {
        h(HeaderBuilder(headers))
    }

    fun responseHeaders(headers: MutableMap<String, String>) = apply {
        responseHeaders = headers
    }

    fun build() = Request(
        url = url,
        method = method,
        body = body,
        params = params,
        headers = headers,
        credentials = credentials,
        responseHeaders = responseHeaders,
    )

    override suspend fun getString(listener: CopyStreamListener?): String {
        return NetClient.getString(build(), listener)
    }

    override suspend fun downloadTo(outputStream: OutputStream, listener: CopyStreamListener?) {
        return NetClient.downloadTo(build(), outputStream, listener)
    }
}