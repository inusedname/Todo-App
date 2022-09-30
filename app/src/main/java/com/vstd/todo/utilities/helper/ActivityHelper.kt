package com.vstd.todo.utilities.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftKeyboard(view: View) {
    val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}