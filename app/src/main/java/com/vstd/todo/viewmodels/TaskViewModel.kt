package com.vstd.todo.viewmodels

import androidx.lifecycle.*
import com.vstd.todo.data.Task
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.utilities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun MutableList<Task>.updateById(task: Task) {
    val index = this.indexOfFirst { it.taskId == task.taskId }
    if (index != -1)
        this[index] = task
}

fun MutableList<Task>.removeById(taskId: Task) {
    val index = this.indexOfFirst { it.taskId == taskId.taskId }
    if (index != -1)
        this.removeAt(index)
}

class TaskViewModel(private val repo: TodoRepo) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWorkspace(Workspace(workspaceName, Constants.COLORS["red"]!!))
        }
    }

    var archiveMode = false
        private set
    private lateinit var tasks: MutableList<Task>
    private var workspaceName = Constants.DEFAULT_WORKSPACE_NAME
    private val _workspaceNameLiveData = MutableLiveData(workspaceName)
    private val _taskLiveData by lazy {
        MutableLiveData<List<Task>>().also {
            changeWorkspace()
        }
    }


    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = repo.insertTask(task)
            tasks.add(task.copy(taskId = newId))
            updateTaskLiveData()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteTask(task)
            tasks.remove(task)
            updateTaskLiveData()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateTask(task)
            tasks.updateById(task)
            cleanUpTasks()
            updateTaskLiveData()
        }
    }

    fun changeWorkspace(workspaceName: String = this.workspaceName) {
        archiveMode = false
        this.workspaceName = workspaceName
        _workspaceNameLiveData.value = workspaceName

        viewModelScope.launch(Dispatchers.IO) {
            tasks = repo.getTask(workspaceName).toMutableList()
            updateTaskLiveData()
        }
    }

    fun fetchArchived() {
        viewModelScope.launch(Dispatchers.IO) {
            archiveMode = true
            tasks = repo.getAllArchivedTasks().toMutableList()
            updateTaskLiveData()
        }
    }

    fun clearArchived() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.clearArchived()
            tasks.clear()
            updateTaskLiveData()
        }
    }

    private fun cleanUpTasks() {
        val disqualified = { task: Task ->
            !(
                    task.workspaceName == workspaceName &&
                            task.isArchived == archiveMode
                    )
        }
        tasks.forEach { if (disqualified(it)) tasks.removeById(it) }
    }

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


    private fun updateTaskLiveData() {
        viewModelScope.launch {
            _taskLiveData.value = tasks
        }
    }

    val taskLiveData: LiveData<List<Task>> = _taskLiveData
    val workspaceNameLiveData: LiveData<String> = _workspaceNameLiveData
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