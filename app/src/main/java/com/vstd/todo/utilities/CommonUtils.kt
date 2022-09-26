package com.vstd.todo.utilities

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.snack(view: View, message: String, anchorView: View? = null) {
    Snackbar.make(this, view, message, Snackbar.LENGTH_SHORT)
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

fun Activity.hideSoftKeyboard() {
    val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as
            InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun getColor(context: Context, resId: Int): Int {
    return ContextCompat.getColor(context, resId)
}
