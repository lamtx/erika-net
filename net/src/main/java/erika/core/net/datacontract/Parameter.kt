package erika.core.net.datacontract

import org.json.JSONArray
import org.json.JSONObject

interface Parameter {
    fun parameterCreator(t: Creator)

    class Creator(@PublishedApi internal val builder: ParameterBuilder) {
        inline fun addAll(t: ParameterBuilder.() -> Unit) {
            t(builder)
        }
    }
}

fun Parameter.toJson(): JSONObject {
    val builder = ParameterBuilder()
    parameterCreator(Parameter.Creator(builder))
    return builder.toJson()
}

fun List<Parameter>.toJson(): JSONArray {
    val array = JSONArray()
    for (e in this) {
        array.put(e.toJson())
    }
    return array
}

fun Parameter.toJsonBody(): StringBody = toJson().toJsonBody()

fun List<Parameter>.toJsonBody(): StringBody = toJson().toJsonBody()

fun Parameter.toUrlEncodedBody(): StringBody = toJson().toUrlEncodedBody()