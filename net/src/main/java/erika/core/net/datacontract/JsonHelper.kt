package erika.core.net.datacontract

import kotlinx.datetime.Instant
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.text.ParseException
import java.util.*
import kotlin.reflect.KClass

class JsonHelper(private val json: JSONObject) {

    fun required(name: String): Nothing {
        throw RequiredFieldException(name)
    }

    fun hasField(name: String): Boolean {
        return json.has(name)
    }

    fun readNullableString(name: String): String? {
        return parseString(json.opt(name))
    }

    fun readString(name: String, defaultValue: String = ""): String {
        return readNullableString(name) ?: defaultValue
    }

    fun readNullableLong(name: String): Long? {
        return parseLong(json.opt(name))
    }

    fun readLong(name: String, defaultValue: Long = 0): Long {
        return readNullableLong(name) ?: defaultValue
    }

    fun readNullableDouble(name: String): Double? {
        return parseDouble(json.opt(name))
    }

    fun readDouble(name: String, defaultValue: Double = 0.0): Double {
        return readNullableDouble(name) ?: defaultValue
    }

    fun readNullableInt(name: String): Int? {
        return parseInt(json.opt(name))
    }

    fun readInt(name: String, defaultValue: Int = 0): Int {
        return readNullableInt(name) ?: defaultValue
    }

    fun readNullableBoolean(name: String): Boolean? {
        return parseBoolean(json.opt(name))
    }

    fun readBoolean(name: String, defaultValue: Boolean = false): Boolean {
        return readNullableBoolean(name) ?: defaultValue
    }

    fun <T> readObject(parser: DataParser<T>, name: String): T? {
        val json = readJsonObject(name) ?: return null
        return parser(JsonHelper(json))
    }

    /**
     * return null if the field is undefined
     */
    inline fun <reified T : Any> readDefined(name: String): Required<T?>? {
        return readDefinedTyped(name, T::class)
    }

    /**
     * return null if the field is undefined or null
     */
    inline fun <reified T : Any> readDefinedNonnull(name: String): Required<T>? {
        return readDefined<T>(name)?.nonnull()
    }

    @PublishedApi
    internal fun <T : Any> readDefinedTyped(name: String, clazz: KClass<T>): Required<T?>? {
        val value = json.opt(name) ?: return null
        val result: Any? = when (clazz) {
            String::class -> parseString(value)
            Int::class -> parseInt(value)
            Double::class -> parseDouble(value)
            Long::class -> parseLong(value)
            Boolean::class -> parseBoolean(value)
            else -> error("Only String, Int, Double, Long and Boolean are supported to check undefined value")
        }
        @Suppress("UNCHECKED_CAST")
        return Required(result) as Required<T?>
    }

    /**
     * Length of list may be less than the original
     * Null will be removed from the list.
     */
    fun <T> readList(
        parser: DataParser<T>,
        name: String,
        cancelOnException: Boolean = true
    ): List<T> {
        val array = readJsonArray(name) ?: return emptyList()
        val result = ArrayList<T>(array.length())
        for (i in 0 until array.length()) {
            val obj = (array.opt(i) as? JSONObject) ?: continue
            if (cancelOnException) {
                result.add(parser(JsonHelper(obj)))
            } else {
                val t: T
                try {
                    t = parser(JsonHelper(obj))
                } catch (_: Exception) {
                    continue
                }
                result.add(t)
            }
        }
        return result
    }

    fun readJsonArray(name: String): JSONArray? {
        return json.opt(name) as? JSONArray
    }

    fun readJsonObject(name: String): JSONObject? {
        return json.opt(name) as? JSONObject
    }

    fun readAny(name: String): JsonHelper? {
        val json = readJsonObject(name) ?: return null
        return JsonHelper(json)
    }

    fun readArray(name: String): Array<JsonHelper> {
        val array = readJsonArray(name) ?: return emptyArray()
        return Array(array.length()) { JsonHelper(array.getJSONObject(it)) }
    }

    fun <T> readMap(parser: DataParser<T>, name: String): Map<String, T> {
        val json = readJsonObject(name) ?: return emptyMap()
        val map = HashMap<String, T>()
        val iterator = json.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            map[key] = parser(JsonHelper(json.getJSONObject(key)))
        }
        return map
    }

    fun readDate(name: String): Date? {
        return try {
            ISO8601DateParser.parse(readNullableString(name))
        } catch (e: ParseException) {
            null
        }
    }


    fun readTime(name: String): Instant? {
        val iso = readNullableString(name) ?: return null
        return try {
            Instant.parse(iso)
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    /**
     * Length of list may be less than the original
     * Null will be removed from the list.
     */
    fun readStringList(name: String): List<String> {
        val array = readJsonArray(name) ?: return emptyList()
        val list = ArrayList<String>(array.length())
        for (i in 0 until array.length()) {
            parseString(array.opt(i))?.let { list.add(it) }
        }
        return list
    }

    /**
     * Length of list may be less than the original
     * Null will be removed from the list.
     */
    fun readIntList(name: String): List<Int> {
        val array = readJsonArray(name) ?: return emptyList()
        val list = ArrayList<Int>(array.length())
        for (i in 0 until array.length()) {
            parseInt(array.opt(i))?.let { list.add(it) }
        }
        return list
    }


    /**
     * Length of list may be less than the original
     * Null will be removed from the list.
     */
    fun readDoubleList(name: String): List<Double> {
        val array = readJsonArray(name) ?: return emptyList()
        val list = ArrayList<Double>(array.length())
        for (i in 0 until array.length()) {
            parseDouble(array.opt(i))?.let { list.add(it) }
        }
        return list
    }


    /**
     * Length of list may be less than the original
     * Null will be removed from the list.
     */
    fun readLongList(name: String): List<Long> {
        val array = readJsonArray(name) ?: return emptyList()
        val list = ArrayList<Long>(array.length())
        for (i in 0 until array.length()) {
            parseLong(array.opt(i))?.let { list.add(it) }
        }
        return list
    }


    /**
     * Length of list may be less than the original
     * Null will be removed from the list.
     */
    fun readBooleanList(name: String): List<Boolean> {
        val array = readJsonArray(name) ?: return emptyList()
        val list = ArrayList<Boolean>(array.length())
        for (i in 0 until array.length()) {
            parseBoolean(array.opt(i))?.let { list.add(it) }
        }
        return list
    }

    companion object {

        private fun parseBoolean(value: Any?): Boolean? {
            return when (value) {
                is Boolean -> value
                is String -> {
                    if ("true".equals(value, ignoreCase = true)) {
                        true
                    } else if ("false".equals(value, ignoreCase = true)) {
                        false
                    } else {
                        null
                    }
                }

                else -> null
            }
        }


        private fun parseDouble(value: Any?): Double? {
            return when (value) {
                is Double -> value
                is Number -> value.toDouble()
                is String -> value.toDoubleOrNull()
                else -> null
            }
        }

        private fun parseInt(value: Any?): Int? {
            return when (value) {
                is Int -> value
                is Number -> value.toInt()
                is String -> value.toIntOrNull()
                else -> null
            }
        }

        private fun parseLong(value: Any?): Long? {
            return when (value) {
                is Long -> value
                is Number -> value.toLong()
                is String -> value.toLong()
                else -> null
            }
        }

        private fun parseString(value: Any?): String? {
            return when (value) {
                is String -> value
                null, JSONObject.NULL -> null
                else -> value.toString()
            }
        }
    }
}

fun JsonHelper(s: String) = JsonHelper(JSONObject(JSONTokener(s)))

fun JsonHelper(obj: Map<*, *>) = JsonHelper(JSONObject(obj))
