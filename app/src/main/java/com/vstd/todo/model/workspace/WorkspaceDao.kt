package com.vstd.todo.model.workspace

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkspaceDao {

    @Query("SELECT * FROM workspaces_table")
    fun getTasks(): List<Workspace>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workspace: Workspace)

}