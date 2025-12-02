package org.androidstudio.notely.data.dao

import androidx.room.*
import org.androidstudio.notely.data.entity.LessonProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: LessonProgressEntity)

    @Query("SELECT * FROM lesson_progress WHERE exerciseId = :exerciseId")
    fun getProgressForExercise(exerciseId: Int): Flow<LessonProgressEntity?>
}
