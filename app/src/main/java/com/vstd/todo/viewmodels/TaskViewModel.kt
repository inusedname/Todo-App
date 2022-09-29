package com.vstd.todo.viewmodels

import androidx.lifecycle.*
import com.vstd.todo.data.Task
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.utilities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TaskViewModel(private val repo: TodoRepo) : ViewModel() {

    private lateinit var tasks: MutableList<Task> private var workspaceName = Constants.DEFAULT_WORKSPACE_NAME
    private val _workspaceNameLiveData = MutableLiveData(workspaceName)

    init {
        dummyData()
    }

    private val _taskLiveData by lazy {
        MutableLiveData<List<Task>>().also {
            changeWorkspace(workspaceName)
        }
    }

    private fun dummyData() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWorkspace(Workspace(workspaceName, Constants.COLORS["red"]!!))
        }
    }

    fun changeWorkspace(workspaceName: String = this.workspaceName) {
        this.workspaceName = workspaceName
        _workspaceNameLiveData.value = workspaceName

        viewModelScope.launch(Dispatchers.IO) {
            tasks = repo.getWorkspaceWithTask(workspaceName).tasks
                .filter { !it.isArchived }.toMutableList()
            updateTaskLiveData()
        }
    }

    fun fetchArchived() {
        viewModelScope.launch(Dispatchers.IO) {
            tasks = repo.getAllArchivedTasks().toMutableList()
            updateTaskLiveData()
        }
    }

    private fun updateTaskLiveData() {
        viewModelScope.launch {
            _taskLiveData.value = tasks
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteTask(task)
            tasks.remove(task)
            updateTaskLiveData()
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val later = tasks.toList()
            tasks.clear()
            updateTaskLiveData()
            later.forEach { repo.deleteTask(it) }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = repo.insertTask(task)
            tasks.add(task.copy(taskId = newId))
            updateTaskLiveData()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateTask(task)
            if (needToUpdateTasks(task)) {
                for (i in tasks.indices) {
                    if (tasks[i].taskId == task.taskId) {
                        tasks[i] = task
                        break
                    }
                }
                updateTaskLiveData()
            }
        }
    }

    private fun needToUpdateTasks(task: Task): Boolean {
        val willBeRemoved = !(
                task.workspaceName == workspaceName &&
                        !task.isArchived
                )
        if (willBeRemoved) {
            tasks.remove(task)
            tasks.forEach {
                if (it.taskId == task.taskId) {
                    tasks.remove(it)
                    return@forEach
                }
            }
            updateTaskLiveData()
        }
        return !willBeRemoved
    }

    val taskLiveData: LiveData<List<Task>> = _taskLiveData
    val workspaceNameLiveData: LiveData<String> = _workspaceNameLiveData

    fun sortTasks(sortType: String) {
        when (sortType) {
            Sorting.DUE_DATE_ASC -> tasks = tasks.sortedByDueDate().toMutableList()
            Sorting.DUE_DATE_DESC -> tasks = tasks.sortedByDueDate().reversed().toMutableList()
            Sorting.CREATE_DATE_ASC -> tasks = tasks.sortedByCreatedDate().toMutableList()
            Sorting.CREATE_DATE_DESC -> tasks =
                tasks.sortedByCreatedDate().reversed().toMutableList()
            Sorting.LAST_MODIFIED_ASC -> tasks = tasks.sortedByLastModified().toMutableList()
            Sorting.LAST_MODIFIED_DESC -> tasks =
                tasks.sortedByLastModified().reversed().toMutableList()
        }
        updateTaskLiveData()
    }
}

@Suppress("UNCHECKED_CAST")
class TaskViewModelFactory(private val repo: TodoRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}