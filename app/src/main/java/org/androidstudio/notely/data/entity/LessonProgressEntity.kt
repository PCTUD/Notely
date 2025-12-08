package org.androidstudio.notely.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,        // NEW
    val lessonId: Int,
    val completedExercisesCount: Int = 0 // 0–10
)
