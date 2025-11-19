package com.example.bodytrack.data

class EntryRepository(private val dao: EntryDao) {

    suspend fun addEntry(weight: Double, date: Long, imagePath: String) {
        val entry = EntryEntity(weight = weight, date = date, imagePath = imagePath)
        dao.insert(entry)
    }

    suspend fun getAllEntries(): List<EntryEntity> {
        return dao.getAllEntries()
    }

    suspend fun deleteEntry(entry: EntryEntity) {
        dao.delete(entry)
    }

    suspend fun updateEntry(entry: EntryEntity) {
        dao.update(entry)
    }
}
