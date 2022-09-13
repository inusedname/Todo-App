package com.vstd.todo

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vstd.todo.adapter.AllTasksAdapter
import com.vstd.todo.data.Task
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.FragmentAllTasksBinding
import com.vstd.todo.utilities.toast
import com.vstd.todo.viewmodels.AllTasksViewModel
import com.vstd.todo.viewmodels.AllTasksViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class AllTaskFragment : Fragment(R.layout.fragment_all_tasks) {

    private lateinit var adapter: AllTasksAdapter
    private lateinit var viewModel: AllTasksViewModel
    private lateinit var binding: FragmentAllTasksBinding

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
            this,
            AllTasksViewModelFactory(repo)
        )[AllTasksViewModel::class.java]

        binding.viewModel = viewModel
    }

    private fun setAdapter() {
        adapter = AllTasksAdapter(onTaskClicked, onDoneTaskClicked, onDeleteTaskClicked)
        binding.rvTasks.adapter = adapter
    }

    private fun observing() {
        viewModel.taskLiveData.observe(viewLifecycleOwner) { tasks ->
            adapter.setData(tasks)
            activity?.toast("There are ${tasks.size} items")
        }
    }

    private val onTaskClicked = { _: Task ->
        //TODO: Not implemented yet
    }
    private val onDoneTaskClicked = { _: Task ->
        //TODO: Not implemented yet
    }
    private val onDeleteTaskClicked = { task: Task ->
        viewModel.deleteTask(task)
    }
}