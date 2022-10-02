package com.vstd.todo.ui.workspace

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import com.vstd.todo.R
import com.vstd.todo.data.Workspace
import com.vstd.todo.databinding.DialogEditWorkspaceBinding
import com.vstd.todo.interfaces.BaseBottomDialogFragment
import com.vstd.todo.others.constants.BundleKeys
import com.vstd.todo.others.utilities.ColorUtils.getContrastColor
import com.vstd.todo.others.utilities.ColorUtils.getRandomColor
import com.vstd.todo.others.utilities.TextUtils
import com.vstd.todo.others.utilities.toast
import com.vstd.todo.ui.color.ColorPickerDialog

class EditWorkspaceDialog(
    private val onWorkspaceSubmit: (Workspace, Boolean) -> Unit,
    private val workspaceNames: List<String>
) :
    BaseBottomDialogFragment(R.layout.dialog_edit_workspace) {

    private var workspace: Workspace? = null
    private lateinit var binding: DialogEditWorkspaceBinding
    private var colorPickerColor: Int
        get() = binding.btChooseColor.backgroundTintList?.defaultColor ?: -1
        set(value) {
            binding.btChooseColor.setTextColor(getContrastColor(value))
            binding.btChooseColor.iconTint = ColorStateList.valueOf(getContrastColor(value))
            binding.btChooseColor.backgroundTintList = ColorStateList.valueOf(value)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DialogEditWorkspaceBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        loadArgs()
        setOnClickListeners()
        binding.etWorkspaceName.requestFocus()
    }

    private fun loadArgs() {
        colorPickerColor = getRandomColor()
        arguments?.let { bundle ->
            binding.btBack.text = getString(R.string.add_workspace)
            binding.btDelete.visibility = View.VISIBLE
            workspace = bundle.getSerializable(BundleKeys.WORKSPACE_OBJ) as Workspace?
            workspace?.let {
                binding.etWorkspaceName.apply {
                    setText(it.workspaceName)
                    isEnabled = false
                    isFocusable = false
                    isFocusableInTouchMode = false
                }
                colorPickerColor = it.workspaceColor
            }
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            btBack.setOnClickListener { onBackClicked() }
            btSave.setOnClickListener { onSaveClicked() }
            btChooseColor.setOnClickListener { onSelectColorSubmit() }
            btDelete.setOnClickListener { onDeleteClicked() }
        }
    }

    private fun onDeleteClicked() {
        onWorkspaceSubmit(workspace!!, true)
        dismiss()
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
        onWorkspaceSubmit(newWorkspace, false)
        dismiss()
    }

    private fun onBackClicked() {
        dismiss()
    }

    private val onColorSubmit = { colorCode: Int ->
        colorPickerColor = colorCode
    }

    private fun getValidateStatus(): String {
        val name = binding.etWorkspaceName.text.toString()
        val isEnabled = binding.etWorkspaceName.isEnabled
        return if (!TextUtils.isValidTitle(name)) {
            TextUtils.NOT_VALID_TITLE
        } else if (isEnabled && workspaceNames.contains(name))
            TextUtils.WORKSPACE_NAME_COINCIDENCE
        else TextUtils.PASSED_ALL_VALIDATION
    }

    companion object {
        const val TAG = "EditWorkspaceDialog"
    }
}