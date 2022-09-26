package com.vstd.todo.viewmodels

import androidx.lifecycle.*
import com.vstd.todo.data.Tag
import com.vstd.todo.data.repository.TodoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagViewModel(private val taskId: Long, private val repo: TodoRepo) : ViewModel() {

    private val tags = mutableListOf<Tag>()

    private val _tagsLiveData by lazy {
        MutableLiveData<List<Tag>>()
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val tagsWithTask = repo.getTaskWithTags(taskId)?.tags
            if (tagsWithTask != null) {
                tags.addAll(tagsWithTask)
            }
            updateTagsLiveData()
        }
    }

    private fun updateTagsLiveData() {
        viewModelScope.launch {
            _tagsLiveData.value = tags
        }

    }

    val tagsLiveData: LiveData<List<Tag>> = _tagsLiveData

    fun addTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertTagToTask(taskId, tag)
            tags.add(tag)
            updateTagsLiveData()
        }
    }

    fun removeTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeTagFromTask(taskId, tag)
            tags.remove(tag)
            updateTagsLiveData()
        }
    }
}

@Suppress("UNCHECKED_CAST")
class TagViewModelFactory(private val taskId: Long, private val repo: TodoRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
            return TagViewModel(taskId, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}