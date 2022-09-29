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

    fun addWorkspace(workspace: Workspace) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWorkspace(workspace)
            var found = false
            for (i in workspaces.indices) {
                if (workspaces[i].workspaceName == workspace.workspaceName) {
                    workspaces[i] = workspace
                    found = true
                    break
                }
            }
            if (!found) workspaces.add(workspace)
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