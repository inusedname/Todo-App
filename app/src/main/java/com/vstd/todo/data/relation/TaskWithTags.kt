package com.vstd.todo.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.vstd.todo.data.Tag
import com.vstd.todo.data.Task
import com.vstd.todo.data.crossReference.TaskTagCrossRef

data class TaskWithTags(
    @Embedded
    val task: Task,

    @Relation(
        parentColumn = "taskId",
        entityColumn = "tagName",
        associateBy = Junction(TaskTagCrossRef::class)
    )
    val tags: List<Tag>
)