package com.vstd.todo.utilities

class TextUtils {
    companion object {
        fun isValidTitle(title: String): Boolean {
            return title.trim().length in 1..50
        }

        fun isValidMultilineText(text: String): Boolean {
            return text.trim().length in 1..1000
        }
    }
}