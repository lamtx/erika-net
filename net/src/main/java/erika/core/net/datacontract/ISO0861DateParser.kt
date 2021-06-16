package erika.core.net.datacontract

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object ISO0861DateParser {
    private val utc = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val local = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    fun parse(input: String?): Date? {
        if (input.isNullOrEmpty()) {
            return null
        }
        if (input.contains('Z')) {
            return tryParse(input, utc)
        }
        return tryParse(input, local)
    }

    private fun tryParse(s: String, formatter: DateFormat): Date? {
        return try {
            formatter.parse(s)
        } catch (e: ParseException) {
            null
        }
    }

    fun format(date: Date?): String? {
        return if (date == null) null else utc.format(date)
    }

    fun formatLocal(date: Date?): String? {
        return if (date == null) null else local.format(date)
    }
}