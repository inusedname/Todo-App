package com.vstd.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.ActivityMainBinding
import com.vstd.todo.viewmodels.MainActivityViewModel
import com.vstd.todo.viewmodels.MainActivityViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setOnClickListeners()
    }

    private fun setupViewModel() {
        val dao = TodoDatabase.getInstance(this).todoDAO
        val repo = TodoRepo.getInstance(dao)
        viewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(repo)
        )[MainActivityViewModel::class.java]
    }

    private fun setOnClickListeners() {

        binding.fab.setOnClickListener {
            val addTaskDialogFragment = AddTaskDialogFragment()
            addTaskDialogFragment.show(supportFragmentManager, AddTaskDialogFragment.TAG)
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val workspaces = viewModel.getWorkspaces()
                val chooseWorkspaceDialogFragment = ChooseWorkspaceDialogFragment(workspaces)
                chooseWorkspaceDialogFragment.show(
                    supportFragmentManager,
                    ChooseWorkspaceDialogFragment.TAG
                )
            }
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // TODO: Not yet implemented
                    true
                }
                R.id.more -> {
                    // TODO: Not yet implemented
                    true
                }
                else -> false
            }
        }
    }
}