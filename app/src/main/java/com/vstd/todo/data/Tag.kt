package com.vstd.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_table")
data class Tag(

    @PrimaryKey(autoGenerate = false)
    val tagName: String,

    val color: Int
)
