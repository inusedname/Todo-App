package com.vstd.todo.ui.task

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vstd.todo.R
import com.vstd.todo.data.Tag
import com.vstd.todo.data.Task
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.FragmentDetailTaskBinding
import com.vstd.todo.interfaces.HasBotAppBar
import com.vstd.todo.interfaces.HasFab
import com.vstd.todo.interfaces.HasTopAppBar
import com.vstd.todo.ui.datetime.DateTimePickerDialog
import com.vstd.todo.utilities.Constants
import com.vstd.todo.utilities.toFriendlyDateTimeString
import com.vstd.todo.viewmodels.TagViewModel
import com.vstd.todo.viewmodels.TagViewModelFactory
import com.vstd.todo.viewmodels.TaskViewModel
import com.vstd.todo.viewmodels.TaskViewModelFactory
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class DetailTaskFragment :
    Fragment(R.layout.fragment_detail_task),
    HasFab,
    HasTopAppBar,
    HasBotAppBar {

    private var needToSave = false
    private lateinit var binding: FragmentDetailTaskBinding
    private lateinit var tagViewModel: TagViewModel
    private lateinit var task: Task

    private lateinit var title: String
    private lateinit var workspaceName: String
    private lateinit var description: String
    private lateinit var dueDateTime: String
    private lateinit var createDateTime: String
    private lateinit var lastModifiedDateTime: String
    private lateinit var doneSubTask: MutableList<String>
    private lateinit var notDoneSubTask: MutableList<String>
    private lateinit var tags: List<Tag>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViewModels()
        loadArgs()
        setUpDataBinding()
        setOnClickListeners()
        observing()
    }

    private fun setUpDataBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            title = this@DetailTaskFragment.title
            description = this@DetailTaskFragment.description
            createDate = getString(
                R.string.created,
                this@DetailTaskFragment.createDateTime.toFriendlyDateTimeString()
            )
            lastModifiedDate =
                getString(
                    R.string.created,
                    this@DetailTaskFragment.lastModifiedDateTime.toFriendlyDateTimeString()
                )
            workspace = this@DetailTaskFragment.workspaceName
            dueDate = this@DetailTaskFragment.dueDateTime
        }
    }

    private fun setUpViewModels() {
        val todoRepo = TodoRepo.getInstance(TodoDatabase.getInstance(requireContext()).todoDAO)
        tagViewModel = ViewModelProvider(
            this, TagViewModelFactory(todoRepo)
        )[TagViewModel::class.java]
    }

    private fun setOnClickListeners() {
        binding.apply {
            // TODO: Not yet implemented
            chipDueDateTime.setOnClickListener { onSelectDueDateTimeClicked() }
        }
    }

    private fun loadArgs() {
        task = requireArguments().getSerializable(Constants.TASK) as Task
        title = task.title
        description = task.description
        dueDateTime = task.dueDateTime ?: "Select date time"
        createDateTime = task.createdDateTime
        lastModifiedDateTime = task.lastModifiedDateTime
        doneSubTask = task.doneSubtasks.toMutableList()
        notDoneSubTask = task.notDoneSubtasks.toMutableList()
        workspaceName = task.workspaceName

        tagViewModel.getTagsWithTask(task.taskId)
    }

    private fun observing() {
        tagViewModel.tagsLiveData.observe(viewLifecycleOwner) {
            tags = it
        }
    }

    private val onSelectDueDateTimeClicked = {
        val dialog = DateTimePickerDialog(onDateTimeSubmit)
        dialog.arguments = Bundle().apply {
            putSerializable(Constants.DATE_TIME_STRING, task.dueDateTime)
        }
        dialog.show(childFragmentManager, DateTimePickerDialog.TAG)
    }

    private val onDateTimeSubmit = { _: LocalDateTime ->
        // TODO: Not yet implemented
    }

    override fun onBotAppBarNavigationClick() {}

    override fun onBotAppBarMenuClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.make_archive -> true
            R.id.make_done -> true
            else -> false
        }
    }

    override fun setUpBotAppBarAppearance(botAppBar: BottomAppBar) {
        botAppBar.setFabAlignmentModeAndReplaceMenu(
            BottomAppBar.FAB_ALIGNMENT_MODE_END, R.menu.detail_bot_app_bar
        )
        botAppBar.navigationIcon = null
    }

    override fun onFabClicked() {
        // TODO: Not yet implemented
    }

    override fun setUpFabAppearance(fab: FloatingActionButton) {
        fab.setImageResource(R.drawable.ic_baseline_save_24)
    }

    override fun onTopAppBarMenuClick(item: MenuItem): Boolean {
        return false
    }

    override fun setUpTopAppBarAppearance(topAppBar: MaterialToolbar) {
        topAppBar.setNavigationIcon(R.drawable.ic_baseline_keyboard_arrow_left_24)
        topAppBar.title = ""
        topAppBar.menu.clear()
    }

    override fun onTopAppBarNavigationClick() {
        if (needToSave) {
            AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_save_24)
                .setTitle(getString(R.string.save_changes))
                .setMessage(getString(R.string.do_you_want_to_save_changes))
                .setPositiveButton(getString(R.string.yes)) { _, _ -> doSave() }
                .setNegativeButton(getString(R.string.no), null)
        }
        navigateBackToAllTask()
    }

    private fun doSave() {
        val updatedTask = task.copy(
            title = binding.title!!,
            description = binding.description!!,
            dueDateTime = binding.dueDate,
            lastModifiedDateTime = LocalDateTime.now().toString(),
            doneSubtasks = doneSubTask,
            notDoneSubtasks = notDoneSubTask
        )
        ViewModelProvider(
            this, TaskViewModelFactory(
                TodoRepo.getInstance(TodoDatabase.getInstance(requireContext()).todoDAO)
            )
        )[TaskViewModel::class.java].updateTask(updatedTask)
    }

    private fun navigateBackToAllTask() {
        findNavController().navigate(
            R.id.action_detailTaskFragment_to_allTaskFragment
        )
    }
}