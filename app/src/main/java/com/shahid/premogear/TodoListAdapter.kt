package com.shahid.premogear

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shahid.premogear.data.TodoItem
import com.shahid.premogear.databinding.ItemTodoBinding

class TodoListAdapter(
    private val onToggleDone: (TodoItem) -> Unit,
    private val onDelete: (TodoItem) -> Unit
) : ListAdapter<TodoItem, TodoListAdapter.TodoViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding, onToggleDone, onDelete)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TodoViewHolder(
        private val binding: ItemTodoBinding,
        private val onToggleDone: (TodoItem) -> Unit,
        private val onDelete: (TodoItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: TodoItem) {
            binding.tvTodoText.text = todo.text
            binding.cbDone.isChecked = todo.isDone
            binding.cbDone.setOnCheckedChangeListener(null)
            binding.cbDone.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                if (isChecked != todo.isDone) {
                    onToggleDone(todo)
                }
            }
            binding.btnDeleteTodo.setOnClickListener {
                AlertDialog.Builder(binding.root.context)
                    .setTitle("Delete To-Do")
                    .setMessage("Are you sure you want to delete this to-do item?")
                    .setPositiveButton("Delete") { _, _ ->
                        onDelete(todo)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean = oldItem == newItem
    }
} 