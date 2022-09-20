package com.vstd.todo.ui.workspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vstd.todo.databinding.DialogEditWorkspaceBinding
import com.vstd.todo.ui.color.ColorPickerDialog

class EditWorkspaceDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogEditWorkspaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditWorkspaceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {
            btBack.setOnClickListener { backClicked() }
            btSave.setOnClickListener { saveClicked() }
            fixedChooseColorLayout.setOnClickListener { selectColorClicked() }
        }
    }

    private fun selectColorClicked() {
        val colorPickerDialog = ColorPickerDialog(onColorSubmit)
        colorPickerDialog.show(childFragmentManager, ColorPickerDialog.TAG)
    }

    private fun saveClicked() {
        // TODO: Not yet implemented
    }

    private fun backClicked() {
        // TODO: Not yet implemented
    }

    private val onColorSubmit = { colorCode: Int ->
        // TODO: Not yet implemented
    }

    companion object {
        const val TAG = "EditWorkspaceDialog"
    }
}