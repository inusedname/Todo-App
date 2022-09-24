package com.vstd.todo.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/* Convert DateTime to pattern "yyyy-mm-ddTHH:mm:ss" */
@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
}

fun LocalDateTime.toFriendlyString(): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toFriendlyDateTimeString(): String {
    return this.toLocalDateTime().toFriendlyString()
}

fun LocalDateTime.toLong(): Long {
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun LocalDateTime.toTimeString(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun LocalDateTime.setTime(hour: Int, minute: Int): LocalDateTime {
    return this.withHour(hour).withMinute(minute)
}

@RequiresApi(Build.VERSION_CODES.O)
class DateTimeUtils {
    companion object {
        fun now(): LocalDateTime {
            return LocalDateTime.now()
        }

        val tomorrow = { date: LocalDateTime ->
            LocalDateTime.now().setTime(date.hour, date.minute).plusDays(1)
        }

        val nextWeek = { date: LocalDateTime ->
            LocalDateTime.now().setTime(date.hour, date.minute).plusWeeks(1)
        }

        val tonight = { _: LocalDateTime ->
            LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)
        }
    }
}
