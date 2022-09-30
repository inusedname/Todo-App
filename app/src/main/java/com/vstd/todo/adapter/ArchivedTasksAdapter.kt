package com.vstd.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vstd.todo.data.Task
import com.vstd.todo.databinding.ItemTaskArchiveBinding

class ArchivedTasksAdapter(
    private val onRestoreSubmit: (Task) -> Unit,
    private val onDeleteSubmit: (Task) -> Unit,
) :
    ListAdapter<Task, ArchivedTasksAdapter.ItemViewHolder>(DiffCallback) {

    private var nowSelected = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemTaskArchiveBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    fun setData(list: List<Task>) {
        submitList(list.toList())
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class ItemViewHolder(private val binding: ItemTaskArchiveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                tvTitle.text = task.title
                tvDescription.text = task.description
                btnDelete.setOnClickListener { onDeleteSubmit(task) }
                btnRestore.setOnClickListener { onRestoreSubmit(task) }
                layoutAction.visibility =
                    if (adapterPosition == nowSelected) View.VISIBLE else View.GONE
                root.setOnClickListener {
                    onItemClicked(adapterPosition)
                }
            }
        }
    }

    private val onItemClicked = { position: Int ->
        val oldSelected = nowSelected
        nowSelected = position
        if (oldSelected != -1)
            notifyItemChanged(oldSelected)
        notifyItemChanged(position)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.description == newItem.description
                        && oldItem.title == newItem.title
            }

        }
    }
}