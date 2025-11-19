package com.example.bodytrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class EntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val weight: Double,
    val date: Long,        // store as timestamp
    val imagePath: String  // path to photo saved in internal storage
)
