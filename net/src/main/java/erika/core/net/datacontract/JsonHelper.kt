package erika.core.net.datacontract

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.text.ParseException
import java.util.*

class JsonHelper(private val json: JSONObject) {

    fun required(name: String): Nothing {
        throw RequiredFieldException(name)
    }

    fun hasField(property: String): Boolean {
        return json.has(property)
    }

    fun readString(property: String, defaultValue: String): String {
        return try {
            json.getString(property) ?: defaultValue
        } catch (e: JSONException) {
            defaultValue
        }
    }

    fun readString(property: String): String {
        return try {
            if (json.isNull(property)) {
                ""
            } else {
                json.getString(property)
            }
        } catch (e: JSONException) {
            ""
        }
    }

    fun readLong(property: String): Long {
        return try {
            json.getLong(property)
        } catch (e: JSONException) {
            0
        }
    }

    fun readNullableLong(property: String): Long? {
        return if (json.isNull(property)) {
            null
        } else {
            try {
                json.getLong(property)
            } catch (e: JSONException) {
                null
            }
        }
    }

    fun readNullableInt(property: String): Int? {
        return if (json.isNull(property)) {
            null
        } else {
            try {
                json.getInt(property)
            } catch (e: JSONException) {
                null
            }
        }
    }

    fun readInt(property: String): Int {
        return try {
            json.getInt(property)
        } catch (e: JSONException) {
            0
        }
    }

    fun <T> readObject(parser: DataParser<T>, property: String): T? {
        val json = readJsonObject(property) ?: return null
        return parser(JsonHelper(json))
    }

    fun readAny(property: String): JsonHelper? {
        val json = readJsonObject(property) ?: return null
        return JsonHelper(json)
    }

    fun <T> readList(parser: DataParser<T>, property: String): List<T> {
        val array = readJsonArray(property) ?: return emptyList()
        val result = ArrayList<T>(array.length())
        for (i in 0 until array.length()) {
            val t: T
            try {
                t = parser(JsonHelper(array.getJSONObject(i)))
            } catch (e: JSONException) {
                continue
            }

            result.add(t)
        }
        return result
    }

    fun readArray(property: String): Array<JsonHelper> {
        val array = readJsonArray(property) ?: return emptyArray()
        return Array(array.length()) { JsonHelper(array.getJSONObject(it)) }
    }

    fun <T> readMap(parser: DataParser<T>, property: String): Map<String, T> {
        val json = readJsonObject(property) ?: return emptyMap()
        val map = HashMap<String, T>()
        val iterator = json.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            map[key] = parser(JsonHelper(json.getJSONObject(key)))
        }
        return map
    }

    fun readBoolean(property: String, defaultValue: Boolean): Boolean {
        return try {
            json.getBoolean(property)
        } catch (e: JSONException) {
            defaultValue
        }
    }

    fun readBoolean(property: String): Boolean {
        return readBoolean(property, false)
    }

    fun readDate(property: String): Date? {
        return try {
            ISO0861DateParser.parse(json.getString(property))
        } catch (e: JSONException) {
            null
        } catch (e: ParseException) {
            null
        }

    }

    fun readJsonArray(property: String): JSONArray? {
        return try {
            json.getJSONArray(property)
        } catch (e: JSONException) {
            null
        }
    }

    fun readJsonObject(property: String): JSONObject? {
        return try {
            json.getJSONObject(property)
        } catch (e: JSONException) {
            null
        }
    }

    fun readStringArray(property: String): Array<String> {
        return readJsonArray(property)?.let { jsonArray ->
            Array(jsonArray.length()) {
                try {
                    jsonArray.getString(it)
                } catch (e: JSONException) {
                    ""
                }
            }
        } ?: emptyArray()
    }

    fun readStringList(property: String): List<String> {
        return readJsonArray(property)?.let { jsonArray ->
            List<String>(jsonArray.length()) {
                try {
                    jsonArray.getString(it)
                } catch (e: JSONException) {
                    ""
                }
            }
        } ?: emptyList()
    }

    fun readDouble(property: String, defaultValue: Double = 0.0): Double {
        return try {
            json.getDouble(property)
        } catch (e: JSONException) {
            defaultValue
        }
    }
}

@Suppress("FUNCTIONNAME")
fun JsonHelper(s: String) = JsonHelper(JSONObject(JSONTokener(s)))
