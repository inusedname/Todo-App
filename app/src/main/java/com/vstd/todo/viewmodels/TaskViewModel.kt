package com.vstd.todo.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.vstd.todo.data.Task
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.utilities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "TVMLog"

@RequiresApi(Build.VERSION_CODES.O)
class TaskViewModel(private val repo: TodoRepo) : ViewModel() {

    private lateinit var tasks: MutableList<Task>
    private var workspaceName = "default"
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
            repo.insertWorkspace(Workspace("default", Constants.COLORS["red"]!!))
            addTask(
                Task(
                    title = "Đi mua sữa",
                    description = "Tại công viên thống nhất\n19:30",
                    workspaceName = "default"
                )
            )
        }
    }

    fun changeWorkspace(workspaceName: String) {
        this.workspaceName = workspaceName
        _workspaceNameLiveData.value = workspaceName

        viewModelScope.launch(Dispatchers.IO) {
            tasks = repo.getWorkspaceWithTask(workspaceName).tasks.toMutableList()
            Log.d(TAG, "changeWorkspace: $tasks")
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
            Log.d(TAG, "updateTask: ${task.taskId}")
            for (i in 0 until tasks.size) {
                if (tasks[i].taskId == task.taskId) {
                    tasks[i] = task
                    break
                }
            }
            updateTaskLiveData()
        }
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
@RequiresApi(Build.VERSION_CODES.O)
class TaskViewModelFactory(private val repo: TodoRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}