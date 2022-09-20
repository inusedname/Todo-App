package com.vstd.todo.data.repository

import com.vstd.todo.data.Task
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.database.TodoDao
import com.vstd.todo.data.relation.WorkspaceWithTasks

class TodoRepo(private val todoDao: TodoDao) {

    fun getWorkspaces(): List<Workspace> {
        return todoDao.getWorkspaces()
    }

    suspend fun getWorkspaceWithTask(workspaceName: String): WorkspaceWithTasks {
        return todoDao.getWorkspaceWithTasks(workspaceName)
    }

    suspend fun insertTask(task: Task) {
        todoDao.insertTask(task)
    }

    suspend fun deleteTask(task: Task) {
        todoDao.deleteTask(task)
    }

    suspend fun insertWorkspace(workspace: Workspace) {
        todoDao.insertWorkspace(workspace)
    }

    suspend fun insertWorkspace(workspaceName: String, workspaceColor: Int) {
        todoDao.insertWorkspace(Workspace(workspaceName, workspaceColor))
    }

    fun deleteWorkspace(workspace: Workspace) {
        // TODO: Not implemented yet
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