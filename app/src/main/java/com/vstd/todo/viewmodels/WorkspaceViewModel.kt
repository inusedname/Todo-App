package com.vstd.todo.viewmodels

import androidx.lifecycle.*
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.repository.TodoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkspaceViewModel(private val repo: TodoRepo) : ViewModel() {
    private lateinit var workspaces: MutableList<Workspace>

    private val _workspaceLivedata by lazy {
        MutableLiveData<List<Workspace>>().also {
            loadWorkspace()
        }
    }

    private fun loadWorkspace() {
        viewModelScope.launch(Dispatchers.IO) {
            workspaces = repo.getWorkspaces().toMutableList()
            updateWorkspace()
        }
    }

    private fun updateWorkspace() {
        viewModelScope.launch {
            _workspaceLivedata.value = workspaces
        }
    }

    fun deleteWorkspace(workspace: Workspace) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteWorkspace(workspace)
            workspaces.remove(workspace)
            updateWorkspace()
        }
    }

    fun addWorkspace(workspace: Workspace) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWorkspace(workspace)
            workspaces.add(workspace)
            updateWorkspace()
        }
    }

    val workspace: LiveData<List<Workspace>> = _workspaceLivedata
}

@Suppress("UNCHECKED_CAST")
class WorkspaceViewModelFactory(private val repo: TodoRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkspaceViewModel::class.java)) {
            return WorkspaceViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}