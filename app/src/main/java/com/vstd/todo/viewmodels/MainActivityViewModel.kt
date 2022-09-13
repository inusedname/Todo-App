package com.vstd.todo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vstd.todo.data.repository.TodoRepo

class MainActivityViewModel(private val repo: TodoRepo) : ViewModel() {
    fun getWorkspaces() = repo.getWorkspaces()
}

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory(private val repo: TodoRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}