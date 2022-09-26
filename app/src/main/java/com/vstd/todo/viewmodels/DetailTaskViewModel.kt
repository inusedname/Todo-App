package com.vstd.todo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vstd.todo.data.Subtask
import com.vstd.todo.data.Task
import com.vstd.todo.utilities.TextUtils
import com.vstd.todo.utilities.toLocalDate
import com.vstd.todo.utilities.toLocalTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

const val NOT_EMPTY_TITLE_MSG = "ERROR 😭: Title can't be empty"
const val PASSED_ALL_VALIDATION = "PASSED_ALL_VALIDATION"

class DetailTaskViewModel(val task: Task) : ViewModel() {

    var needToSave = true
    var dueDate: LocalDate? = null
    var dueTime: LocalTime? = null
    private var subtasks: MutableList<Subtask>

    lateinit var taskTitle: String
    lateinit var taskDescription: String

    private val _subtasksLiveData = MutableLiveData<List<Subtask>>()
    val subtasksLiveData = _subtasksLiveData


    init {
        dueDate = task.dueDate.toLocalDate()
        dueTime = task.dueTime.toLocalTime()
        subtasks = task.subtasks.toMutableList()
        _subtasksLiveData.value = subtasks
    }

    private fun cleanTexts() {
        taskTitle.apply { trim() }
        taskDescription.apply { trim() }

        for (i in subtasks.indices)
            subtasks[i].title.apply { trim() }
    }

    fun getValidateStatus(): String {
        cleanTexts()
        return if (!TextUtils.isValidTitle(taskTitle)) {
            NOT_EMPTY_TITLE_MSG
        } else {
            PASSED_ALL_VALIDATION
        }
    }

    fun getModifiedTask(): Task {
        return task.copy(
            title = taskTitle,
            description = taskDescription,
            dueTime = dueTime.toString(),
            dueDate = dueDate.toString(),
            lastModifiedDateTime = LocalDateTime.now().toString(),
            subtasks = subtasks,
        )
    }

    fun deleteSubtask(subtask: Subtask) {
        subtasks.remove(subtask)
        _subtasksLiveData.value = subtasks
    }

    fun makeSubtaskDone(subtask: Subtask) {
        val i = subtasks.indexOf(subtask)
        subtasks[i] = subtasks[i].copy(isDone = !subtasks[i].isDone)
        _subtasksLiveData.value = subtasks
    }

    fun addSubtask(subtask: Subtask) {
        subtasks.add(subtask)
        _subtasksLiveData.value = subtasks
    }
}

@Suppress("UNCHECKED_CAST")
class DetailTaskViewModelFactory(val task: Task) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailTaskViewModel::class.java)) {
            return DetailTaskViewModel(task) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}