package erika.core.net.datacontract

@ParameterMarker
class HeaderBuilder(private val headers: MutableList<Pair<String, String>>) {

    infix fun String.x(value: String) {
        headers.add(Pair(this, value))
    }
}