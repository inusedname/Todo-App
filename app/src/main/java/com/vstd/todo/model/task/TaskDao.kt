package com.vstd.todo.model.task

import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks_table")
    fun getTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Delete
    suspend fun remove(task: Task)
}