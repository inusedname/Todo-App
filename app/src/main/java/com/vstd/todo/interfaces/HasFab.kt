package com.vstd.todo.interfaces

import com.google.android.material.floatingactionbutton.FloatingActionButton

interface HasFab {
    fun onFabClicked()
    fun setUpFabAppearance(fab: FloatingActionButton)
}