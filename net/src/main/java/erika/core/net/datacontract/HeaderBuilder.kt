package erika.core.net.datacontract

@ParameterMarker
class HeaderBuilder(private val headers: MutableList<Pair<String, String>>) {

    @Deprecated(
        message = "Use `set` instead.",
        replaceWith = ReplaceWith("set")
    )
    @Suppress("NOTHING_TO_INLINE")
    inline infix fun String.x(value: String) {
        set(value)
    }

    infix fun String.set(value: String) {
        headers.add(Pair(this, value))
    }
}