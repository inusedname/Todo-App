package com.vstd.todo.interfaces

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

interface HasFab {
    fun onFabClicked(fab: View)
    fun setUpFabAppearance(fab: FloatingActionButton)
}