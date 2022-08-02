package erika.core.net.datacontract

import org.json.JSONObject

typealias ParameterCreator = ParameterBuilder.() -> Unit

// False positive, still inline `this`
@Suppress("NOTHING_TO_INLINE")
inline fun ParameterCreator.toJson(): JSONObject {
    val parameterBuilder = ParameterBuilder()
    this(parameterBuilder)
    return parameterBuilder.toJson()
}

fun ParameterCreator.toJsonBody() = toJson().toJsonBody()

fun ParameterCreator.toUrlEncodedBody() = toJson().toUrlEncodedBody()