package erika.core.net.datacontract.odata

import erika.core.net.datacontract.DataParser

data class ODataListResult<out T>(
        val items: List<T>,
        val nextLink: String
) {
    companion object {
        fun <T> createParser(parser: DataParser<T>): DataParser<ODataListResult<T>> = {
            ODataListResult(
                    items = readList(parser, "value"),
                    nextLink = readString("@odata.nextLink")
            )
        }
    }
}