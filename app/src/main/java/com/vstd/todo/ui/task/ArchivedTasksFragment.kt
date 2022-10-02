package com.vstd.todo.ui.task

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.vstd.todo.R
import com.vstd.todo.adapter.ArchivedTasksAdapter
import com.vstd.todo.data.Task
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.FragmentArchivedTasksBinding
import com.vstd.todo.interfaces.HasBotAppBar
import com.vstd.todo.interfaces.HasCustomBackPress
import com.vstd.todo.interfaces.HasTopAppBar
import com.vstd.todo.others.helper.showSortPopup
import com.vstd.todo.viewmodels.TaskViewModel

class ArchivedTasksFragment :
    Fragment(R.layout.fragment_archived_tasks),
    HasTopAppBar,
    HasBotAppBar,
    HasCustomBackPress {

    private lateinit var repo: TodoRepo
    private lateinit var adapter: ArchivedTasksAdapter
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var binding: FragmentArchivedTasksBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentArchivedTasksBinding.bind(view)
        repo = TodoRepo.getInstance(TodoDatabase.getInstance(requireContext()).todoDAO)

        setUpAdapter()
        observing()
    }

    private fun setUpAdapter() {
        adapter = ArchivedTasksAdapter(onRestoreSubmit, onDeleteSubmit)
        binding.rvTasks.adapter = adapter
        viewModel.fetchArchived()
    }

    private fun observing() {
        viewModel.taskLiveData.observe(viewLifecycleOwner) { tasks: List<Task> ->
            adapter.setData(tasks)
        }
    }

    override fun onBotAppBarNavigationClick() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All")
            .setMessage("Are you sure to delete all archived tasks?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.clearArchived()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onBotAppBarMenuClick(item: MenuItem): Boolean {
        return true
    }

    override fun setUpBotAppBarAppearance(botAppBar: BottomAppBar) {
        botAppBar.menu.clear()
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

    private val onRestoreSubmit = { task: Task ->
        viewModel.updateTask(task.copy(isArchived = false))
    }

    private val onDeleteSubmit = { task: Task ->
        viewModel.deleteTask(task)
    }

    private val onSortSubmit = { sortOption: String ->
        viewModel.sortTasks(sortOption)
    }

    private fun navigateBackToAllTask() {
        findNavController().popBackStack()
    }

    override fun onBackPressed() {
        navigateBackToAllTask()
    }
}