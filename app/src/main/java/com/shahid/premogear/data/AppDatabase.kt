package com.shahid.premogear.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shahid.premogear.data.TodoItem
import com.shahid.premogear.data.TodoItemDao

@Database(entities = [Entry::class, TodoItem::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
    abstract fun todoItemDao(): TodoItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "daily_rays_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
} 