package com.vstd.todo.utilities

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.bottomappbar.BottomAppBar
import com.vstd.todo.MainActivity
import com.vstd.todo.R

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.hideSoftKeyboard() {
    val inputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as
            InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun MainActivity.hideBottomAppBar() {
    this.findViewById<BottomAppBar>(R.id.bottom_app_bar).visibility = View.GONE
}
