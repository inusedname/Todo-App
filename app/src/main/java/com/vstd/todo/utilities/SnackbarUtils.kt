package com.vstd.todo.utilities

import android.app.Activity
import android.view.View
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

fun Activity.snackNotAvailable(view: View, anchorView: View? = null) {
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

fun Activity.snackArchived(view: View, anchorView: View? = null) {
    this.snack(view, getString(R.string.task_archived_msg), anchorView)
}