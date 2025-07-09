package com.shahid.premogear

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahid.premogear.data.AppDatabase
import com.shahid.premogear.data.TodoItem
import com.shahid.premogear.databinding.ActivityTodoListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoListBinding
    private lateinit var adapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TodoListAdapter(
            onToggleDone = { todo -> toggleTodoDone(todo) },
            onDelete = { todo -> deleteTodo(todo) }
        )
        binding.rvTodos.layoutManager = LinearLayoutManager(this)
        binding.rvTodos.adapter = adapter

        binding.fabAddTodo.setOnClickListener { showAddTodoDialog() }
        loadTodos()
    }

    private fun loadTodos() {
        lifecycleScope.launch {
            val todos = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(this@TodoListActivity).todoItemDao().getAllTodos()
            }
            adapter.submitList(todos)
        }
    }

    private fun showAddTodoDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_todo, null)
        val etTodo = dialogView.findViewById<EditText>(R.id.etTodo)
        AlertDialog.Builder(this)
            .setTitle("Add To-Do")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val text = etTodo.text.toString().trim()
                if (text.isNotEmpty()) {
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            AppDatabase.getDatabase(this@TodoListActivity).todoItemDao().insertTodo(TodoItem(text = text))
                        }
                        loadTodos()
                    }
                } else {
                    Toast.makeText(this, "To-Do cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun toggleTodoDone(todo: TodoItem) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(this@TodoListActivity).todoItemDao().updateTodo(todo.copy(isDone = !todo.isDone))
            }
            loadTodos()
        }
    }

    private fun deleteTodo(todo: TodoItem) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(this@TodoListActivity).todoItemDao().deleteTodo(todo)
            }
            loadTodos()
        }
    }
} 