package com.vstd.todo.ui.task

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vstd.todo.R
import com.vstd.todo.adapter.SubtaskAdapter
import com.vstd.todo.data.Subtask
import com.vstd.todo.data.Task
import com.vstd.todo.data.database.TodoDatabase
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.FragmentDetailTaskBinding
import com.vstd.todo.interfaces.HasBotAppBar
import com.vstd.todo.interfaces.HasCustomBackPress
import com.vstd.todo.interfaces.HasFab
import com.vstd.todo.interfaces.HasTopAppBar
import com.vstd.todo.others.constants.BundleKeys
import com.vstd.todo.others.utilities.*
import com.vstd.todo.ui.datetime.DateTimePickerDialog
import com.vstd.todo.ui.workspace.WorkspacePickerDialog
import com.vstd.todo.viewmodels.DetailTaskViewModel
import com.vstd.todo.viewmodels.DetailTaskViewModelFactory
import com.vstd.todo.viewmodels.TaskViewModel

class DetailTaskFragment :
    Fragment(R.layout.fragment_detail_task),
    HasFab,
    HasTopAppBar,
    HasBotAppBar,
    HasCustomBackPress {

    private lateinit var repo: TodoRepo
    private lateinit var detailTaskViewModel: DetailTaskViewModel
    private lateinit var subtasksAdapter: SubtaskAdapter
    private lateinit var binding: FragmentDetailTaskBinding
    private lateinit var fab: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDetailTaskBinding.bind(view)
        repo = TodoRepo.getInstance(TodoDatabase.getInstance(requireContext()).todoDAO)

        loadArgs()
        setUpAdapters()
        setOnClickListeners()
        observing()
    }

    private fun loadArgs() {
        val task = requireArguments().getSerializable(BundleKeys.TASK_OBJ) as Task
        setUpViewModels(task)
        displayData(task)
    }

    private fun setUpAdapters() {
        subtasksAdapter = SubtaskAdapter(onDoneSubtask, onDeleteSubtask, onRenameSubtask)
        binding.rvSubtasks.adapter = subtasksAdapter
    }

    private fun setOnClickListeners() {
        binding.apply {
            chipWorkspace.setOnClickListener { onSelectWorkspaceClicked() }
            chipDueDateTime.setOnClickListener { onSelectDueDateTimeClicked() }
        }
    }

    private fun observing() {
        detailTaskViewModel.subtasksLiveData.observe(viewLifecycleOwner) { subtasks: List<Subtask> ->
            subtasksAdapter.setData(subtasks)
        }
    }

    private fun refreshViewModelData() {
        binding.rvSubtasks.clearFocus()
        detailTaskViewModel.apply {
            taskTitle = binding.etTitle.text.toString()
            taskDescription = binding.etDescription.text.toString()
            workspaceName = binding.chipWorkspace.text.toString()
        }
    }

    private fun displayData(task: Task) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            etTitle.setText(task.title)
            etDescription.setText(task.description)
            tvCreatedDate.text = getString(
                R.string.created,
                task.createdDateTime.toFriendlyDateTimeString()
            )
            tvModifiedDate.text = getString(
                R.string.last_modified,
                task.lastModifiedDateTime.toFriendlyDateTimeString()
            )
            chipDueDateTime.text = detailTaskViewModel.dueDate.let {
                DateTimeUtils.formatFriendly(
                    it,
                    detailTaskViewModel.dueTime
                )
            }
                ?: getString(R.string.pick_a_date_and_time)
            chipWorkspace.text = task.workspaceName
        }
    }

    private fun setUpViewModels(task: Task) {

        detailTaskViewModel = ViewModelProvider(
            this, DetailTaskViewModelFactory(task)
        )[DetailTaskViewModel::class.java]
    }

    override fun onBotAppBarNavigationClick() {}

    override fun onBotAppBarMenuClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.make_done -> {
                updateTaskDone()
                true
            }
            else -> false
        }
    }

    override fun setUpBotAppBarAppearance(botAppBar: BottomAppBar) {
        botAppBar.setFabAlignmentModeAndReplaceMenu(
            BottomAppBar.FAB_ALIGNMENT_MODE_END, R.menu.detail_bot_app_bar
        )
        botAppBar.navigationIcon = null
    }

    override fun onFabClicked(fab: View) {
        refreshViewModelData()
        doSaveAndNavigateIfCan()
    }

    override fun setUpFabAppearance(fab: FloatingActionButton) {
        this.fab = fab
        fab.setImageResource(R.drawable.ic_baseline_save_24)
    }

    override fun onTopAppBarMenuClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                confirmAndDelete()
                true
            }
            R.id.archive -> {
                updateTaskArchive()
                true
            }
            else -> false
        }
    }

    override fun setUpTopAppBarAppearance(topAppBar: MaterialToolbar) {
        topAppBar.setNavigationIcon(R.drawable.ic_baseline_keyboard_arrow_left_24)
        topAppBar.title = ""
        topAppBar.menu.clear()
        topAppBar.inflateMenu(R.menu.detail_top_app_bar)
    }

    override fun onTopAppBarNavigationClick() {
        backWithoutSave(
            onAccept = { doSaveAndNavigateIfCan() },
            onDecline = { navigateBackToAllTask() },
            ifNoNeedToSave = { navigateBackToAllTask() }
        )
    }

    override fun onBackPressed() {
        backWithoutSave(
            onAccept = { doSaveAndNavigateIfCan() },
            onDecline = { navigateBackToAllTask() },
            ifNoNeedToSave = { navigateBackToAllTask() }
        )
    }

    private fun confirmAndDelete() {
        AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setTitle("Delete task")
            .setMessage("Do you want to delete?")
            .setPositiveButton(R.string.yes) { _, _ ->
                deleteTask()
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    private fun deleteTask() {
        val taskViewModel: TaskViewModel by activityViewModels()
        taskViewModel.deleteTask(detailTaskViewModel.oldTask)
        requireActivity().snack(binding.root, getString(R.string.task_deleted), fab)
        navigateBackToAllTask()
    }

    private fun updateTaskDone() {
        backWithoutSave(
            onAccept = {
                detailTaskViewModel.isDone = detailTaskViewModel.isDone.not()
                doSaveAndNavigateIfCan()
            },
            onDecline = {
                val oldTask = detailTaskViewModel.oldTask
                updateTask(oldTask.copy(isDone = !oldTask.isDone))
                navigateBackToAllTask()
            },
            ifNoNeedToSave = {
                val oldTask = detailTaskViewModel.oldTask
                updateTask(oldTask.copy(isDone = !oldTask.isDone))
                navigateBackToAllTask()
            }
        )
    }

    private fun updateTaskArchive() {
        backWithoutSave(
            onAccept = {
                detailTaskViewModel.isArchived = true
                doSaveAndNavigateIfCan()
                requireActivity().snackArchived(binding.root, fab)
            },
            onDecline = {
                val oldTask = detailTaskViewModel.oldTask
                updateTask(oldTask.copy(isArchived = true))
                navigateBackToAllTask()
                requireActivity().snackArchived(binding.root, fab)
            },
            ifNoNeedToSave = {
                val oldTask = detailTaskViewModel.oldTask
                updateTask(oldTask.copy(isArchived = true))
                navigateBackToAllTask()
                requireActivity().snackArchived(binding.root, fab)
            }
        )
    }

    private fun doSaveAndNavigateIfCan(navigateEnabled: Boolean = true) {
        if (detailTaskViewModel.getValidateStatus() != TextUtils.PASSED_ALL_VALIDATION) {
            requireActivity().snackAlert(binding.root, detailTaskViewModel.getValidateStatus(), fab)
            return
        }
        if (detailTaskViewModel.needToSave()) {
            updateTask(detailTaskViewModel.getNewTask())
            requireActivity().snack(binding.root, "Updated 😊", fab)
        }
        if (navigateEnabled) navigateBackToAllTask()
    }

    private fun updateTask(task: Task) {
        val taskViewModel: TaskViewModel by activityViewModels()
        taskViewModel.updateTask(task)
    }

    private fun navigateBackToAllTask() {
        findNavController().popBackStack()
    }

    private fun backWithoutSave(
        onAccept: () -> Unit,
        onDecline: () -> Unit = {},
        ifNoNeedToSave: () -> Unit = {}
    ) {
        refreshViewModelData()
        detailTaskViewModel.getValidateStatus()
        if (detailTaskViewModel.needToSave()) {
            AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_save_24)
                .setTitle(getString(R.string.save_changes))
                .setMessage(getString(R.string.do_you_want_to_save_changes))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    onAccept()
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> onDecline() }
                .show()
        } else
            ifNoNeedToSave()
    }

    private val onDoneSubtask = { i: Int ->
        detailTaskViewModel.updateSubtaskDone(i)
    }

    private val onDeleteSubtask = { i: Int ->
        detailTaskViewModel.deleteSubtask(i)
    }

    private val onRenameSubtask = { i: Int, newName: String ->
        requireActivity().hideSoftKeyboard(binding.root)
        detailTaskViewModel.renameSubtask(i, newName)
    }

    private val onSelectDueDateTimeClicked = {
        val dialog = DateTimePickerDialog(onDateTimeSubmit)
        dialog.arguments = Bundle().apply {
            putSerializable(BundleKeys.DATE_STRING, detailTaskViewModel.dueDate)
            putSerializable(BundleKeys.TIME_STRING, detailTaskViewModel.dueTime)
        }
        dialog.show(childFragmentManager, DateTimePickerDialog.TAG)
    }

    private val onDateTimeSubmit = { date: String, time: String ->
        detailTaskViewModel.dueDate = date
        detailTaskViewModel.dueTime = time
        binding.chipDueDateTime.text = DateTimeUtils.formatFriendly(date, time)
    }

    private val onSelectWorkspaceClicked = {
        val workspacePickerFragment = WorkspacePickerDialog(repo, onWorkspaceSubmit)
        workspacePickerFragment.show(childFragmentManager, WorkspacePickerDialog.TAG)
    }

    private val onWorkspaceSubmit = { workspaceName: String ->
        binding.chipWorkspace.text = workspaceName
    }
}