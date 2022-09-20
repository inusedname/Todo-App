package com.vstd.todo.ui.task

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.vstd.todo.ui.datetime.DateTimePickerDialog
import com.vstd.todo.R
import com.vstd.todo.data.Task
import com.vstd.todo.databinding.FragmentDetailTaskBinding
import com.vstd.todo.utilities.Constants
import com.vstd.todo.utilities.toFriendlyString
import com.vstd.todo.utilities.toLocalDateTime
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class DetailTaskFragment : Fragment(R.layout.fragment_detail_task) {

    private lateinit var binding: FragmentDetailTaskBinding
    private lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadArgs()
        loadViews()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {
            btArchive.setOnClickListener { onArchivedClicked() }
            btDelete.setOnClickListener { onDeleteClicked() }
            btSave.setOnClickListener { onSaveClicked() }
            chipWorkspace.setOnClickListener { onSelectWorkspaceClicked() }
            chipDueDateTime.setOnClickListener { onSelectDueDateTimeClicked() }
        }
    }

    private fun loadViews() {
        binding.apply {
            etTitle.setText(task.title)
            etDescription.setText(task.description)
            chipWorkspace.text = task.workspaceName
        }
        if (task.dueDateTime != null) {
            binding.chipDueDateTime.text = task.dueDateTime!!.toLocalDateTime().toFriendlyString()
        }
    }

    private fun loadArgs() {
        task = requireArguments().getSerializable(Constants.TASK) as Task
    }

    private val onArchivedClicked = {
        // TODO: Not yet implemented
    }

    private val onDeleteClicked = {
        // TODO: Not yet implemented
    }


    private val onSaveClicked = {
        // TODO: Not yet implemented
    }

    private val onSelectDueDateTimeClicked = {
        val dialog = DateTimePickerDialog(onDateTimeSubmit)
        dialog.arguments = Bundle().apply {
            putSerializable(Constants.DATE_TIME_STRING, task.dueDateTime)
        }
        dialog.show(childFragmentManager, DateTimePickerDialog.TAG)
    }

    private val onSelectWorkspaceClicked = {
        // TODO: Not yet implemented
    }

    private val onDateTimeSubmit = { _: LocalDateTime ->

    }
}