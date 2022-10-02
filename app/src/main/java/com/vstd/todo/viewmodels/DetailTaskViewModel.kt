package com.vstd.todo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vstd.todo.data.Subtask
import com.vstd.todo.data.Task
import com.vstd.todo.utilities.TextUtils
import java.time.LocalDateTime

class DetailTaskViewModel(private val _task: Task) : ViewModel() {

    val oldTask = _task
    var dueDate = _task.dueDate
    var dueTime = _task.dueTime
    private var subtasks = _task.subtasks.toMutableList()
    var workspaceName = _task.workspaceName
    var taskTitle = _task.title
    var taskDescription = _task.description
    private var isDone = _task.isDone
    private var isArchived = false

    private val _subtasksLiveData = MutableLiveData<List<Subtask>>()
    val subtasksLiveData = _subtasksLiveData

    init {
        updateSubtaskLiveData()
    }

    fun needToSave(): Boolean {
        var flag = taskTitle == _task.title &&
                taskDescription == _task.description &&
                dueDate == _task.dueDate &&
                dueTime == _task.dueTime &&
                workspaceName == _task.workspaceName &&
                isDone == _task.isDone &&
                subtasks.size == _task.subtasks.size
        // log("${taskTitle == _task.title} ${taskDescription == _task.description} ${dueDate == _task.dueDate} ${dueTime == _task.dueTime} ${workspaceName == _task.workspaceName} ${isDone == _task.isDone} ${subtasks.size == _task.subtasks.size}")
        if (flag)
            for (i in _task.subtasks.indices)
                if (subtasks[i] != _task.subtasks[i])
                    flag = false
        // log("flag = $flag")
        return !flag
    }

    private fun addADummySubtask() {
        subtasks.add(Subtask(""))
    }

    fun deleteSubtask(i: Int) {
        subtasks.removeAt(i)
        updateSubtaskLiveData()
    }

    fun renameSubtask(i: Int, newName: String) {
        if (i < subtasks.size && subtasks[i].title != newName) {
            subtasks[i] = subtasks[i].copy(title = newName)
            updateSubtaskLiveData()
        }
    }

    fun updateSubtaskDone(i: Int) {
        subtasks[i] = subtasks[i].copy(isDone = !subtasks[i].isDone)
        updateSubtaskLiveData()
    }

    fun updateTaskDone() {
        isDone = !isDone
    }

    fun updateTaskArchived() {
        isArchived = true
    }

    private fun cleanBeforeValidate() {
        taskTitle.apply { trim() }
        taskDescription.apply { trim() }

        for (i in subtasks.size - 1 downTo 0) {
            subtasks[i].title.apply { trim() }
            if (subtasks[i].title.isEmpty())
                subtasks.removeAt(i)
        }
    }

    fun getValidateStatus(): String {
        cleanBeforeValidate()
        return if (!TextUtils.isValidTitle(taskTitle)) {
            TextUtils.NOT_VALID_TITLE
        } else {
            TextUtils.PASSED_ALL_VALIDATION
        }
    }

    fun getNewTask() =
        _task.copy(
            title = taskTitle,
            description = taskDescription,
            dueTime = dueTime,
            dueDate = dueDate,
            lastModifiedDateTime = LocalDateTime.now().toString(),
            subtasks = subtasks,
            isDone = isDone,
            isArchived = isArchived,
            workspaceName = workspaceName
        )

    private fun updateSubtaskLiveData() {
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