package com.vstd.todo

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.vstd.todo.data.Task
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.ActivityMainBinding
import com.vstd.todo.ui.task.AddTaskDialog
import com.vstd.todo.ui.workspace.WorkspacePickerDialog
import com.vstd.todo.utilities.Constants
import com.vstd.todo.viewmodels.TaskViewModel
import com.vstd.todo.viewmodels.TaskViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var repo: TodoRepo
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setOnClickListeners()
    }

    private fun setupViewModel() {
        val dao = TodoDatabase.getInstance(this).todoDAO
        repo = TodoRepo.getInstance(dao)

        taskViewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(repo)
        )[TaskViewModel::class.java]

    }

    private fun setOnClickListeners() {

        binding.fab.setOnClickListener { fabClicked() }
        binding.bottomAppBar.setNavigationOnClickListener { onChooseWorkspaceClicked() }
        binding.bottomAppBar.setOnMenuItemClickListener { onItemBotBarClicked(it) }
        binding.topAppBar.setOnMenuItemClickListener { onItemTopBarClicked(it) }
    }


    private fun fabClicked() {
        val addTaskDialog = AddTaskDialog(repo, onAddTaskSubmit)
        addTaskDialog.arguments = Bundle().apply {
            putString(Constants.WORKSPACE_NAME_STRING, taskViewModel.workspaceName)
        }
        addTaskDialog.show(supportFragmentManager, AddTaskDialog.TAG)
    }

    private val onAddTaskSubmit = { task: Task ->
        taskViewModel.addTask(task)
    }

    private val onChooseWorkspaceClicked = {
        val workspacePickerDialog = WorkspacePickerDialog(repo, onChooseWorkspaceSubmit)
        workspacePickerDialog.show(
            supportFragmentManager,
            WorkspacePickerDialog.TAG
        )
    }

    private val onChooseWorkspaceSubmit = { workspaceName: String ->
        taskViewModel.changeWorkspace(workspaceName)
    }

    private val onItemBotBarClicked = { menuItem: MenuItem ->
        // TODO: Not yet implemented
        when (menuItem.itemId) {
            R.id.search -> true
            R.id.more -> true
            else -> false
        }
    }

    private val onItemTopBarClicked = { menuItem: MenuItem ->
        // TODO: Not yet implemented
        when (menuItem.itemId) {
            R.id.sort -> {
                showSortPopup()
                true
            }
            else -> false
        }
    }

    private fun showSortPopup() {
        PopupMenu(this, binding.topAppBar.getChildAt(0)).apply {
            setOnMenuItemClickListener { onSortSubmit(it) }
            inflate(R.menu.sort_tasks_popup_menu)
            show()
        }
    }

    private val onSortSubmit = { menuItem: MenuItem ->
        when (menuItem.itemId) {
            R.id.due_date_inc -> true
            else -> false
        }
    }
}