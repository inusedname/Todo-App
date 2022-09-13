package com.vstd.todo.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.vstd.todo.data.Task
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.database.TodoDao
import com.vstd.todo.data.relation.WorkspaceWithTasks
import com.vstd.todo.utilities.DateTimeUtils

class TodoRepo(private val todoDao: TodoDao) {

    fun getWorkspaces(): List<Workspace> = todoDao.getWorkspaces()

    suspend fun getWorkspaceWithTask(workspaceName: String): WorkspaceWithTasks {
        return todoDao.getWorkspaceWithTasks(workspaceName)
    }

    suspend fun insertTask(task: Task) {
        todoDao.insertTask(task)
    }

    suspend fun deleteTask(task: Task) {
        todoDao.deleteTask(task)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertTask(title: String, description: String, workspaceName: String) {
        todoDao.insertTask(
            Task(
                title = title,
                description = description,
                createdDateTime = DateTimeUtils.now().toString(),
                lastModifiedDateTime = DateTimeUtils.now().toString(),
                workspaceName = workspaceName
            )
        )
    }

    suspend fun insertWorkspace(workspaceName: String) {
        todoDao.insertWorkspace(Workspace(workspaceName))
    }

    companion object {
        @Volatile
        private var INSTANCE: TodoRepo? = null

        fun getInstance(todoDao: TodoDao): TodoRepo {

            // Just only one thread can execute code block at the same time.
            // Prevents more than one threads create database simultaneously.
            synchronized(this) {
                return INSTANCE ?: TodoRepo(todoDao)
                    .also { INSTANCE = it }
            }
        }
    }
}