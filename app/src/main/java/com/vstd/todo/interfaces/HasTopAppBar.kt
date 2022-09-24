package com.vstd.todo.interfaces

import android.view.MenuItem
import com.google.android.material.appbar.MaterialToolbar

interface HasTopAppBar {
    fun onTopAppBarMenuClick(item: MenuItem): Boolean
    fun onTopAppBarNavigationClick()
    fun setUpTopAppBarAppearance(topAppBar: MaterialToolbar)
}