package com.vstd.todo.adapter
//
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vstd.todo.data.Workspace
import com.vstd.todo.databinding.ItemCardWorkspaceBinding

class ChooseWorkspaceAdapter(
    workspaces: List<Workspace>,
    private val onWorkspaceClickListener: ((Workspace) -> Unit)
) :
    ListAdapter<Workspace, ChooseWorkspaceAdapter.ChooseWorkspaceViewHolder>(
        DIFF_CALLBACK
    ) {

    init {
        submitList(workspaces)
    }

    fun updateList(workspaces: List<Workspace>) {
        submitList(workspaces)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseWorkspaceViewHolder {
        val binding =
            ItemCardWorkspaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChooseWorkspaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseWorkspaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChooseWorkspaceViewHolder(private val binding: ItemCardWorkspaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workspace: Workspace) {
            binding.tvWorkspaceName.text = workspace.workspaceName
            binding.root.setOnClickListener {
                onWorkspaceClickListener.invoke(workspace)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Workspace>() {
            override fun areItemsTheSame(oldItem: Workspace, newItem: Workspace): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Workspace, newItem: Workspace): Boolean {
                return oldItem.workspaceName == newItem.workspaceName
            }
        }
    }
}