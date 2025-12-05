package org.androidstudio.notely.data.database


import android.content.Context
import androidx.room.Room
import org.androidstudio.notely.data.NotelyDatabase

object DatabaseProvider {
    @Volatile
    private var INSTANCE: NotelyDatabase? = null

    fun get(context: Context): NotelyDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                NotelyDatabase::class.java,
                "notely.db"
            ).build().also { INSTANCE = it }
        }
}