package com.vstd.todo.utilities

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun getColor(context: Context, resId: Int): Int {
    return ContextCompat.getColor(context, resId)
}

fun getContrastColor(color: Int): Int {
    val y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000
    return if (y >= 128) Color.BLACK else Color.WHITE
}

fun getRandomColor(): Int {
    val rnd = java.util.Random()
    return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
}

fun Fragment.log(msg: String) {
    Log.d(DEBUG_KEY, this.javaClass.simpleName + ":" + msg)
}

fun Activity.log(msg: String) {
    Log.d(DEBUG_KEY, this.javaClass.simpleName + ":" + msg)
}