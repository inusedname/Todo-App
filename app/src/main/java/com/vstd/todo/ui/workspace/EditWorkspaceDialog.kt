package com.vstd.todo.ui.workspace

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vstd.todo.data.Workspace
import com.vstd.todo.databinding.DialogEditWorkspaceBinding
import com.vstd.todo.interfaces.BaseBottomDialogFragment
import com.vstd.todo.ui.color.ColorPickerDialog
import com.vstd.todo.utilities.Constants
import com.vstd.todo.utilities.TextUtils
import com.vstd.todo.utilities.getContrastColor
import com.vstd.todo.utilities.toast

class EditWorkspaceDialog(private val onWorkspaceSubmit: (Workspace) -> Unit) :
    BaseBottomDialogFragment() {

    private lateinit var binding: DialogEditWorkspaceBinding
    private var colorPickerColor: Int
        get() = binding.btChooseColor.backgroundTintList?.defaultColor ?: Constants.DEFAULT_COLOR
        set(value) {
            binding.btChooseColor.setTextColor(getContrastColor(value))
            binding.btChooseColor.iconTint = ColorStateList.valueOf(getContrastColor(value))
            binding.btChooseColor.backgroundTintList = ColorStateList.valueOf(value)
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
        super.onViewCreated(view, savedInstanceState)
        loadArgs()
        setOnClickListeners()
        binding.etWorkspaceName.requestFocus()
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
            btBack.setOnClickListener { onBackClicked() }
            btSave.setOnClickListener { onSaveClicked() }
            btChooseColor.setOnClickListener { onSelectColorSubmit() }
        }
    }

    private fun onSelectColorSubmit() {
        val colorPickerDialog = ColorPickerDialog(onColorSubmit)
        colorPickerDialog.show(childFragmentManager, ColorPickerDialog.TAG)
    }

    private fun onSaveClicked() {
        if (getValidateStatus() != TextUtils.PASSED_ALL_VALIDATION) {
            requireActivity().toast(getValidateStatus())
            return
        }
        val newWorkspace = Workspace(
            binding.etWorkspaceName.text.toString(),
            colorPickerColor
        )
        onWorkspaceSubmit(newWorkspace)
        dismiss()
    }

    private fun onBackClicked() {
        dismiss()
    }

    private val onColorSubmit = { colorCode: Int ->
        colorPickerColor = colorCode
    }

    private fun getValidateStatus(): String {
        return if (!TextUtils.isValidTitle(binding.etWorkspaceName.text.toString())) {
            TextUtils.NOT_VALID_TITLE
        } else TextUtils.PASSED_ALL_VALIDATION
    }

    companion object {
        const val TAG = "EditWorkspaceDialog"
    }
}