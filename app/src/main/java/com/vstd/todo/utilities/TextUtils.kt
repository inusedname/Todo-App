package com.vstd.todo.utilities

class TextUtils {
    companion object {
        fun isValidTitle(title: String): Boolean {
            return title.trim().length in 1..50
        }
    }
}