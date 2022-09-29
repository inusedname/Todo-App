package com.vstd.todo.ui.task

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vstd.todo.MainActivity
import com.vstd.todo.R
import com.vstd.todo.adapter.AllTasksAdapter
import com.vstd.todo.data.Task
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.FragmentAllTasksBinding
import com.vstd.todo.interfaces.HasBotAppBar
import com.vstd.todo.interfaces.HasFab
import com.vstd.todo.interfaces.HasTopAppBar
import com.vstd.todo.ui.workspace.WorkspacePickerDialog
import com.vstd.todo.utilities.Constants
import com.vstd.todo.utilities.helper.getTopAppBar
import com.vstd.todo.utilities.helper.showSortPopup
import com.vstd.todo.viewmodels.TaskViewModel
import com.vstd.todo.viewmodels.TaskViewModelFactory

class AllTaskFragment :
    Fragment(R.layout.fragment_all_tasks),
    HasFab,
    HasBotAppBar,
    HasTopAppBar {

    private lateinit var repo: TodoRepo
    private lateinit var adapter: AllTasksAdapter
    private lateinit var viewModel: TaskViewModel
    private lateinit var binding: FragmentAllTasksBinding

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
        adapter = AllTasksAdapter(onTaskClicked, onDoneTaskClicked)
        binding.rvTasks.adapter = adapter
    }

    private fun observing() {
        viewModel.taskLiveData.observe(viewLifecycleOwner) { tasks: List<Task> ->
            adapter.setData(tasks)
        }
    }

    override fun setUpFabAppearance(fab: FloatingActionButton) {
        fab.setImageResource(R.drawable.ic_baseline_add_24)
        fab.contentDescription = getString(R.string.add_a_task)
    }

    override fun setUpBotAppBarAppearance(botAppBar: BottomAppBar) {
        botAppBar.menu.clear()
        botAppBar.inflateMenu(R.menu.home_bot_app_bar)
        botAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        botAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        botAppBar.setNavigationContentDescription(R.string.choose_workspace)
    }

    override fun setUpTopAppBarAppearance(topAppBar: MaterialToolbar) {
        topAppBar.navigationIcon = null
        topAppBar.menu.clear()
        topAppBar.inflateMenu(R.menu.home_top_app_bar)
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

    override fun onBotAppBarMenuClick(item: MenuItem): Boolean {
        // TODO: Not yet implemented
        return when (item.itemId) {
            R.id.search -> true
            R.id.show_archived -> {
                navigateToArchivedTasks()
                true
            }
            else -> false
        }
    }

    override fun onFabClicked(fab: View) {
        val addTaskDialog = AddTaskDialog(repo, onAddTaskSubmit)
        addTaskDialog.arguments = Bundle().apply {
            putString(Constants.WORKSPACE_NAME_STRING, viewModel.workspaceNameLiveData.value)
        }
        addTaskDialog.show(childFragmentManager, AddTaskDialog.TAG)
    }

    override fun onBotAppBarNavigationClick() {
        val workspacePickerDialog = WorkspacePickerDialog(repo, onChooseWorkspaceSubmit)
        workspacePickerDialog.show(
            childFragmentManager, WorkspacePickerDialog.TAG
        )
    }

    override fun onTopAppBarNavigationClick() {}

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).getTopAppBar().title =
            viewModel.workspaceNameLiveData.value
    }

    private val onTaskClicked = { task: Task ->
        navigateToDetail(task)
    }

    private val onDoneTaskClicked = { task: Task ->
        viewModel.updateTask(task.copy(isDone = !task.isDone))
    }

    private val onAddTaskSubmit = { task: Task ->
        viewModel.addTask(task)
    }

    private val onChooseWorkspaceSubmit = { workspaceName: String ->
        viewModel.changeWorkspace(workspaceName)
    }

    private val onSortSubmit = { sortOption: String ->
        viewModel.sortTasks(sortOption)
    }

    private fun navigateToArchivedTasks() {
        findNavController().navigate(R.id.action_allTaskFragment_to_archivedTasksFragment)
    }

    private fun navigateToDetail(task: Task) {
        findNavController().navigate(R.id.action_allTaskFragment_to_detailTaskFragment,
            Bundle().apply {
                putSerializable(Constants.TASK, task)
            })
    }
}