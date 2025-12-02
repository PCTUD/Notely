package org.androidstudio.notely.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.androidstudio.notely.data.model.Lesson

@Dao
interface LessonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lesson: Lesson)

    @Update
    suspend fun update(lesson: Lesson)

    @Delete
    suspend fun delete(lesson: Lesson)

    @Query("SELECT * FROM lessons ORDER BY id DESC")
    fun getAllLessons(): LiveData<List<Lesson>>

    @Query("SELECT * FROM lessons WHERE id = :lessonID LIMIT 1")
    suspend fun getLessonId(lessonId: Int): Lesson?
}