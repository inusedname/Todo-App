package com.vstd.todo.others.utilities

import android.content.Context
import android.graphics.Color

object ColorUtils {
    fun getContrastColor(color: Int): Int {
        val y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000
        return if (y >= 128) Color.BLACK else Color.WHITE
    }

    fun getRandomColor(): Int {
        val rnd = java.util.Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}

fun Context.getColorArray(id: Int): List<Int> {
    val typedArray = resources.obtainTypedArray(id)
    val colors = mutableListOf<Int>()
    for (i in 0 until typedArray.length())
        colors.add(typedArray.getColor(i, 0))
    typedArray.recycle()
    return colors
}