package androidNinja.playStore.android.mynotesapp.data

import androidNinja.playStore.android.mynotesapp.data.models.Priority
import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }
}