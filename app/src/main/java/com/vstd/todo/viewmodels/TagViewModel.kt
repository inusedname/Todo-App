package com.vstd.todo.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.vstd.todo.data.Tag
import com.vstd.todo.data.repository.TodoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagViewModel(private val repo: TodoRepo) : ViewModel() {

    private val tags = mutableListOf<Tag>()

    private val _tagsLiveData by lazy {
        MutableLiveData<List<Tag>>()
    }

    private fun updateTagsLiveData() {
        viewModelScope.launch {
            _tagsLiveData.value = tags
        }

    }

    fun getTagsWithTask(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tags.clear()
            val tagsWithTask = repo.getTaskWithTags(taskId)?.tags
            if (tagsWithTask != null) {
                tags.addAll(tagsWithTask)
            }
            updateTagsLiveData()
        }
    }

    val tagsLiveData: LiveData<List<Tag>> = _tagsLiveData
}

@Suppress("UNCHECKED_CAST")
@RequiresApi(Build.VERSION_CODES.O)
class TagViewModelFactory(private val repo: TodoRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
            return TagViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}