package com.vstd.todo.utilities

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/* Convert DateTime to pattern "yyyy-mm-ddTHH:mm:ss" */

fun String.toLocalDate(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
    } catch (e: Exception) {
        null
    }

}

fun String.toLocalTime(): LocalTime? {
    return try {
        LocalTime.parse(this, DateTimeFormatter.ISO_TIME)
    } catch (e: Exception) {
        null
    }
}

fun String.toFriendlyDateTimeString(): String? {
    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
            .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
    } catch (e: Exception) {
        null
    }
}

fun LocalDate.toFriendlyString(): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}

fun LocalTime.toFriendlyString(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun LocalDate.toMilliSecEpoch(): Long {
    return this
        .atTime(9, 0)
        .atZone(ZoneId.systemDefault())
        .toEpochSecond() * 1000
}

class DateTimeUtils {
    companion object {

        fun tomorrow(): LocalDate {
            return LocalDate.now().plusDays(1)
        }

        fun nextWeek(): LocalDate {
            return LocalDate.now().plusWeeks(1)
        }

        fun tonight(): LocalTime {
            return LocalTime.of(23, 59, 59)
        }

        fun format(date: String, time: String): String? {
            val localDate = date.toLocalDate()
            val localTime = time.toLocalTime()
            if (localDate == null)
                return null
            var res = localDate.toFriendlyString()
            if (localTime != null)
                res += " " + localTime.toFriendlyString()
            return res
        }
    }
}
