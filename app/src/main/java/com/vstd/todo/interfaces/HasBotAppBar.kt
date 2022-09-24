package com.vstd.todo.interfaces

import android.view.MenuItem
import com.google.android.material.bottomappbar.BottomAppBar

interface HasBotAppBar {
    fun onBotAppBarNavigationClick()
    fun onBotAppBarMenuClick(item: MenuItem): Boolean
    fun setUpBotAppBarAppearance(botAppBar: BottomAppBar)
}