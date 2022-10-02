package com.vstd.todo.ui.workspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.vstd.todo.R
import com.vstd.todo.adapter.ChooseWorkspaceAdapter
import com.vstd.todo.data.Workspace
import com.vstd.todo.data.repository.TodoRepo
import com.vstd.todo.databinding.DialogWorkspacePickerBinding
import com.vstd.todo.interfaces.BaseBottomDialogFragment
import com.vstd.todo.utilities.Constants
import com.vstd.todo.utilities.toast
import com.vstd.todo.viewmodels.WorkspaceViewModel
import com.vstd.todo.viewmodels.WorkspaceViewModelFactory

class WorkspacePickerDialog(
    private val repo: TodoRepo,
    private val onChooseWorkspaceSubmit: (String) -> Unit
) :
    BaseBottomDialogFragment() {

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
        super.onViewCreated(view, savedInstanceState)
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

    private fun setupRecyclerView() {
        adapter = ChooseWorkspaceAdapter(onWorkspaceSelected, onWorkspaceLongClicked)
        binding.rvWorkspace.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        binding.rvWorkspace.adapter = adapter
    }

    private fun observing() {
        viewModel.workspaceLiveData.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }

    private fun editClicked() {
        requireActivity().toast(getString(R.string.edit_workspace_helper))
    }

    private fun addClicked() {
        val workspaceNames = viewModel.workspaceLiveData.value!!.map { it.workspaceName }
        val editWorkspaceDialog = EditWorkspaceDialog(onAddWorkspaceSubmit, workspaceNames)
        editWorkspaceDialog.show(childFragmentManager, EditWorkspaceDialog.TAG)
    }

    private val onWorkspaceSelected = { workspaceName: String ->
        onChooseWorkspaceSubmit(workspaceName)
        dismiss()
    }

    private val onAddWorkspaceSubmit = { workspace: Workspace, _: Boolean ->
        viewModel.addWorkspace(workspace)
    }

    private val onWorkspaceLongClicked = { workspace: Workspace ->
        val workspaceNames = viewModel.workspaceLiveData.value!!.map { it.workspaceName }
        val editWorkspaceDialog = EditWorkspaceDialog(onEditWorkspaceDialog, workspaceNames)

        editWorkspaceDialog.arguments = Bundle().apply {
            putSerializable(Constants.WORKSPACE_OBJ, workspace)
        }
        editWorkspaceDialog.show(childFragmentManager, EditWorkspaceDialog.TAG)
    }
    private val onEditWorkspaceDialog = { workspace: Workspace, isDeleted: Boolean ->
        if (isDeleted) {
            viewModel.deleteWorkspace(workspace)
        } else {
            viewModel.editWorkspace(workspace)
        }
    }

    companion object {
        const val TAG = "DialogWorkspacePicker"
    }
}