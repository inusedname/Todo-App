package com.vstd.todo.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vstd.todo.utilities.DateTimeUtils
import java.io.Serializable

@Entity(tableName = "task_table")
@RequiresApi(Build.VERSION_CODES.O)
data class Task(

    @PrimaryKey(autoGenerate = true)
    val taskId: Long = 0,

    val title: String,

    val description: String = "",

    val doneSubtasks: List<String> = listOf(),

    val notDoneSubtasks: List<String> = listOf(),

    val createdDateTime: String = DateTimeUtils.now().toString(),

    val lastModifiedDateTime: String = DateTimeUtils.now().toString(),

    val dueDateTime: String? = null,

    val workspaceName: String,

    val isArchived: Boolean = false,

    val isDone: Boolean = false,
) : Serializable
