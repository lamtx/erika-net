package erika.core.net.datacontract

import erika.core.net.CopyStreamListener
import erika.core.net.Credentials
import erika.core.net.HttpMethod
import java.io.OutputStream
import java.net.HttpURLConnection

class Request(
    val url: String,
    val method: HttpMethod = HttpMethod.Get,
    val body: Body? = null,
    val params: String? = null,
    val headers: List<Pair<String, String>> = emptyList(),
    val credentials: Credentials? = null,
    val modifier: HttpURLConnection.() -> Unit = { },
    val responseHeaders: MutableMap<String, String>? = null,
) : NetworkService {
    val fullUrl: String
        get() {
            return if (params.isNullOrEmpty()) {
                url
            } else {
                if (url.contains('?')) {
                    "$url&${params}"
                } else {
                    "$url?${params}"
                }
            }
        }

    override suspend fun getString(listener: CopyStreamListener?): String {
        return NetClient.getString(this, listener)
    }

    override suspend fun downloadTo(outputStream: OutputStream, listener: CopyStreamListener?) {
        return NetClient.downloadTo(this, outputStream, listener)
    }
}