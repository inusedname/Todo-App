package com.vstd.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(

    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,

    val title: String,

    val description: String = "",

    val completedSubtasks: List<String> = listOf(),

    val uncompletedSubtasks: List<String> = listOf(),

    val createdDateTime: String,

    val lastModifiedDateTime: String,

    val dueDateTime: String = "",

    val workspaceName: String,


    )
