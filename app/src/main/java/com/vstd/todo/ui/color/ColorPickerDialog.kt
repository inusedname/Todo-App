package com.vstd.todo.ui.color

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.vstd.todo.adapter.ColorAdapter
import com.vstd.todo.databinding.DialogColorPickerBinding
import com.vstd.todo.interfaces.BaseBottomDialogFragment
import com.vstd.todo.utilities.Constants

class ColorPickerDialog(private val chooseColorCompleted: (Int) -> Unit) :
    BaseBottomDialogFragment() {

    private lateinit var binding: DialogColorPickerBinding
    private lateinit var adapter: ColorAdapter
    private var colorSelected: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DialogColorPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        val colors = ArrayList(Constants.COLORS.values)
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