package com.vstd.todo.utilities

import com.vstd.todo.data.Task

object Sorting {
    const val DUE_DATE_ASC = "DUE_DATE_ASC"
    const val DUE_DATE_DESC = "DUE_DATE_DESC"
    const val CREATE_DATE_ASC = "CREATE_DATE_ASC"
    const val CREATE_DATE_DESC = "CREATE_DATE_DESC"
    const val LAST_MODIFIED_ASC = "LAST_MODIFIED_ASC"
    const val LAST_MODIFIED_DESC = "LAST_MODIFIED_DESC"
}

fun List<Task>.sortedByDueDate(): List<Task> {
    return this.sortedBy { it.dueDateTime }
}

fun List<Task>.sortedByCreatedDate(): List<Task> {
    return this.sortedBy { it.createdDateTime }
}

fun List<Task>.sortedByLastModified(): List<Task> {
    return this.sortedBy { it.lastModifiedDateTime }
}