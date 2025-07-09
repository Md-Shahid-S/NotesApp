package com.shahid.premogear

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shahid.premogear.data.Entry
import com.shahid.premogear.databinding.ItemEntryBinding
import java.text.SimpleDateFormat
import java.util.Locale

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
            // Format date to dd-MM-yyyy
            val formattedDate = try {
                val inDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                outDateFormat.format(inDateFormat.parse(entry.date) ?: entry.date)
            } catch (e: Exception) {
                entry.date
            }
            // Format time to 12-hour with AM/PM
            val formattedTime = try {
                val inFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val outFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                outFormat.format(inFormat.parse(entry.time) ?: entry.time)
            } catch (e: Exception) {
                entry.time
            }
            binding.tvDate.text = "$formattedDate  $formattedTime"
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