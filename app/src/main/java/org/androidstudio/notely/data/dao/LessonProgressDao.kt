package org.androidstudio.notely.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.androidstudio.notely.data.entity.LessonProgressEntity

@Dao
interface LessonProgressDao {

    // Insert or update (if conflict)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: LessonProgressEntity)

    @Update
    suspend fun update(progress: LessonProgressEntity)

    @Delete
    suspend fun delete(progress: LessonProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: LessonProgressEntity)

    // Retrieve a single progress record for user + lesson
    @Query(
        """
        SELECT * FROM lesson_progress
        WHERE userId = :userId AND lessonId = :lessonId
        ORDER BY id DESC
        LIMIT 1
    """
    )
    suspend fun getProgress(userId: Int, lessonId: Int): LessonProgressEntity?

}