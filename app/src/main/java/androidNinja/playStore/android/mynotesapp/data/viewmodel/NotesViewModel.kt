package androidNinja.playStore.android.mynotesapp.data.viewmodel

import android.app.Application
import androidNinja.playStore.android.mynotesapp.data.NotesDatabase
import androidNinja.playStore.android.mynotesapp.data.models.NotesEntity
import androidNinja.playStore.android.mynotesapp.data.repository.NotesRepository
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {

    val notesRepository: NotesRepository

    val getAllNotes: LiveData<List<NotesEntity>>
    val sortByHighPriority: LiveData<List<NotesEntity>>
    val sortByLowPriority: LiveData<List<NotesEntity>>

    init {
        val dao = NotesDatabase.getDataBaseInstance(application).getNotesDao()
        notesRepository = NotesRepository(dao)
        getAllNotes = notesRepository.getAllData
        sortByHighPriority = notesRepository.sortByHighPriority
        sortByLowPriority = notesRepository.sortByLowPriority
    }

    fun insertData(notesEntity: NotesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.insertData(notesEntity)
        }
    }

    fun updateData(notesEntity: NotesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.upDateData(notesEntity)
        }
    }

    fun deleteData(notesEntity: NotesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteData(notesEntity)
        }
    }

    fun deleteAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteAllData()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<NotesEntity>> {
        return notesRepository.searchDatabase(searchQuery)
    }
}