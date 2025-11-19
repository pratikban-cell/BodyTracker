package com.example.bodytrack.data

import androidx.room.*

@Dao
interface EntryDao {

    @Insert
    suspend fun insert(entry: EntryEntity)

    @Query("SELECT * FROM entries ORDER BY date DESC")
    suspend fun getAllEntries(): List<EntryEntity>

    @Delete
    suspend fun delete(entry: EntryEntity)

    @Update
    suspend fun update(entry: EntryEntity)
}
