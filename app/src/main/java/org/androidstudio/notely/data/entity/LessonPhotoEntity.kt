package org.androidstudio.notely.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson_photos")
data class LessonPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lessonId: Int,
    val uri: String          // Persist URI to photo (gallery or camera)
)
