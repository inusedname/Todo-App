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
import com.vstd.todo.utilities.TextUtils
import com.vstd.todo.utilities.toast

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
        binding.etTitle.requestFocus()
    }

    private fun loadArgs() {
        workspace = requireArguments().getString(Constants.WORKSPACE_NAME_STRING) ?: ""
        binding.btSetWorkspace.text = workspace
    }

    private fun setClickListeners() {
        binding.apply {
            btSubmit.setOnClickListener { submitClicked() }
            btSetDueDate.setOnClickListener { setDueDateClicked() }
            btSetWorkspace.setOnClickListener { setWorkspaceClicked() }
        }
    }

    private fun submitClicked() {
        if (getValidateStatus() != TextUtils.PASSED_ALL_VALIDATION) {
            requireActivity().toast(getValidateStatus())
            return
        }
        val newTask = Task(
            title = binding.etTitle.text.toString(),
            workspaceName = workspace,
            dueDate = date,
            dueTime = time,
        )
        onSubmit(newTask)
        dismiss()

    }

    private fun setDueDateClicked() {
        val datePickerFragment = DateTimePickerDialog(onDueDateSubmit)
        datePickerFragment.show(childFragmentManager, DateTimePickerDialog.TAG)
    }

    private fun setWorkspaceClicked() {
        val workspacePickerFragment = WorkspacePickerDialog(repo, onWorkspaceSubmit)
        workspacePickerFragment.show(childFragmentManager, WorkspacePickerDialog.TAG)
    }

    private val onDueDateSubmit = { date: String, time: String ->
        this.date = date
        this.time = time
        binding.btSetDueDate.text = DateTimeUtils.format(date, time)
    }

    private val onWorkspaceSubmit = { selectedWorkspace: String ->
        binding.btSetWorkspace.text = selectedWorkspace
        this.workspace = selectedWorkspace
    }

    private fun getValidateStatus(): String {
        return if (!TextUtils.isValidTitle(binding.etTitle.text.toString()))
            TextUtils.NOT_VALID_TITLE
        else
            TextUtils.PASSED_ALL_VALIDATION
    }

    companion object {
        const val TAG = "AddTaskDialog"
    }
}