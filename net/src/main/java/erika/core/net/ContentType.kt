package erika.core.net

@Deprecated("Renamed to `ContentType`")
typealias MimeType = ContentType

class ContentType private constructor(
    val primaryType: String,
    val subType: String?,
    val parameters: Map<String, String?> = emptyMap(),
) {
    constructor(
        primaryType: String,
        subType: String?,
        charset: String? = null,
        parameters: Map<String, String> = emptyMap()
    ) : this(primaryType, subType, lowercase(parameters, charset))

    val charset: String?
        get() = parameters["charset"]

    val mimeType: String
        get() {
            val sb = StringBuilder(primaryType)
            if (subType != null) {
                sb.append("/").append(subType)
            }
            for ((key, value) in parameters) {
                sb.append("; ").append(key)
                if (value != null) {
                    sb.append("=")
                    if (value.contains(' ')) {
                        sb.append("\"").append(value).append("\"")
                    } else {
                        sb.append(value)
                    }
                }
            }
            return sb.toString()
        }

    fun copy(
        primaryType: String = this.primaryType,
        subType: String? = this.subType,
        parameters: Map<String, String?> = this.parameters,
    ): ContentType = ContentType(primaryType, subType, parameters)

    override fun toString() = mimeType

    companion object {
        /// Content type for plain text using UTF-8 encoding.
        ///
        ///     text/plain; charset=utf-8
        val Text = ContentType("text", "plain", charset = "utf-8")

        /// Content type for HTML using UTF-8 encoding.
        ///
        ///    text/html; charset=utf-8
        val Html = ContentType("text", "html", charset = "utf-8")

        /// Content type for JSON using UTF-8 encoding.
        ///
        ///    application/json; charset=utf-8
        val Json = ContentType("application", "json", charset = "utf-8")

        /// Content type for binary data.
        ///
        ///    application/octet-stream
        val Binary = ContentType("application", "octet-stream")

        fun parse(mimeType: String): ContentType {
            val list = mimeType.split(';')
            val head = list.first()
            val slash = head.indexOf('/')
            val primaryType: String
            val subType: String?
            val params = mutableMapOf<String, String?>()
            if (slash == -1) {
                primaryType = head.trim()
                subType = null
            } else {
                primaryType = head.substring(0, slash).trim()
                subType = head.substring(slash + 1).trim()
            }
            for (i in 1 until list.size) {
                val index = list[i].indexOf('=')
                if (index == -1) {
                    params[list[i].trim()] = null
                } else {
                    params[list[i].substring(index).trim()] = list[i].substring(index + 1).trim()
                }
            }
            return ContentType(primaryType, subType, params)
        }

        private fun lowercase(
            parameters: Map<String, String>,
            charset: String?
        ): Map<String, String> {
            val lowercaseMap = mutableMapOf<String, String>()
            for ((key, value) in parameters) {
                val lowercaseKey = key.lowercase()
                if (lowercaseKey == "charset") {
                    lowercaseMap[lowercaseKey] = value.lowercase()
                } else {
                    lowercaseMap[lowercaseKey] = value
                }
            }
            if (charset != null) {
                lowercaseMap["charset"] = charset.lowercase()
            }
            return lowercaseMap
        }
    }
}
