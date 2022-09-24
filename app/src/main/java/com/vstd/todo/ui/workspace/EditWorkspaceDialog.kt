package com.vstd.todo.ui.workspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vstd.todo.data.Workspace
import com.vstd.todo.databinding.DialogEditWorkspaceBinding
import com.vstd.todo.ui.color.ColorPickerDialog
import com.vstd.todo.utilities.Constants

class EditWorkspaceDialog(private val onWorkspaceSubmit: (Workspace) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogEditWorkspaceBinding
    private var colorPickerColor: Int
        get() = binding.bgBtChooseColor.color.cardBackgroundColor.defaultColor
        set(value) {
            binding.bgBtChooseColor.color.setCardBackgroundColor(value)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditWorkspaceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadArgs()
        setOnClickListeners()
    }

    private fun loadArgs() {
        arguments?.let { bundle ->
            val workspace = bundle.getSerializable(Constants.WORKSPACE_OBJ) as Workspace?
            workspace?.let {
                binding.etWorkspaceName.setText(it.workspaceName)
                colorPickerColor = it.workspaceColor
            }
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            btBack.setOnClickListener { backClicked() }
            btSave.setOnClickListener { saveClicked() }
            btChooseColor.setOnClickListener { selectColorClicked() }
        }
    }

    private fun selectColorClicked() {
        val colorPickerDialog = ColorPickerDialog(onColorSubmit)
        colorPickerDialog.show(childFragmentManager, ColorPickerDialog.TAG)
    }

    private fun saveClicked() {
        val newWorkspace = Workspace(
            binding.etWorkspaceName.text.toString(),
            colorPickerColor
        )
        onWorkspaceSubmit(newWorkspace)
        dismiss()
    }

    private fun backClicked() {
        dismiss()
    }

    private val onColorSubmit = { colorCode: Int ->
        colorPickerColor = colorCode
    }

    companion object {
        const val TAG = "EditWorkspaceDialog"
    }
}