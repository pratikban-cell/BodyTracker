package com.example.bodytrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodytrack.data.DatabaseProvider
import com.example.bodytrack.data.EntryRepository
import com.example.bodytrack.data.EntryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EntryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EntryRepository

    private val _allEntries = MutableStateFlow<List<EntryEntity>>(emptyList())
    val allEntries: StateFlow<List<EntryEntity>> = _allEntries

    init {
        val db = DatabaseProvider.getDatabase(application)
        repository = EntryRepository(db.entryDao())
        loadEntries()
    }

    fun loadEntries() {
        viewModelScope.launch {
            _allEntries.value = repository.getAllEntries()
        }
    }

    fun saveEntry(weight: Double, date: Long, imagePath: String) {
        viewModelScope.launch {
            repository.addEntry(weight, date, imagePath)
            loadEntries()
        }
    }

    fun deleteEntry(entry: EntryEntity) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
            loadEntries()
        }
    }

    fun updateEntry(entry: EntryEntity) {
        viewModelScope.launch {
            repository.updateEntry(entry)
            loadEntries()
        }
    }
}
