package com.shahid.premogear

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shahid.premogear.data.Entry
import com.shahid.premogear.databinding.ItemEntryBinding

class EntryAdapter(
    private val onDelete: ((Entry) -> Unit)? = null
) : ListAdapter<Entry, EntryAdapter.EntryViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val binding = ItemEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntryViewHolder(binding, onDelete)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EntryViewHolder(
        private val binding: ItemEntryBinding,
        private val onDelete: ((Entry) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: Entry) {
            binding.tvDate.text = "${entry.date}  ${entry.time}"
            binding.tvExercise.text = entry.exerciseLog
            binding.tvSkill.text = entry.skillLog
            binding.tvExtra.text = entry.extraLearningLog
            binding.tvJournal.text = entry.journalEntry

            binding.btnDelete.setOnClickListener {
                AlertDialog.Builder(binding.root.context)
                    .setTitle("Delete Entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton("Delete") { _, _ ->
                        onDelete?.invoke(entry)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Entry>() {
        override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean = oldItem == newItem
    }
} 