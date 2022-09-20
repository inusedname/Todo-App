package com.vstd.todo.viewmodels

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.vstd.todo.data.Task
import com.vstd.todo.data.repository.TodoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class TaskViewModel(private val repo: TodoRepo) : ViewModel() {

    private lateinit var tasks: MutableList<Task>
    private var _workspace = "default"

    init {
        dummyData()
    }

    private val _taskLiveData by lazy {
        MutableLiveData<List<Task>>().also {
            changeWorkspace(_workspace)
        }
    }

    private fun dummyData() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWorkspace("default", Color.BLACK)
            repo.insertTask(
                Task(
                    title = "Đi mua sữa",
                    description = "Tại công viên thống nhất\n19:30",
                    workspaceName = "default"
                )
            )
        }
    }

    fun changeWorkspace(workspaceName: String) {
        _workspace = workspaceName
        viewModelScope.launch(Dispatchers.IO) {
            tasks = repo.getWorkspaceWithTask(workspaceName).tasks.toMutableList()
            updateTasks()
        }
    }

    private fun updateTasks() {
        viewModelScope.launch {
            _taskLiveData.value = tasks
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteTask(task)
            tasks.remove(task)
            updateTasks()
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertTask(task)
            tasks.add(task)
            updateTasks()
        }
    }

    val taskLiveData: LiveData<List<Task>> = _taskLiveData
    val workspaceName: String = _workspace
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