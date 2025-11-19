package com.example.bodytrack.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EntryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
}
