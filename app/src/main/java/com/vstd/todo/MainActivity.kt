package com.vstd.todo

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.ActivityMainBinding
import com.vstd.todo.interfaces.HasBotAppBar
import com.vstd.todo.interfaces.HasCustomBackPress
import com.vstd.todo.interfaces.HasFab
import com.vstd.todo.interfaces.HasTopAppBar
import com.vstd.todo.viewmodels.TaskViewModel
import com.vstd.todo.viewmodels.TaskViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val containerChildManager by lazy {
        (supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment).childFragmentManager
    }

    private fun currentFragment(): Fragment {
        return containerChildManager.fragments.last()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpDataBinding()
        setOnClickListeners()
        setOnFragmentChangeListeners()
    }

    private fun setUpDataBinding() {
        val repo = TodoRepo.getInstance(TodoDatabase.getInstance(this).todoDAO)
        binding.viewModel =
            ViewModelProvider(this, TaskViewModelFactory(repo))[TaskViewModel::class.java]
        binding.lifecycleOwner = this
    }

    private fun setOnFragmentChangeListeners() {
        setUpActivityComponentAppearance()
        containerChildManager.addOnBackStackChangedListener {
            setUpActivityComponentAppearance()
        }
    }

    private fun setUpActivityComponentAppearance() {
        val fragment = currentFragment()
        if (fragment is HasTopAppBar) {
            binding.topAppBar.visibility = View.VISIBLE
            (fragment as HasTopAppBar).setUpTopAppBarAppearance(binding.topAppBar)
        } else binding.topAppBar.visibility = View.GONE

        if (fragment is HasBotAppBar) {
            binding.bottomAppBar.visibility = View.VISIBLE
            (fragment as HasBotAppBar).setUpBotAppBarAppearance(binding.bottomAppBar)
        } else binding.bottomAppBar.visibility = View.GONE

        // The second condition is to enable moving animation of fab
        if (fragment is HasFab) {
            (fragment as HasFab).setUpFabAppearance(binding.fab)
            if (!binding.fab.isShown)
                binding.fab.show()
        } else
            binding.fab.hide()
    }

    private fun setOnClickListeners() {

        binding.fab.setOnClickListener { onFabClicked(it) }
        binding.bottomAppBar.setNavigationOnClickListener { onBotBarNavClicked() }
        binding.bottomAppBar.setOnMenuItemClickListener { onItemBotBarClicked(it) }
        binding.topAppBar.setOnMenuItemClickListener { onItemTopBarClicked(it) }
        binding.topAppBar.setNavigationOnClickListener { onTopBarNavClicked() }
    }


    private fun onFabClicked(fab: View) {
        if (currentFragment() is HasFab) {
            (currentFragment() as HasFab).onFabClicked(fab)
        }
    }

    private fun onBotBarNavClicked() {
        if (currentFragment() is HasBotAppBar) {
            (currentFragment() as HasBotAppBar).onBotAppBarNavigationClick()
        }
    }

    private fun onItemBotBarClicked(menuItem: MenuItem): Boolean {
        return if (currentFragment() is HasBotAppBar) {
            (currentFragment() as HasBotAppBar).onBotAppBarMenuClick(menuItem)
        } else false
    }

    private fun onTopBarNavClicked() {
        if (currentFragment() is HasTopAppBar)
            (currentFragment() as HasTopAppBar).onTopAppBarNavigationClick()
    }

    private fun onItemTopBarClicked(menuItem: MenuItem): Boolean {
        return if (currentFragment() is HasTopAppBar) {
            (currentFragment() as HasTopAppBar).onTopAppBarMenuClick(menuItem)
        } else false
    }

    override fun onBackPressed() {
        if (currentFragment() is HasCustomBackPress)
            (currentFragment() as HasCustomBackPress).onBackPressed()
        else
            super.onBackPressed()
    }
}