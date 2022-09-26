package com.vstd.todo.utilities.helper

import com.google.android.material.appbar.MaterialToolbar
import com.vstd.todo.MainActivity
import com.vstd.todo.R

fun MainActivity.getTopAppBar(): MaterialToolbar {
    return this.findViewById(R.id.topAppBar)
}