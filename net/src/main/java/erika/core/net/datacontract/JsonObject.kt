package erika.core.net.datacontract

import org.json.JSONArray
import org.json.JSONObject

@Deprecated("Use [JsonObject]", replaceWith = ReplaceWith("JsonObject"))
interface Parameter : JsonObject {

    fun parameterCreator(t: Creator)

    override fun ParameterBuilder.describeContent() {
        parameterCreator(Creator(this))
    }

    class Creator(@PublishedApi internal val builder: ParameterBuilder) {
        inline fun addAll(t: ParameterBuilder.() -> Unit) {
            t(builder)
        }
    }
}

interface JsonObject {
    fun ParameterBuilder.describeContent()
}

fun JsonObject.toJson(): JSONObject {
    return ParameterBuilder().apply {
        describeContent()
    }.toJson()
}


fun List<JsonObject>.toJson(): JSONArray {
    val array = JSONArray()
    for (e in this) {
        array.put(e.toJson())
    }
    return array
}

fun Map<String, JsonObject>.toJson(): JSONObject {
    val obj = JSONObject()
    for ((key, value) in this) {
        obj.put(key, value.toJson())
    }
    return obj
}

fun JsonObject.toJsonBody(): StringBody = toJson().toJsonBody()

fun List<JsonObject>.toJsonBody(): StringBody = toJson().toJsonBody()

fun JsonObject.toUrlEncodedBody(): StringBody = toJson().toUrlEncodedBody()