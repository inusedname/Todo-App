package com.vstd.todo.utilities

import android.graphics.Color

object Constants {
    const val DATE_TIME_STRING = "DATE_TIME_STRING"
    const val WORKSPACE_NAME_STRING = "WORKSPACE_NAME"
    const val TASK = "TASK"
    const val TONIGHT = "TONIGHT"
    const val TOMORROW = "TOMORROW"
    const val NEXT_WEEK = "NEXT_WEEK"
    const val WORKSPACE_OBJ = "WORKSPACE_OBJ"

    val COLORS = mapOf(
        "red" to Color.parseColor("#FF7878"),
        "green" to Color.parseColor("#064420"),
        "blue" to Color.parseColor("#748DA6"),
        "yellow" to Color.parseColor("#FCFFA6"),
        "orange" to Color.parseColor("#FBC6A4"),
        "purple" to Color.parseColor("#B1B2FF"),
        "pink" to Color.parseColor("#F675A8"),
        "brown" to Color.parseColor("#5E454B"),
        "gray" to Color.parseColor("#898AA6"),
        "black" to Color.parseColor("#354259"),
        "white" to Color.parseColor("#FDFAF6"),
        "teal" to Color.parseColor("#A7D2CB"),
    )
}
