package com.vstd.todo.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vstd.todo.R
import com.vstd.todo.data.Task
import com.vstd.todo.databinding.ItemTaskBinding
import com.vstd.todo.utilities.getColor

class AllTasksAdapter(
    private val onItemClicked: (Task) -> Unit,
    private val onDoneClicked: (Task) -> Unit,
    private val onDeleteClicked: (Task) -> Unit
) :
    ListAdapter<Task, AllTasksAdapter.ItemViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemTaskBinding.inflate(
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

    inner class ItemViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            val colorByDone =
                if (task.isDone)
                    getColor(itemView.context, R.color.light_gray)
                else
                    getColor(itemView.context, R.color.black)

            binding.apply {
                tvTitle.text = task.title
                tvDescription.text = task.description
                checkboxDone.isChecked = task.isDone
                checkboxDone.setOnClickListener { onDoneClicked(task) }
                btDeleteTask.setOnClickListener { onDeleteClicked(task) }
                itemView.setOnClickListener { onItemClicked(task) }

                tvTitle.setTextColor(colorByDone)
                tvDescription.setTextColor(colorByDone)
                btDeleteTask.imageTintList = ColorStateList.valueOf(colorByDone)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.isDone == newItem.isDone
                        && oldItem.description == newItem.description
                        && oldItem.title == newItem.title
            }

        }
    }
}