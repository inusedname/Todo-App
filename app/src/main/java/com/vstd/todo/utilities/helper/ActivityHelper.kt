package com.vstd.todo.utilities.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftKeyboard(view: View? = null): Boolean {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    if (true) {
        return if (view == null) {
            inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        } else {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    return false
}