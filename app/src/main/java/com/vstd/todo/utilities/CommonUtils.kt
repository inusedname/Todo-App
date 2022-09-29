package com.vstd.todo.utilities

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat

fun getColor(context: Context, resId: Int): Int {
    return ContextCompat.getColor(context, resId)
}

fun getContrastColor(color: Int): Int {
    val y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000
    return if (y >= 128) Color.BLACK else Color.WHITE
}