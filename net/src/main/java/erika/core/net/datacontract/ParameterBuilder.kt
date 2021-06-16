package erika.core.net.datacontract

import org.json.JSONArray
import org.json.JSONObject
import java.util.*

@ParameterMarker
class ParameterBuilder(private val properties: JSONObject) {

    constructor() : this(JSONObject())

    fun string(s: String?) = EmptyString(s)

    infix fun String.x(value: Null) {
        properties.put(this, JSONObject.NULL)
    }

    infix fun String.x(value: EmptyString) {
        properties.put(this, value.value)
    }

    infix fun String.x(value: Date?) {
        if (value != null) {
            properties.put(this, ISO0861DateParser.format(value))
        }
    }

    infix fun String.x(value: CharSequence?) {
        if (!value.isNullOrEmpty()) {
            properties.put(this, value)
        }
    }

    infix fun String.x(value: Int) {
        properties.put(this, value)
    }

    infix fun String.x(value: Double) {
        properties.put(this, value)
    }

    infix fun String.x(value: Long) {
        properties.put(this, value)
    }

    infix fun String.x(value: Boolean) {
        properties.put(this, value)
    }

    infix fun String.x(value: Number?) {
        when {
            value is Int -> properties.put(this, value)
            value is Long -> properties.put(this, value)
            value is Double -> properties.put(this, value)
            value is Float -> properties.put(this, value)
            value is Byte -> properties.put(this, value)
            value != null -> properties.put(this, value)
        }
    }

    inline infix fun String.x(block: ParameterCreator) {
        val child = ParameterBuilder()
        block(child)
    }

    infix fun String.x(values: Map<String, Any>?) {
        if (values != null) {
            val json = JSONObject()
            for ((key, value) in values) {
                test(value)?.let {
                    json.put(key, it)
                }
            }
            properties.put(this, json)
        }
    }

    infix fun String.x(value: Parameter?) {
        if (value != null) {
            val json = value.toJson()
            properties.put(this, json)
        }
    }

    private fun Iterable<*>.isEmpty(): Boolean {
        return if (this is Collection<*>) {
            isEmpty()
        } else {
            iterator().hasNext()
        }
    }

    private fun Iterable<*>.isNotEmpty() = !isEmpty()

    infix fun String.x(values: Iterable<*>?) {
        if (values != null && values.isNotEmpty()) {
            val array = JSONArray()
            for (data in values) {
                test(data)?.let {
                    array.put(it)
                }
            }
            properties.put(this, array)
        }
    }

    private fun test(value: Any?): Any? {
        if (value == null || value == JSONObject.NULL || value is JSONObject || value is JSONArray || value is String || value is Number) {
            return value
        }
        if (value is Parameter) {
            return value.toJson()
        }
        if (value is Date) {
            return ISO0861DateParser.format(value)
        }
        if (value is Iterable<*>) {
            if (value.isEmpty()) {
                return null
            }
            val array = JSONArray()
            for (e in value) {
                test(e)?.let {
                    array.put(it)
                }
            }
            return array
        }
        if (value is Map<*, *>) {
            if (value.isEmpty()) {
                return null
            }
            val result = JSONObject()
            for ((k, v) in value) {
                if (k !is String) {
                    throw  error("Unsupported Json with name is not a String")
                }
                test(v)?.let {
                    result.put(k, it)
                }
            }
        }
        throw error("Unsupported Json type ${value.javaClass.name}")
    }

    infix fun String.x(values: Array<*>?) {
        this x values?.asList()
    }

    infix fun String.x(values: IntArray) {
        val array = JSONArray()
        for (data in values) {
            array.put(data)
        }
        properties.put(this, array)
    }

    @PublishedApi
    internal fun toJson(): JSONObject {
        return properties
    }

    object Null

    class EmptyString(val value: String?)
}