package com.vstd.todo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vstd.todo.data.Subtask
import com.vstd.todo.data.Task
import com.vstd.todo.utilities.TextUtils
import java.time.LocalDateTime

const val NOT_EMPTY_TITLE_MSG = "ERROR 😭: Title can't be empty"
const val PASSED_ALL_VALIDATION = "PASSED_ALL_VALIDATION"

class DetailTaskViewModel(private val _task: Task) : ViewModel() {

    var dueDate = "null"
    var dueTime = "null"
    private var subtasks: MutableList<Subtask>

    lateinit var workspaceName: String
    private var isDone: Boolean
    lateinit var taskTitle: String
    lateinit var taskDescription: String

    private val _subtasksLiveData = MutableLiveData<List<Subtask>>()
    val subtasksLiveData = _subtasksLiveData

    init {
        dueDate = _task.dueDate
        dueTime = _task.dueTime
        subtasks = _task.subtasks.toMutableList()
        isDone = _task.isDone
        updateLiveData()
    }

    fun needToSave(): Boolean {
        var flag = taskTitle == _task.title &&
                taskDescription == _task.description &&
                dueDate == _task.dueDate &&
                dueTime == _task.dueTime &&
                workspaceName == _task.workspaceName &&
                isDone == _task.isDone &&
                subtasks.size - 1 == _task.subtasks.size
        if (flag)
            for (i in _task.subtasks.indices)
                if (subtasks[i] != _task.subtasks[i])
                    flag = false
        return !flag
    }

    private fun addADummySubtask() {
        subtasks.add(Subtask(""))
    }

    fun deleteSubtask(i: Int) {
        subtasks.removeAt(i)
        updateLiveData()
    }

    fun renameSubtask(i: Int, newName: String) {
        if (subtasks[i].title != newName) {
            subtasks[i] = subtasks[i].copy(title = newName)
            updateLiveData()
        }
    }

    fun updateSubtaskDone(i: Int) {
        subtasks[i] = subtasks[i].copy(isDone = !subtasks[i].isDone)
        updateLiveData()
    }

    private fun cleanBeforeValidate() {
        taskTitle.apply { trim() }
        taskDescription.apply { trim() }

        for (i in subtasks.indices) {
            subtasks[i].title.apply { trim() }
            if (subtasks[i].title.isEmpty())
                subtasks.removeAt(i)
        }
    }

    fun getValidateStatus(): String {
        cleanBeforeValidate()
        return if (!TextUtils.isValidTitle(taskTitle)) {
            NOT_EMPTY_TITLE_MSG
        } else {
            PASSED_ALL_VALIDATION
        }
    }

    val task =
        _task.copy(
            title = taskTitle,
            description = taskDescription,
            dueTime = dueTime,
            dueDate = dueDate,
            lastModifiedDateTime = LocalDateTime.now().toString(),
            subtasks = subtasks,
            isDone = isDone,
            workspaceName = workspaceName
        )

    private fun updateLiveData() {
        if (subtasks.isEmpty() || subtasks.last().title.isNotEmpty())
            addADummySubtask()
        _subtasksLiveData.value = subtasks
    }
}

@Suppress("UNCHECKED_CAST")
class DetailTaskViewModelFactory(val task: Task) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailTaskViewModel::class.java)) {
            return DetailTaskViewModel(task) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}