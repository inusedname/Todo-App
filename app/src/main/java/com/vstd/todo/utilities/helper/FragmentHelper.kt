package com.vstd.todo.utilities.helper

import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.vstd.todo.R
import com.vstd.todo.utilities.Sorting

fun Fragment.showSortPopup(itemId: Int, callback: (String) -> Unit) {
    PopupMenu(requireContext(), requireActivity().findViewById(itemId)).apply {
        setForceShowIcon(true)
        setOnMenuItemClickListener { onSortSubmit(it, callback) }
        inflate(R.menu.sort_tasks_popup_menu)
        show()
    }
}

private fun onSortSubmit(menuItem: MenuItem, callback: (String) -> Unit): Boolean {
    val sortOptions = mapOf(
        R.id.due_date_inc to Sorting.DUE_DATE_ASC,
        R.id.due_date_dec to Sorting.DUE_DATE_DESC,
        R.id.created_date_inc to Sorting.CREATE_DATE_ASC,
        R.id.created_date_dec to Sorting.CREATE_DATE_DESC,
        R.id.last_modified_date_inc to Sorting.LAST_MODIFIED_ASC,
        R.id.last_modified_date_dec to Sorting.LAST_MODIFIED_DESC,
    )
    val myOptionId = menuItem.itemId
    callback(sortOptions[myOptionId]!!)
    return true
}