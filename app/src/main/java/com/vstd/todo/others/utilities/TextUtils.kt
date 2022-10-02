package com.vstd.todo.others.utilities

object TextUtils {

    const val NOT_VALID_TITLE = "Title must be from 1 to 20 chars"
    const val WORKSPACE_NAME_COINCIDENCE = "Workspace name is already used."
    const val PASSED_ALL_VALIDATION = "PASSED_ALL_VALIDATION"

    fun isValidTitle(title: String): Boolean {
        return title.trim().length in 1..20
    }
}