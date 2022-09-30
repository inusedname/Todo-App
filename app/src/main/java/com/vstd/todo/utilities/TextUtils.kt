package com.vstd.todo.utilities

object TextUtils {

    const val NOT_VALID_TITLE = "Title must be from 1 to 50 chars"
    const val PASSED_ALL_VALIDATION = "PASSED_ALL_VALIDATION"

    fun isValidTitle(title: String): Boolean {
        return title.trim().length in 1..50
    }
}