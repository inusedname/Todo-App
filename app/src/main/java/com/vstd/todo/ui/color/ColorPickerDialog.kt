package com.vstd.todo.ui.color

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.vstd.todo.R
import com.vstd.todo.adapter.ColorAdapter
import com.vstd.todo.databinding.DialogColorPickerBinding
import com.vstd.todo.interfaces.BaseBottomDialogFragment
import com.vstd.todo.others.utilities.getColorArray

class ColorPickerDialog(private val chooseColorCompleted: (Int) -> Unit) :
    BaseBottomDialogFragment(R.layout.dialog_color_picker) {

    private lateinit var binding: DialogColorPickerBinding
    private lateinit var adapter: ColorAdapter
    private var colorSelected: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DialogColorPickerBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btSubmitColor.setOnClickListener {
            chooseColorCompleted(colorSelected!!)
            dismiss()
        }
    }

    companion object {
        const val TAG = "WorkspaceDialogPicker"
    }

    private fun setupRecyclerView() {
        val colors = requireContext().getColorArray(R.array.color_picker_colors)
        adapter = ColorAdapter(colors, onColorClicked)
        binding.rvColors.adapter = adapter
        binding.rvColors.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    }

    private val onColorClicked = { colorCode: Int ->
        colorSelected = colorCode
        binding.btSubmitColor.isEnabled = true
    }
}