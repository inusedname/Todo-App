package com.vstd.todo.utilities.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
            com.vstd.todo.utilities.getColor(
                view.context,
                android.R.color.holo_red_dark
            )
        )
        .setTextColor(
            com.vstd.todo.utilities.getColor(
                view.context,
                android.R.color.white
            )
        )
        .setAnchorView(anchorView)
        .show()
}

fun Activity.hideSoftKeyboard(view: View) {
    val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}