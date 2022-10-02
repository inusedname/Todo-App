package com.vstd.todo.others.helper

import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.vstd.todo.R

fun Fragment.showSortPopup(itemId: Int, callback: (String) -> Unit) {
    PopupMenu(requireContext(), requireActivity().findViewById(itemId)).apply {
        setForceShowIcon(true)
        setOnMenuItemClickListener { onSortTaskSubmit(it, callback) }
        inflate(R.menu.sort_tasks_popup_menu)
        show()
    }
}

private fun onSortTaskSubmit(menuItem: MenuItem, callback: (String) -> Unit): Boolean {
    val sortOptions = mapOf(
        R.id.due_date_inc to TaskHelper.DUE_DATE_ASC,
        R.id.due_date_dec to TaskHelper.DUE_DATE_DESC,
        R.id.created_date_inc to TaskHelper.CREATE_DATE_ASC,
        R.id.created_date_dec to TaskHelper.CREATE_DATE_DESC,
        R.id.last_modified_date_inc to TaskHelper.LAST_MODIFIED_ASC,
        R.id.last_modified_date_dec to TaskHelper.LAST_MODIFIED_DESC,
    )
    val myOptionId = menuItem.itemId
    callback(sortOptions[myOptionId]!!)
    return true
}