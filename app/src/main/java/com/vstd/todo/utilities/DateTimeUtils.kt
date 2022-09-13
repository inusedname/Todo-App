package com.vstd.todo.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/* Convert DateTime to pattern "yyyy-mm-ddTHH:mm:ss" */
@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
}

@RequiresApi(Build.VERSION_CODES.O)
class DateTimeUtils {
    companion object {
        fun now(): LocalDateTime {
            return LocalDateTime.now()
        }
    }
}
