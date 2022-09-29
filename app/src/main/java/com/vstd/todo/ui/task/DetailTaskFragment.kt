package com.vstd.todo.ui.task

import android.app.AlertDialog
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
import com.vstd.todo.ui.datetime.DateTimePickerDialog
import com.vstd.todo.ui.workspace.WorkspacePickerDialog
import com.vstd.todo.utilities.Constants
import com.vstd.todo.utilities.DateTimeUtils
import com.vstd.todo.utilities.helper.hideSoftKeyboard
import com.vstd.todo.utilities.helper.snack
import com.vstd.todo.utilities.helper.snackAlert
import com.vstd.todo.utilities.toFriendlyDateTimeString
import com.vstd.todo.viewmodels.*

class DetailTaskFragment :
    Fragment(R.layout.fragment_detail_task),
    HasFab,
    HasTopAppBar,
    HasBotAppBar,
    HasCustomBackPress {

    private lateinit var repo: TodoRepo
    private lateinit var detailTaskViewModel: DetailTaskViewModel
    private lateinit var tagsViewModel: TagViewModel
    private lateinit var subtasksAdapter: SubtaskAdapter
    private lateinit var binding: FragmentDetailTaskBinding
    private lateinit var fab: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = TodoRepo.getInstance(TodoDatabase.getInstance(requireContext()).todoDAO)

        loadArgs()
        setUpAdapters()
        setOnClickListeners()
        observing()
    }

    private fun loadArgs() {
        val task = requireArguments().getSerializable(Constants.TASK) as Task
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
        tagsViewModel.tagsLiveData.observe(viewLifecycleOwner) {
            // TODO: Not yet implemented
        }
    }

    private fun refreshViewModelData() {
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
                DateTimeUtils.format(
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

        tagsViewModel = ViewModelProvider(
            this, TagViewModelFactory(
                task.taskId,
                repo
            )
        )[TagViewModel::class.java]
    }

    override fun onBotAppBarNavigationClick() {}

    override fun onBotAppBarMenuClick(item: MenuItem): Boolean {
        // TODO: Not yet implemented
        return when (item.itemId) {
            R.id.make_done -> {
                true
            }
            R.id.archive -> {
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
        doSave()
    }

    override fun setUpFabAppearance(fab: FloatingActionButton) {
        this.fab = fab
        fab.setImageResource(R.drawable.ic_baseline_save_24)
    }

    override fun onTopAppBarMenuClick(item: MenuItem): Boolean {
        // TODO: Not yet implemented
        return when (item.itemId) {
            R.id.archive -> {
                true
            }
            R.id.notify -> {
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
        backWithoutSave()
    }

    private fun doSave() {
        if (!detailTaskViewModel.needToSave()) {
            navigateBackToAllTask()
            return
        }
        if (detailTaskViewModel.getValidateStatus() != PASSED_ALL_VALIDATION) {
            requireActivity().snackAlert(binding.root, detailTaskViewModel.getValidateStatus(), fab)
            return
        }
        val repo = TodoRepo.getInstance(TodoDatabase.getInstance(requireContext()).todoDAO)
        ViewModelProvider(
            requireActivity(), TaskViewModelFactory(repo)
        )[TaskViewModel::class.java].updateTask(detailTaskViewModel.getTask())
        requireActivity().snack(binding.root, "Updated 😊", fab)
        navigateBackToAllTask()
    }

    private fun navigateBackToAllTask() {
        findNavController().popBackStack()
    }

    override fun onBackPressed() {
        backWithoutSave()
    }

    private fun backWithoutSave() {
        refreshViewModelData()
        if (detailTaskViewModel.needToSave()) {
            AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_baseline_save_24)
                .setTitle(getString(R.string.save_changes))
                .setMessage(getString(R.string.do_you_want_to_save_changes))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    doSave()
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> navigateBackToAllTask() }
                .show()
        } else
            navigateBackToAllTask()
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
            putSerializable(Constants.DATE_STRING, detailTaskViewModel.dueDate)
            putSerializable(Constants.TIME_STRING, detailTaskViewModel.dueTime)
        }
        dialog.show(childFragmentManager, DateTimePickerDialog.TAG)
    }

    private val onDateTimeSubmit = { date: String, time: String ->
        detailTaskViewModel.dueDate = date
        detailTaskViewModel.dueTime = time
        binding.chipDueDateTime.text = DateTimeUtils.format(date, time)
    }

    private val onSelectWorkspaceClicked = {
        val workspacePickerFragment = WorkspacePickerDialog(repo, onWorkspaceSubmit)
        workspacePickerFragment.show(childFragmentManager, WorkspacePickerDialog.TAG)
    }

    private val onWorkspaceSubmit = { workspaceName: String ->
        binding.chipWorkspace.text = workspaceName
    }
}