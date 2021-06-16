package erika.core.net.datacontract

import erika.core.net.CopyStreamListener

interface NetworkService {
    suspend fun getString(listener: CopyStreamListener? = null): String

    suspend fun <T> get(parser: DataParser<T>, listener: CopyStreamListener? = null): T

    suspend fun <T> getList(parser: DataParser<T>, listener: CopyStreamListener? = null): List<T>
}