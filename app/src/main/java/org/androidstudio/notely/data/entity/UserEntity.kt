package org.androidstudio.notely.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val emoji: String,
    val abilityScore: Double? = null,
    val currentLessonId: Int? = null //might remove
)
