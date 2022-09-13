package com.vstd.todo.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.vstd.todo.data.Task
import com.vstd.todo.data.repository.TodoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class AllTasksViewModel(private val repo: TodoRepo) : ViewModel() {

    private lateinit var tasks: MutableList<Task>

    private val _taskLiveData by lazy {
        MutableLiveData<List<Task>>().also {
            loadTasks(it)
        }
    }

    private fun loadTasks(tasksMutableLiveData: MutableLiveData<List<Task>>) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWorkspace("default")
            repo.insertTask(
                title = "Đi mua sữa",
                description = "Tại công viên thống nhất\n19:30",
                workspaceName = "default"
            )

            tasks = repo.getWorkspaceWithTask("default").tasks.toMutableList()
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

    val taskLiveData: LiveData<List<Task>> = _taskLiveData
}

@Suppress("UNCHECKED_CAST")
@RequiresApi(Build.VERSION_CODES.O)
class AllTasksViewModelFactory(private val repo: TodoRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllTasksViewModel::class.java)) {
            return AllTasksViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}