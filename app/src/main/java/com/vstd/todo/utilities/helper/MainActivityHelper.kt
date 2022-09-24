package com.vstd.todo.utilities.helper

import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vstd.todo.MainActivity
import com.vstd.todo.R

fun MainActivity.hideAllExceptContainer() {
    hideBottomAppBar()
    hideTopAppBar()
}

fun MainActivity.showAll() {
    showBottomAppBar()
    showTopAppBar()
}

fun MainActivity.hideBottomAppBar() {
    this.findViewById<BottomAppBar>(R.id.bottom_app_bar).visibility = View.GONE
    this.findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE
}

fun MainActivity.hideTopAppBar() {
    this.findViewById<MaterialToolbar>(R.id.topAppBar).visibility = View.GONE
}

fun MainActivity.showBottomAppBar() {
    this.findViewById<BottomAppBar>(R.id.bottom_app_bar).visibility = View.VISIBLE
    this.findViewById<FloatingActionButton>(R.id.fab).visibility = View.VISIBLE
}

fun MainActivity.showTopAppBar() {
    this.findViewById<MaterialToolbar>(R.id.topAppBar).visibility = View.VISIBLE
}

fun MainActivity.getTopAppBar() : MaterialToolbar {
    return this.findViewById<MaterialToolbar>(R.id.topAppBar)
}