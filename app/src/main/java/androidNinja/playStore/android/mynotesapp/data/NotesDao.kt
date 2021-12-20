package androidNinja.playStore.android.mynotesapp.data

import androidNinja.playStore.android.mynotesapp.data.models.NotesEntity
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    @Query("SELECT * FROM Notes_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<NotesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(notesEntity: NotesEntity)

    @Update
    suspend fun upDateData(notesEntity: NotesEntity)

    @Delete
    suspend fun deleteData(notesEntity: NotesEntity)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM notes_table WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<NotesEntity>>

    @Query("SELECT * FROM notes_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<NotesEntity>>

    @Query("SELECT * FROM notes_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<NotesEntity>>

}