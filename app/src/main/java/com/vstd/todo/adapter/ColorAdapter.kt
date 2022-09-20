package com.vstd.todo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vstd.todo.databinding.ItemColorBinding

class ColorAdapter(
    private val colors: List<Int>,
    private val onColorClicked: (Int) -> Unit
) :
    RecyclerView.Adapter<ColorAdapter.ItemViewHolder>() {

    private var lastSelected: ItemColorBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemColorBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(colors[position])
    }

    inner class ItemViewHolder(private val binding: ItemColorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(colorCode: Int) {
            binding.apply {
                color.setCardBackgroundColor(colorCode)
                root.setOnClickListener {
                    colorButtonStateUpdate(this, colorCode)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    private val colorButtonStateUpdate = { binding: ItemColorBinding, colorCode: Int ->
        binding.signalSelected.visibility = View.VISIBLE
        lastSelected?.signalSelected?.visibility = View.GONE
        lastSelected = binding
        notifyDataSetChanged()
        // TODO: Chỗ này lỗi chưa set hiển thị được màu
        Log.i("TAG", "Mike: Color clicked")
        onColorClicked(colorCode)
    }
}