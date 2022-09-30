package com.vstd.todo.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vstd.todo.data.Task
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.DialogAddTaskBinding
import com.vstd.todo.interfaces.BaseBottomDialogFragment
import com.vstd.todo.ui.datetime.DateTimePickerDialog
import com.vstd.todo.ui.workspace.WorkspacePickerDialog
import com.vstd.todo.utilities.Constants
import com.vstd.todo.utilities.DateTimeUtils

class AddTaskDialog(
    private val repo: TodoRepo,
    private val onSubmit: (Task) -> Unit
) : BaseBottomDialogFragment() {

    private lateinit var binding: DialogAddTaskBinding
    private var date = "null"
    private var time = "null"
    private lateinit var workspace: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadArgs()
        setClickListeners()
    }

    private fun loadArgs() {
        workspace = requireArguments().getString(Constants.WORKSPACE_NAME_STRING) ?: ""
        binding.btSetWorkspace.text = workspace
    }

    private fun setClickListeners() {
        binding.apply {
            btAddTask.setOnClickListener { submitClicked() }
            btSetDueDate.setOnClickListener { setDueDateClicked() }
            btSetWorkspace.setOnClickListener { setWorkspaceClicked() }
        }
    }

    companion object {
        const val TAG = "AddTaskDialog"
    }

    val submitClicked = {
        val newTask = Task(
            title = binding.etTitle.text.toString(),
            workspaceName = workspace,
            dueDate = date,
            dueTime = time,
        )
        onSubmit(newTask)
        dismiss()
    }
    private val setDueDateClicked = {
        val datePickerFragment = DateTimePickerDialog(onDueDateSubmit)
        datePickerFragment.show(childFragmentManager, DateTimePickerDialog.TAG)
    }

    private val onDueDateSubmit = { date: String, time: String ->
        this.date = date
        this.time = time
        binding.btSetDueDate.text = DateTimeUtils.format(date, time)
    }

    val setWorkspaceClicked = {
        val workspacePickerFragment = WorkspacePickerDialog(repo, onWorkspaceSubmit)
        workspacePickerFragment.show(childFragmentManager, WorkspacePickerDialog.TAG)
    }

    private val onWorkspaceSubmit = { selectedWorkspace: String ->
        binding.btSetWorkspace.text = selectedWorkspace
        this.workspace = selectedWorkspace
    }
}