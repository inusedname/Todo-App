package com.vstd.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime

@Entity(tableName = "task_table")
data class Task(

    @PrimaryKey(autoGenerate = true)
    val taskId: Long = 0,

    val title: String,

    val description: String = "",

    val subtasks: List<Subtask> = emptyList(),

    val createdDateTime: String = LocalDateTime.now().toString(),

    val lastModifiedDateTime: String = LocalDateTime.now().toString(),

    val dueDate: String = "null",

    val dueTime: String = "null",

    val workspaceName: String,

    val isArchived: Boolean = false,

    val isDone: Boolean = false,
) : Serializable
