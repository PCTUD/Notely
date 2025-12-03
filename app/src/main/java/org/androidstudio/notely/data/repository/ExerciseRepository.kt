package org.androidstudio.notely.data.repository

import kotlinx.coroutines.flow.Flow
import org.androidstudio.notely.data.dao.ExerciseDao
import org.androidstudio.notely.data.entity.ExerciseEntity

class ExerciseRepository(
    private val exerciseDao: ExerciseDao
) {

    suspend fun insertExercise(exercise: ExerciseEntity) {
        exerciseDao.insert(exercise)
    }

    fun getExercises(): Flow<List<ExerciseEntity>> {
        return exerciseDao.getExercises()
    }

    suspend fun updateExerciseCompletion(id: Int, completed: Boolean) {
        exerciseDao.updateCompletion(id, completed)
    }
}
