package com.vstd.todo.model.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "completed_subtasks") val completedSubtasks: List<String>,
    @ColumnInfo(name = "uncompleted_subtasks") val uncompletedSubtasks: List<String>,
    @ColumnInfo(name = "created_date_time") val createdDateTime: String,
    @ColumnInfo(name = "last_modified_date_time") val lastModifiedDateTime: String,
    @ColumnInfo(name = "due_date_time") val dueDateTime: String,
    @ColumnInfo(name = "tags") val tags: List<String>,
)
