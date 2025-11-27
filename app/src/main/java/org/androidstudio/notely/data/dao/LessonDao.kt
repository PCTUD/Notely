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

    @Query()
    fun getAllLessons(): LiveData<List<Lesson>>

    @Query()
    suspend fun getLessonId(lessonId: Int): Lesson?
}