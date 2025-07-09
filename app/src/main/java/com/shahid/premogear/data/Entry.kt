package com.shahid.premogear.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val time: String,
    val exerciseLog: String,
    val skillLog: String,
    val extraLearningLog: String,
    val journalEntry: String
) 