package org.androidstudio.notely.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


//Lesson Table
@Entity(tableName = "lessons")
data class Lesson(
    @PrimaryKey(autogenerate = true)

    val id: Int = 0,
    val title: String,
    val level: String,
    val progress: Int = 0
)