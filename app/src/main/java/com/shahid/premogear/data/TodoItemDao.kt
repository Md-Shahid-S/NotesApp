package com.shahid.premogear.data

import androidx.room.*

@Dao
interface TodoItemDao {
    @Insert
    suspend fun insertTodo(todo: TodoItem)

    @Query("SELECT * FROM todo_items ORDER BY id DESC")
    suspend fun getAllTodos(): List<TodoItem>

    @Update
    suspend fun updateTodo(todo: TodoItem)

    @Delete
    suspend fun deleteTodo(todo: TodoItem)
} 