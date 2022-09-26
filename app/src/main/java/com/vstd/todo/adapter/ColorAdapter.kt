package com.vstd.todo.adapter

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

    val TAG = "ColorAdapter"
    private var nowSelected = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemColorBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ItemViewHolder(private val binding: ItemColorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                signalSelected.visibility =
                    if (position == nowSelected) View.VISIBLE else View.GONE
                color.setCardBackgroundColor(colors[position])
                root.setOnClickListener {
                    colorButtonStateUpdate(position, colors[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    private val colorButtonStateUpdate = { position: Int, colorCode: Int ->
        val oldSelected = nowSelected
        nowSelected = position
        if (oldSelected != -1)
            notifyItemChanged(oldSelected)
        notifyItemChanged(position)
        onColorClicked(colorCode)
    }
}