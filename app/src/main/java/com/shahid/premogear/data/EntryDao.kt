package com.shahid.premogear.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EntryDao {
    @Insert
    suspend fun insertEntry(entry: Entry)

    @Query("SELECT * FROM entries ORDER BY id DESC")
    suspend fun getAllEntries(): List<Entry>

    @androidx.room.Delete
    suspend fun deleteEntry(entry: Entry)
} 