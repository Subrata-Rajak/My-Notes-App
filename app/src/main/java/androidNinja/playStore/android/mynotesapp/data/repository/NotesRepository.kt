package androidNinja.playStore.android.mynotesapp.data.repository

import androidNinja.playStore.android.mynotesapp.data.NotesDao
import androidNinja.playStore.android.mynotesapp.data.models.NotesEntity
import androidx.lifecycle.LiveData

class NotesRepository(private val notesDao: NotesDao) {

    val getAllData: LiveData<List<NotesEntity>> = notesDao.getAllData()
    val sortByHighPriority: LiveData<List<NotesEntity>> = notesDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<NotesEntity>> = notesDao.sortByLowPriority()

    suspend fun insertData(notesEntity: NotesEntity){
        notesDao.insertData(notesEntity)
    }

    suspend fun upDateData(notesEntity: NotesEntity){
        notesDao.upDateData(notesEntity)
    }

    suspend fun deleteData(notesEntity: NotesEntity){
        notesDao.deleteData(notesEntity)
    }

    suspend fun deleteAllData(){
        notesDao.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<NotesEntity>> {
        return notesDao.searchDatabase(searchQuery)
    }
}