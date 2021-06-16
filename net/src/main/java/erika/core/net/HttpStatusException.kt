package erika.core.net

import java.net.HttpURLConnection
import java.util.*

class HttpStatusException(val content: String, val statusCode: Int) : Exception(String.format(Locale.US, "%d : %s", statusCode, content)) {

    val isOk: Boolean
        get() = statusCode == HttpURLConnection.HTTP_OK

    val isUnauthorized: Boolean
        get() = statusCode == HttpURLConnection.HTTP_UNAUTHORIZED

    val statusCodeDescription: String
        get() = "HTTP Status code $statusCode"

    val isClientError: Boolean
        get() = statusCode in 400 until 500

    companion object {
        private const val serialVersionUID = -4314839439005366241L
    }
}
