package erika.core.net.datacontract

import android.os.Build
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object ISO0861DateParser {
    private val utc = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val local = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    private val timeZoned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US)
    } else {
        null
    }
    private val timeSecondZoned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)
    } else {
        null
    }

    fun parse(input: String?): Date? {
        if (input.isNullOrEmpty()) {
            return null
        }
        if (input.contains('Z')) {
            return tryParse(input, utc)
        }
        if (input.contains('.') && timeSecondZoned != null) {
            return tryParse(input, timeSecondZoned)
        }
        if (timeZoned != null) {
            return tryParse(input, timeZoned) ?: tryParse(input, local)
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