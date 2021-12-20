package androidNinja.playStore.android.mynotesapp.data

import android.content.Context
import androidNinja.playStore.android.mynotesapp.data.models.NotesEntity
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [NotesEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun getNotesDao(): NotesDao

    companion object{

        @Volatile
        var INSTANCE: NotesDatabase? = null

        fun getDataBaseInstance(context: Context): NotesDatabase {

            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val roomDatabaseInstance = Room.databaseBuilder(
                    context,
                    NotesDatabase::class.java,
                    "Notes_table"
                ).build()
                INSTANCE = roomDatabaseInstance
                return roomDatabaseInstance
            }
        }
    }
}