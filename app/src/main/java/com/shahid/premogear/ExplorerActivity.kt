package com.shahid.premogear

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahid.premogear.data.AppDatabase
import com.shahid.premogear.databinding.ActivityExplorerBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import com.shahid.premogear.data.Entry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Intent
import android.view.Menu
import android.view.MenuItem

class ExplorerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExplorerBinding
    private lateinit var adapter: EntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExplorerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = EntryAdapter { entry ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    AppDatabase.getDatabase(this@ExplorerActivity).entryDao().deleteEntry(entry)
                }
                loadEntries()
            }
        }
        binding.rvEntries.layoutManager = LinearLayoutManager(this)
        binding.rvEntries.adapter = adapter

        binding.fabAddEntry.setOnClickListener {
            showAddEntryDialog()
        }
        loadEntries()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_explorer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                loadEntries()
                true
            }
            R.id.action_todo_list -> {
                startActivity(Intent(this, TodoListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadEntries() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvEntries.visibility = View.GONE
        binding.tvEmpty.visibility = View.GONE
        lifecycleScope.launch {
            try {
                val entries = withContext(Dispatchers.IO) {
                    AppDatabase.getDatabase(this@ExplorerActivity).entryDao().getAllEntries()
                }
                if (entries.isNullOrEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvEntries.visibility = View.GONE
                } else {
                    adapter.submitList(entries)
                    binding.rvEntries.visibility = View.VISIBLE
                    binding.tvEmpty.visibility = View.GONE
                }
                Toast.makeText(this@ExplorerActivity, "Loaded ${entries.size} entries", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ExplorerActivity, "Failed to load entries: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showAddEntryDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_entry, null)
        val etExercise = dialogView.findViewById<EditText>(R.id.etExercise)
        val etSkill = dialogView.findViewById<EditText>(R.id.etSkill)
        val etExtra = dialogView.findViewById<EditText>(R.id.etExtra)
        val etJournal = dialogView.findViewById<EditText>(R.id.etJournal)
        // Removed etTodoList

        AlertDialog.Builder(this)
            .setTitle("Add Entry")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val now = Date()
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now)
                val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(now)
                val entry = Entry(
                    date = date,
                    time = time,
                    exerciseLog = etExercise.text.toString(),
                    skillLog = etSkill.text.toString(),
                    extraLearningLog = etExtra.text.toString(),
                    journalEntry = etJournal.text.toString()
                    // Removed todoList
                )
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        AppDatabase.getDatabase(this@ExplorerActivity).entryDao().insertEntry(entry)
                    }
                    loadEntries()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
} 