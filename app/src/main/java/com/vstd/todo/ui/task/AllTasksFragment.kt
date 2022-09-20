package com.vstd.todo.ui.task

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vstd.todo.MainActivity
import com.vstd.todo.R
import com.vstd.todo.adapter.AllTasksAdapter
import com.vstd.todo.data.Task
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.FragmentAllTasksBinding
import com.vstd.todo.utilities.Constants
import com.vstd.todo.utilities.helper.hideAllExceptContainer
import com.vstd.todo.utilities.helper.showAll
import com.vstd.todo.viewmodels.TaskViewModel
import com.vstd.todo.viewmodels.TaskViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class AllTaskFragment : Fragment(R.layout.fragment_all_tasks) {

    private lateinit var adapter: AllTasksAdapter
    private lateinit var viewModel: TaskViewModel
    private lateinit var binding: FragmentAllTasksBinding

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).showAll()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAllTasksBinding.bind(view)

        setViewModel()
        setAdapter()
        observing()
    }


    private fun setViewModel() {
        val dao = TodoDatabase.getInstance(requireContext()).todoDAO
        val repo = TodoRepo.getInstance(dao)

        viewModel = ViewModelProvider(
            requireActivity(),
            TaskViewModelFactory(repo)
        )[TaskViewModel::class.java]

        binding.viewModel = viewModel
    }

    private fun setAdapter() {
        adapter = AllTasksAdapter(onTaskClicked, onDoneTaskClicked, onDeleteTaskClicked)
        binding.rvTasks.adapter = adapter
    }

    private fun observing() {
        viewModel.taskLiveData.observe(viewLifecycleOwner) { tasks: List<Task> ->
            adapter.setData(tasks)
        }
    }

    private val onTaskClicked = { task: Task ->
        (activity as MainActivity).hideAllExceptContainer()
        findNavController().navigate(
            R.id.action_allTaskFragment_to_detailTaskFragment,
            Bundle().apply {
                putSerializable(Constants.TASK, task)
            })
    }
    private val onDoneTaskClicked = { _: Task ->
        //TODO: Not implemented yet
    }
    private val onDeleteTaskClicked = { task: Task ->
        viewModel.deleteTask(task)
    }
}