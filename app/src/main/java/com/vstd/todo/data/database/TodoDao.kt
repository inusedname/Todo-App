package com.vstd.todo.data.database

import androidx.room.*
import com.vstd.todo.data.Tag
import com.vstd.todo.data.Task
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.crossReference.TaskTagCrossRef
import com.vstd.todo.data.relation.TagWithTasks
import com.vstd.todo.data.relation.TaskWithTags
import com.vstd.todo.data.relation.WorkspaceWithTasks

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWorkspace(workspace: Workspace)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun bindTaskAndTag(taskTagCrossRef: TaskTagCrossRef)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM workspace_table")
    fun getWorkspaces(): List<Workspace>

    @Transaction
    @Query("SELECT * FROM workspace_table WHERE workspaceName = :workspaceName")
    suspend fun getWorkspaceWithTasks(workspaceName: String): WorkspaceWithTasks

    @Transaction
    @Query("SELECT * FROM task_table WHERE taskId = :taskId")
    suspend fun getTaskWithTags(taskId: Int): TaskWithTags

    @Transaction
    @Query("SELECT * FROM tag_table WHERE tagName = :tagName")
    suspend fun getTagWithTasks(tagName: String): TagWithTasks

}