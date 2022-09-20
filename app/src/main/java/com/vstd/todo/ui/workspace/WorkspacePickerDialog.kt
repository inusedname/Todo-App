package com.vstd.todo.ui.workspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vstd.todo.adapter.ChooseWorkspaceAdapter
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.DialogWorkspacePickerBinding
import com.vstd.todo.viewmodels.WorkspaceViewModel
import com.vstd.todo.viewmodels.WorkspaceViewModelFactory

class WorkspacePickerDialog(
    private val repo: TodoRepo,
    private val onChooseWorkspaceSubmit: (String) -> Unit
) :
    BottomSheetDialogFragment() {

    enum class STATE {
        VIEW, EDIT
    }

    private lateinit var binding: DialogWorkspacePickerBinding
    private lateinit var viewModel: WorkspaceViewModel
    private lateinit var adapter: ChooseWorkspaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DialogWorkspacePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModel()
        setupRecyclerView()
        setupClickListeners()
        observing()
    }

    private fun setupViewModel() {

        viewModel = ViewModelProvider(
            this,
            WorkspaceViewModelFactory(repo)
        )[WorkspaceViewModel::class.java]
    }

    private fun setupClickListeners() {
        binding.apply {
            btAdd.setOnClickListener { addClicked() }
            btEdit.setOnClickListener { editClicked() }
        }
    }

    private fun editClicked() {
        // TODO: Not yet implemented
    }

    private fun addClicked() {
        val editWorkspaceDialog = EditWorkspaceDialog()
        editWorkspaceDialog.show(childFragmentManager, EditWorkspaceDialog.TAG)
    }

    companion object {
        const val TAG = "DialogWorkspacePicker"
    }

    private fun setupRecyclerView() {
        adapter = ChooseWorkspaceAdapter(onWorkspaceSelected)
        binding.rvWorkspace.adapter = adapter
    }

    private fun observing() {
        viewModel.workspace.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }

    private val onWorkspaceSelected = { workspace: String ->
        onChooseWorkspaceSubmit(workspace)
        dismiss()
    }
}