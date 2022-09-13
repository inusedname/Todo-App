package com.vstd.todo.data.crossReference

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["tagName", "taskId"])
data class TaskTagCrossRef(
    val tagName: String,

    @ColumnInfo(index = true)
    val taskId: Int,
)