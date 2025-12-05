package org.androidstudio.notely.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import org.androidstudio.notely.data.entity.LessonPhotoEntity

@Dao
interface LessonPhotoDao {

    // Only allow one photo per lesson -> overwrite = true
    @Insert
    suspend fun insertPhoto(photo: LessonPhotoEntity)

    @Query("SELECT * FROM lesson_photos WHERE lessonId = :lessonId LIMIT 1")
    suspend fun getPhotoForLesson(lessonId: Int): LessonPhotoEntity?

    @Query("DELETE FROM lesson_photos WHERE lessonId = :lessonId")
    suspend fun deletePhotoForLesson(lessonId: Int)

    @Delete
    suspend fun delete(photo: LessonPhotoEntity)
}
