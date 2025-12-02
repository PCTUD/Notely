package org.androidstudio.notely.data.database


import android.content.Context
import androidx.room.Room

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