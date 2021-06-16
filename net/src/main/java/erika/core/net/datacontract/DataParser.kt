package erika.core.net.datacontract

import org.json.JSONArray
import org.json.JSONTokener
import java.util.*

typealias DataParser<T> = JsonHelper.() -> T

fun <T> DataParser<T>.parseObject(string: String): T {
    return this(JsonHelper(string))
}

fun <T> DataParser<T>.parserList(string: String?): List<T> {
    if (string.isNullOrEmpty()) {
        return emptyList()
    }
    val json = JSONArray(JSONTokener(string))
    val len = json.length()
    val list = ArrayList<T>(len)
    for (i in 0 until len) {
        list.add(this(JsonHelper(json.getJSONObject(i))))
    }
    return list
}

