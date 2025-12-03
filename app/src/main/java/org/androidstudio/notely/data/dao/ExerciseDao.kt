// interacts with the database to manipulate exercise data

package org.androidstudio.notely.data.dao

import androidx.room.*
import org.androidstudio.notely.data.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: ExerciseEntity)

    @Query("SELECT * FROM exercises")
    fun getExercises(): Flow<List<ExerciseEntity>>

    @Query("UPDATE exercises SET isCompleted = :completed WHERE id = :id")
    suspend fun updateCompletion(id: Int, completed: Boolean)
}
