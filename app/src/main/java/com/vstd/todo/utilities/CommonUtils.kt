package com.vstd.todo.utilities

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.vstd.todo.R

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.snack(view: View, message: String, anchorView: View? = null) {
    Snackbar.make(this, view, message, Snackbar.LENGTH_SHORT)
        .setAnchorView(anchorView)
        .show()
}

fun Activity.snackNotAvaiable(view: View, anchorView: View? = null) {
    Snackbar.make(this, view, getString(R.string.feature_not_available), Snackbar.LENGTH_SHORT)
        .setAnchorView(anchorView)
        .show()
}

fun Activity.snackAlert(view: View, message: String, anchorView: View? = null) {
    Snackbar.make(this, view, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(
            getColor(
                view.context,
                android.R.color.holo_red_dark
            )
        )
        .setTextColor(
            getColor(
                view.context,
                android.R.color.white
            )
        )
        .setAnchorView(anchorView)
        .show()
}

fun Activity.hideSoftKeyboard(view: View) {
    val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as
            InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun getColor(context: Context, resId: Int): Int {
    return ContextCompat.getColor(context, resId)
}

fun getContrastColor(color: Int): Int {
    val y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000
    return if (y >= 128) Color.BLACK else Color.WHITE
}