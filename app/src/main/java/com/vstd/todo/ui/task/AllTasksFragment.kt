package com.vstd.todo.ui.task

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
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
import com.vstd.todo.utilities.Sorting
import com.vstd.todo.utilities.helper.getTopAppBar
import com.vstd.todo.viewmodels.TaskViewModel
import com.vstd.todo.viewmodels.TaskViewModelFactory

class AllTaskFragment : Fragment(R.layout.fragment_all_tasks), HasFab, HasBotAppBar, HasTopAppBar {

    private lateinit var repo: TodoRepo
    private lateinit var adapter: AllTasksAdapter
    private lateinit var viewModel: TaskViewModel
    private lateinit var binding: FragmentAllTasksBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentAllTasksBinding.bind(view)
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
        adapter = AllTasksAdapter(onTaskClicked, onDoneTaskClicked, onDeleteTaskClicked)
        binding.rvTasks.adapter = adapter
    }

    private val onTaskClicked = { task: Task ->
        findNavController().navigate(R.id.action_allTaskFragment_to_detailTaskFragment,
            Bundle().apply {
                putSerializable(Constants.TASK, task)
            })
    }

    private val onDoneTaskClicked = { task: Task ->
        viewModel.updateTask(task.copy(isDone = !task.isDone))
    }

    private val onDeleteTaskClicked = { task: Task ->
        viewModel.deleteTask(task)
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
        botAppBar.setFabAlignmentModeAndReplaceMenu(
            BottomAppBar.FAB_ALIGNMENT_MODE_CENTER, R.menu.home_bot_app_bar
        )
        botAppBar.setNavigationIcon(R.drawable.ic_baseline_dashboard_24)
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
                showSortPopup(itemId)
                true
            }
            else -> false
        }
    }

    override fun onBotAppBarMenuClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> true
            R.id.show_archived -> true
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

    private val onAddTaskSubmit = { task: Task ->
        viewModel.addTask(task)
    }

    private val onChooseWorkspaceSubmit = { workspaceName: String ->
        viewModel.changeWorkspace(workspaceName)
    }

    private fun showSortPopup(itemId: Int) {
        PopupMenu(requireContext(), requireActivity().findViewById(itemId)).apply {
            setOnMenuItemClickListener { onSortSubmit(it) }
            inflate(R.menu.sort_tasks_popup_menu)
            show()
        }
    }

    private fun onSortSubmit(menuItem: MenuItem): Boolean {
        val sortOptions = mapOf(
            R.id.due_date_inc to Sorting.DUE_DATE_ASC,
            R.id.due_date_dec to Sorting.DUE_DATE_DESC,
            R.id.created_date_inc to Sorting.CREATE_DATE_ASC,
            R.id.created_date_dec to Sorting.CREATE_DATE_DESC,
            R.id.last_modified_date_inc to Sorting.LAST_MODIFIED_ASC,
            R.id.last_modified_date_dec to Sorting.LAST_MODIFIED_DESC,
        )
        val myOptionId = menuItem.itemId
        viewModel.sortTasks(sortOptions[myOptionId]!!)
        return true
    }


}