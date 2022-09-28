package com.vstd.todo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vstd.todo.data.Subtask
import com.vstd.todo.data.Task
import com.vstd.todo.utilities.TextUtils
import com.vstd.todo.utilities.log
import com.vstd.todo.utilities.toLocalDate
import com.vstd.todo.utilities.toLocalTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

const val NOT_EMPTY_TITLE_MSG = "ERROR 😭: Title can't be empty"
const val PASSED_ALL_VALIDATION = "PASSED_ALL_VALIDATION"

class DetailTaskViewModel(val task: Task) : ViewModel() {

    var dueDate: LocalDate? = null
    var dueTime: LocalTime? = null
    private var subtasks: MutableList<Subtask>

    lateinit var workspaceName: String
    var isDone: Boolean
    var isArchived: Boolean = false
    lateinit var taskTitle: String
    lateinit var taskDescription: String

    private val _subtasksLiveData = MutableLiveData<List<Subtask>>()
    val subtasksLiveData = _subtasksLiveData

    init {
        dueDate = task.dueDate.toLocalDate()
        dueTime = task.dueTime.toLocalTime()
        subtasks = task.subtasks.toMutableList()
        isDone = task.isDone
        updateLiveData()
    }

    fun needToSave(): Boolean {
        var flag = taskTitle == task.title &&
                taskDescription == task.description &&
                dueDate.toString() == task.dueDate &&
                dueTime.toString() == task.dueTime &&
                workspaceName == task.workspaceName &&
                isDone == task.isDone &&
                isArchived == task.isArchived &&
                subtasks.size - 1 == task.subtasks.size
        if (flag)
            for (i in task.subtasks.indices)
                if (subtasks[i] != task.subtasks[i])
                    flag = false
        return !flag
    }


    // Create a new subtask to be the add option for user
    private fun addADummySubtask() {
        subtasks.add(Subtask(""))
    }

    private fun updateLiveData() {
        if (subtasks.isEmpty() || subtasks.last().title.isNotEmpty())
            addADummySubtask()
        _subtasksLiveData.value = subtasks
    }

    private fun cleanTexts() {
        taskTitle.apply { trim() }
        taskDescription.apply { trim() }

        for (i in subtasks.indices) {
            subtasks[i].title.apply { trim() }
            if (subtasks[i].title.isEmpty())
                subtasks.removeAt(i)
        }
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
            isDone = isDone,
            isArchived = isArchived,
            workspaceName = workspaceName
        )
    }

    fun deleteSubtask(i: Int) {
        subtasks.removeAt(i)
        updateLiveData()
    }

    fun makeSubtaskDone(i: Int) {
        subtasks[i] = subtasks[i].copy(isDone = !subtasks[i].isDone)
        updateLiveData()
    }

    fun updateSubtaskName(i: Int, newName: String) {
        if (subtasks[i].title != newName) {
            subtasks[i] = subtasks[i].copy(title = newName)
            updateLiveData()
            log("updateSubtaskName: $subtasks")
        }
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