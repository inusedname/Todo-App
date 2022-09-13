package com.vstd.todo.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vstd.todo.data.Tag
import com.vstd.todo.data.Task
import com.vstd.todo.data.crossReference.TaskTagCrossRef

data class TagWithTasks(
    @Embedded
    val tag: Tag,

    @Relation(
        parentColumn = "tagName",
        entityColumn = "taskId",
        associateBy = Junction(TaskTagCrossRef::class)
    )
    val tasks: List<Task>
)
