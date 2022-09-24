package com.vstd.todo.data.repository

import com.vstd.todo.data.Tag
import com.vstd.todo.data.Task
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.database.TodoDao
import com.vstd.todo.data.relation.TaskWithTags
import com.vstd.todo.data.relation.WorkspaceWithTasks

class TodoRepo(private val todoDao: TodoDao) {

    fun getWorkspaces(): List<Workspace> {
        return todoDao.getWorkspaces()
    }

    suspend fun getWorkspaceWithTask(workspaceName: String): WorkspaceWithTasks {
        return todoDao.getWorkspaceWithTasks(workspaceName)
    }

    suspend fun insertTask(task: Task): Long {
        return todoDao.insertTask(task)
    }

    suspend fun insertWorkspace(workspace: Workspace) {
        todoDao.insertWorkspace(workspace)
    }

    suspend fun insertTagToTask(task: Task, tag: Tag) {
        todoDao.insertTagToTask(task, tag)
    }

    suspend fun removeTagFromTask(task: Task, tag: Tag) {
        todoDao.removeTagFromTask(task, tag)
    }

    suspend fun deleteTask(task: Task) {
        todoDao.removeTagsWithTask(task)
        todoDao.deleteTask(task)
    }

    suspend fun deleteWorkspace(workspace: Workspace) {
        todoDao.removeTasksWithWorkspace(workspace)
        todoDao.deleteWorkspace(workspace)
    }

    suspend fun deleteTag(tag: Tag) {
        todoDao.removeTagFromTasks(tag)
        todoDao.deleteTag(tag)
    }

    suspend fun updateTag(tag: Tag) {
        todoDao.updateTag(tag)
    }

    suspend fun updateWorkspace(workspace: Workspace) {
        todoDao.updateWorkspace(workspace)
    }

    suspend fun updateTask(task: Task) {
        todoDao.updateTask(task)
    }

    suspend fun getTaskWithTags(taskId: Long): TaskWithTags? {
        return todoDao.getTaskWithTags(taskId)
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