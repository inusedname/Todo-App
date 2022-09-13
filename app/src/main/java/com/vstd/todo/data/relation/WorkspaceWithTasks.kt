package com.vstd.todo.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.vstd.todo.data.Task
import com.vstd.todo.data.Workspace

data class WorkspaceWithTasks(

    @Embedded
    val workspace: Workspace,

    @Relation(
        parentColumn = "workspaceName",
        entityColumn = "workspaceName"
    )
    val tasks: List<Task>
)
