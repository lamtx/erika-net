package erika.core.net

import java.net.HttpURLConnection

interface Credentials {
    fun prepareRequest(connection: HttpURLConnection)
}