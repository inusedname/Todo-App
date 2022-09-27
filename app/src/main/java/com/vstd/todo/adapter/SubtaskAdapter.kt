package com.vstd.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vstd.todo.R
import com.vstd.todo.data.Subtask
import com.vstd.todo.databinding.ItemSubtaskBinding
import com.vstd.todo.utilities.getColor
import com.vstd.todo.utilities.log

class SubtaskAdapter(
    private val onDoneClicked: (Int) -> Unit,
    private val onDeleteClicked: (Int) -> Unit,
    private val onUpdateItemName: (Int, String) -> Unit,
) :
    ListAdapter<Subtask, SubtaskAdapter.ItemViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemSubtaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
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
            else getColor(itemView.context, R.color.black)

            binding.apply {
                if (subtask.title.isEmpty()) {
                    cbSubtaskDone.visibility = View.INVISIBLE
                    ivRemoveSubtask.visibility = View.INVISIBLE
                } else {
                    cbSubtaskDone.visibility = View.VISIBLE
                    ivRemoveSubtask.visibility = View.VISIBLE
                }
                etSubtaskTitle.setText(subtask.title)
                etSubtaskTitle.setTextColor(colorByDone)

                cbSubtaskDone.isChecked = subtask.isDone
                cbSubtaskDone.setOnClickListener {
                    etSubtaskTitle.clearFocus()
                    onDoneClicked(adapterPosition)
                }
                ivRemoveSubtask.setOnClickListener {
                    etSubtaskTitle.clearFocus()
                    onDeleteClicked(adapterPosition)
                    log("bind: Deleted $adapterPosition")
                }
                etSubtaskTitle.setOnEditorActionListener { _, type, _ ->
                    return@setOnEditorActionListener if (type == EditorInfo.IME_ACTION_DONE) {
                        etSubtaskTitle.clearFocus()
                        true
                    } else
                        false
                }
                etSubtaskTitle.setOnFocusChangeListener { _, isFocused: Boolean ->
                    if (!isFocused)
                        updateNewName(subtask, etSubtaskTitle, adapterPosition)
                }
            }
        }

        fun updateNewName(
            subtask: Subtask,
            et: EditText,
            adapterPosition: Int
        ) {
            if (subtask.title != et.text.toString()) {
                log("bind: FocusUpdated $adapterPosition")
                onUpdateItemName(adapterPosition, et.text.toString())

                // ERROR: Without this the adapter become shit, no idea why the final item isn't refreshed
                if (adapterPosition == currentList.size - 1)
                    et.setText("")
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