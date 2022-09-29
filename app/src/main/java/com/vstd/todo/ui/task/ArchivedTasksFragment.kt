package com.vstd.todo.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.vstd.todo.R
import com.vstd.todo.adapter.AllTasksAdapter
import com.vstd.todo.data.Task
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.FragmentAllTasksBinding
import com.vstd.todo.interfaces.HasBotAppBar
import com.vstd.todo.interfaces.HasCustomBackPress
import com.vstd.todo.interfaces.HasTopAppBar
import com.vstd.todo.utilities.helper.showSortPopup
import com.vstd.todo.viewmodels.TaskViewModel
import com.vstd.todo.viewmodels.TaskViewModelFactory

class ArchivedTasksFragment :
    Fragment(R.layout.fragment_all_tasks),
    HasTopAppBar,
    HasBotAppBar,
    HasCustomBackPress {

    private lateinit var repo: TodoRepo
    private lateinit var adapter: AllTasksAdapter
    private lateinit var viewModel: TaskViewModel
    private lateinit var binding: FragmentAllTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = TodoRepo.getInstance(TodoDatabase.getInstance(requireContext()).todoDAO)

        setUpViewModel()
        setUpAdapter()
        observing()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), TaskViewModelFactory(repo)
        )[TaskViewModel::class.java]
    }

    private fun setUpAdapter() {
        adapter = AllTasksAdapter(onTaskClicked, onUnarchivedTaskClicked)
        binding.rvTasks.adapter = adapter
        viewModel.fetchArchived()
    }

    private fun observing() {
        viewModel.taskLiveData.observe(viewLifecycleOwner) { tasks: List<Task> ->
            adapter.setData(tasks)
        }
    }

    override fun onBotAppBarNavigationClick() {
        viewModel.clearArchived()
        navigateBackToAllTask()
    }

    override fun onBotAppBarMenuClick(item: MenuItem): Boolean {
        // TODO: Not yet implemented
        return when (item.itemId) {
            R.id.search -> true
            else -> false
        }
    }

    override fun setUpBotAppBarAppearance(botAppBar: BottomAppBar) {
        botAppBar.menu.clear()
        botAppBar.inflateMenu(R.menu.archived_bot_app_bar)
        botAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        botAppBar.setNavigationIcon(R.drawable.ic_baseline_clear_all_24)
        botAppBar.setNavigationContentDescription(R.string.delete_all)
    }

    override fun onTopAppBarMenuClick(item: MenuItem): Boolean {
        return when (val itemId = item.itemId) {
            R.id.sort -> {
                showSortPopup(itemId, onSortSubmit)
                true
            }
            else -> false
        }
    }

    override fun onTopAppBarNavigationClick() {
        navigateBackToAllTask()
    }

    override fun setUpTopAppBarAppearance(topAppBar: MaterialToolbar) {
        topAppBar.setNavigationIcon(R.drawable.ic_baseline_keyboard_arrow_left_24)
        topAppBar.title = "Archived Tasks"
        topAppBar.menu.clear()
        topAppBar.inflateMenu(R.menu.home_top_app_bar)
    }

    private val onTaskClicked = { task: Task ->
    }

    private val onUnarchivedTaskClicked = { _: Task ->
        // TODO: Not yet implemented
    }

    private val onSortSubmit = { sortOption: String ->
        viewModel.sortTasks(sortOption)
    }

    private fun navigateBackToAllTask() {
        findNavController().popBackStack()
    }

    override fun onBackPressed() {
        viewModel.changeWorkspace()
        navigateBackToAllTask()
    }
}