package org.androidstudio.notely.data.repository

import org.androidstudio.notely.data.dao.LessonProgressDao
import org.androidstudio.notely.data.entity.LessonProgressEntity

class LessonProgressRepository(
    private val dao: LessonProgressDao
) {
    suspend fun save(progress: LessonProgressEntity) {
        dao.upsertProgress(progress)
    }

    fun getProgress(exerciseId: Int) =
        dao.getProgressForExercise(exerciseId)
}

