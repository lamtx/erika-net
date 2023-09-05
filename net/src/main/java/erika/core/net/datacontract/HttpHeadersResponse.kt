package erika.core.net.datacontract

import erika.core.net.HttpHeaders
import java.net.HttpURLConnection

class HttpHeadersResponse {
    private var _headers: HttpHeaders? = null
    private var _statusCode: Int = Int.MIN_VALUE

    val headers: HttpHeaders
        get() = _headers ?: error("Headers has not set.")

    val statusCode: Int get() = if (_statusCode == Int.MIN_VALUE) error("Status code has not set.") else _statusCode

    internal fun set(connection: HttpURLConnection) {
        if (_headers != null) {
            error("Cannot call this method twice.")
        }
        _statusCode = connection.responseCode
        _headers = HttpHeaders(connection.headerFields)
    }
}