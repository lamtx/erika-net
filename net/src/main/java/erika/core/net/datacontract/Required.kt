package erika.core.net.datacontract

/**
 * [JsonObject] ignores the null value or empty string. To accept it, wrap the value to this object.
 */
class Required<T>(val value: T) {
    override fun toString() = value.toString()

    override fun equals(other: Any?): Boolean = other is Required<*> && other.value == value

    override fun hashCode() = value.hashCode()
}

fun <T : Any> Required<T?>?.nonnull(): Required<T>? {
    return if (this == null || value == null) null else Required(value)
}