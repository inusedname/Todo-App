package com.vstd.todo.ui.task

import android.os.Bundle
import android.view.View
import com.vstd.todo.R
import com.vstd.todo.data.Task
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.DialogAddTaskBinding
import com.vstd.todo.interfaces.BaseBottomDialogFragment
import com.vstd.todo.others.constants.BundleKeys
import com.vstd.todo.others.utilities.DateTimeUtils
import com.vstd.todo.others.utilities.TextUtils
import com.vstd.todo.others.utilities.toast
import com.vstd.todo.ui.datetime.DateTimePickerDialog
import com.vstd.todo.ui.workspace.WorkspacePickerDialog

class AddTaskDialog(
    private val repo: TodoRepo,
    private val onSubmit: (Task) -> Unit
) : BaseBottomDialogFragment(R.layout.dialog_add_task) {

    private lateinit var binding: DialogAddTaskBinding
    private var date = ""
    private var time = ""
    private lateinit var workspace: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogAddTaskBinding.bind(view)
        loadArgs()
        setClickListeners()
        binding.etTitle.requestFocus()
    }

    private fun loadArgs() {
        workspace = requireArguments().getString(BundleKeys.WORKSPACE_NAME_STRING) ?: ""
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
        datePickerFragment.arguments = Bundle().apply {
            putString(BundleKeys.DATE_STRING, date)
            putString(BundleKeys.TIME_STRING, time)
        }
        datePickerFragment.show(childFragmentManager, DateTimePickerDialog.TAG)
    }

    private fun setWorkspaceClicked() {
        val workspacePickerFragment = WorkspacePickerDialog(repo, onWorkspaceSubmit)
        workspacePickerFragment.show(childFragmentManager, WorkspacePickerDialog.TAG)
    }

    private val onDueDateSubmit = { date: String, time: String ->
        this.date = date
        this.time = time
        binding.btSetDueDate.text =
            DateTimeUtils.formatFriendly(date, time) ?: getString(R.string.set_due_date)
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
/**
Logcat:
onPause, false
onStop, false
onDestroyView, false
onDestroy, false
 */