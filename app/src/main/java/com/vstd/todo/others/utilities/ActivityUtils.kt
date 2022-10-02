package com.vstd.todo.others.utilities

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.vstd.todo.others.constants.Constants

fun Activity.hideSoftKeyboard(view: View? = null): Boolean {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        return if (view == null) {
            inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        } else {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    return false
}

fun Activity.log(msg: String) {
    Log.d(Constants.DEBUG_KEY, this.javaClass.simpleName + ":" + msg)
}