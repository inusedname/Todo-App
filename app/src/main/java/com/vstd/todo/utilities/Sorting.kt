package com.vstd.todo.utilities

import com.vstd.todo.data.Task

fun sortedTasksByDueDate(list: List<Task>): List<Task> {
    return list.sortedBy { it.dueDateTime }
}

fun sortedTasksByDueDateDescending(list: List<Task>): List<Task> {
    return list.sortedByDescending { it.dueDateTime }
}

fun sortedTasksByCreatedDate(list: List<Task>): List<Task> {
    return list.sortedBy { it.createdDateTime }
}

fun sortedTasksByCreatedDateDescending(list: List<Task>): List<Task> {
    return list.sortedByDescending { it.createdDateTime }
}

fun sortedTasksByLastModified(list: List<Task>): List<Task> {
    return list.sortedBy { it.lastModifiedDateTime }
}

fun sortedTasksByLastModifiedDescending(list: List<Task>): List<Task> {
    return list.sortedByDescending { it.lastModifiedDateTime }
}