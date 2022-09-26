package com.vstd.todo.data

import java.io.Serializable

data class Subtask(
    var title: String,
    var isDone: Boolean = false
) : Serializable