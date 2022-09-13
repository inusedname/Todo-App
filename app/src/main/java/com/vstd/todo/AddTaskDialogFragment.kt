package com.vstd.todo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vstd.todo.data.Task
import com.vstd.todo.databinding.FragmentAddTaskBinding
import com.vstd.todo.utilities.DateTimeUtils

class AddTaskDialogFragment : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentAddTaskBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btAddTask.setOnClickListener {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask() {
        val newTask = Task(
            title = "fak",
            createdDateTime = DateTimeUtils.now().toString(),
            lastModifiedDateTime = DateTimeUtils.now().toString(),
            workspaceName = "default",
        )
    }

    companion object {
        const val TAG = "AddTaskBotSheetFragment"
    }
}