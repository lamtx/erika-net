package erika.core.net.datacontract

import erika.core.net.CopyStreamListener
import java.io.File

interface NetworkService {
    suspend fun getString(listener: CopyStreamListener? = null): String

    suspend fun <T> get(parser: DataParser<T>, listener: CopyStreamListener? = null): T {
        return parser.parseObject(getString(listener))
    }

    suspend fun <T> getList(parser: DataParser<T>, listener: CopyStreamListener? = null): List<T> {
        return parser.parseList(getString(listener))
    }

    suspend fun download(file: File, listener: CopyStreamListener? = null)
}