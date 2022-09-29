package com.vstd.todo.viewmodels

import androidx.lifecycle.*
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.repository.TodoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun MutableList<Workspace>.updateWorkspace(workspace: Workspace): Boolean {
    val index = this.indexOfFirst { it.workspaceName == workspace.workspaceName }
    if (index != -1) {
        this[index] = workspace
    }
    return index != -1
}

class WorkspaceViewModel(private val repo: TodoRepo) : ViewModel() {
    private lateinit var workspaces: MutableList<Workspace>

    private val _workspaceLivedata by lazy {
        MutableLiveData<List<Workspace>>().also {
            loadWorkspaces()
        }
    }

    private fun loadWorkspaces() {
        viewModelScope.launch(Dispatchers.IO) {
            workspaces = repo.getWorkspaces().toMutableList()
            updateLiveData()
        }
    }

    private fun updateLiveData() {
        viewModelScope.launch {
            _workspaceLivedata.value = workspaces
        }
    }

    fun deleteWorkspace(workspace: Workspace) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteWorkspace(workspace)
            workspaces.remove(workspace)
            updateLiveData()
        }
    }

    fun editWorkspace(workspace: Workspace) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateWorkspace(workspace)
            workspaces.updateWorkspace(workspace)
            updateLiveData()
        }
    }

    fun addWorkspace(workspace: Workspace) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWorkspace(workspace)
            val updated = workspaces.updateWorkspace(workspace)
            if (!updated) workspaces.add(workspace)
            updateLiveData()
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