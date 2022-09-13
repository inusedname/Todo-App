package com.vstd.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vstd.todo.adapter.ChooseWorkspaceAdapter
import com.vstd.todo.data.Workspace
import com.vstd.todo.databinding.FragmentChooseWorkspaceDialogListDialogBinding

class ChooseWorkspaceDialogFragment(private val workspaces: List<Workspace>) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChooseWorkspaceDialogListDialogBinding
    private lateinit var adapter: ChooseWorkspaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            FragmentChooseWorkspaceDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btAdd.setOnClickListener {
            // TODO: Not yet implemented
        }

        binding.btEdit.setOnClickListener {
            // TODO: Not yet implemented
        }
    }

    companion object {
        const val TAG = "ChooseWorkspaceDialogFragment"
    }

    private fun setupRecyclerView() {
        adapter = ChooseWorkspaceAdapter(workspaces, onWorkspaceClicked)
        binding.rvWorkspace.adapter = adapter
    }

    private val onWorkspaceClicked = { _: Workspace ->
        // TODO: Not yet implemented
    }
}