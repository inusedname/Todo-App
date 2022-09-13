package com.vstd.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workspace_table")
data class Workspace(

    @PrimaryKey(autoGenerate = false)
    val workspaceName: String,
)
