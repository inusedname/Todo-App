package com.vstd.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vstd.todo.R
import com.vstd.todo.data.Subtask
import com.vstd.todo.databinding.ItemSubtaskBinding
import com.vstd.todo.utilities.getColor

class SubtaskAdapter(
    private val onDoneClicked: (Subtask) -> Unit,
    private val onDeleteClicked: (Subtask) -> Unit
) :
    ListAdapter<Subtask, SubtaskAdapter.ItemViewHolder>(DiffCallback) {
    val TAG = "SubtaskAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemSubtaskBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    fun setData(list: List<Subtask>) {
        submitList(list.toList())
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class ItemViewHolder(private val binding: ItemSubtaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subtask: Subtask) {
            val colorByDone = if (subtask.isDone) getColor(itemView.context, R.color.light_gray)
            else getColor(itemView.context, R.color.white)

            binding.apply {
                etSubtaskTitle.setText(subtask.title)
                etSubtaskTitle.setTextColor(colorByDone)
                cbSubtaskDone.isChecked = subtask.isDone
                cbSubtaskDone.setOnClickListener { onDoneClicked(subtask) }
                // TODO: Implement delete subtask
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Subtask>() {
            override fun areItemsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
                return oldItem.isDone == newItem.isDone
                        && oldItem.title == newItem.title
            }
        }
    }
}