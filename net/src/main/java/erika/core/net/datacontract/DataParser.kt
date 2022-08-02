package erika.core.net.datacontract

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

typealias DataParser<T> = JsonHelper.() -> T

fun <T> DataParser<T>.parseObject(string: String): T {
    return this(JsonHelper(string))
}

fun <T> DataParser<T>.parseList(string: String?): List<T> {
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

fun <T> DataParser<T>.parseObject(obj: Map<*, *>): T {
    return this(JsonHelper(obj))
}

fun <T> DataParser<T>.parseObject(obj: JSONObject): T {
    return this(JsonHelper(obj))
}

fun <T> DataParser<T>.parseList(objects: List<*>): List<T> {
    return objects.map {
        if (it is Map<*, *>) {
            this(JsonHelper(it))
        } else {
            error("The provided list is not a list of Map.")
        }
    }
}