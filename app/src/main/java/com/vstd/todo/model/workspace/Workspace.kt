package com.vstd.todo.model.workspace

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workspaces_table")
data class Workspace(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "tasks") val tasks: List<Int>,
    @ColumnInfo(name = "starred_tasks") val starredTasks: List<Int>,
)
