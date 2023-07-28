package erika.core.net.datacontract

import android.net.Network
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
    val network: Network? = null,
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

    override suspend fun <T : OutputStream> download(
        outputFactory: (HttpURLConnection) -> T,
        listener: CopyStreamListener?,
        uploadListener: CopyStreamListener?,
    ): T {
        return NetClient.download(this, outputFactory, listener, uploadListener)
    }
}