package com.example.bodytrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodytrack.data.DatabaseProvider
import com.example.bodytrack.data.EntryRepository
import com.example.bodytrack.data.EntryEntity
import kotlinx.coroutines.launch

class EntryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EntryRepository

    init {
        val db = DatabaseProvider.getDatabase(application)
        repository = EntryRepository(db.entryDao())
    }

    fun saveEntry(weight: Double, date: Long, imagePath: String) {
        viewModelScope.launch {
            repository.addEntry(weight, date, imagePath)
        }
    }
    fun deleteEntry(entry: EntryEntity) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }

    fun updateEntry(entry: EntryEntity) {
        viewModelScope.launch {
            repository.updateEntry(entry)
        }
    }

    suspend fun getAllEntries(): List<EntryEntity> {
        return repository.getAllEntries()
    }
}
