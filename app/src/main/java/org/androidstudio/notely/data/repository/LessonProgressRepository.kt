package org.androidstudio.notely.data.repository

import org.androidstudio.notely.data.dao.LessonProgressDao
import org.androidstudio.notely.data.entity.LessonProgressEntity

class LessonProgressRepository(private val dao: LessonProgressDao) {

    suspend fun getProgress(userId: Int, lessonId: Int): LessonProgressEntity? {
        return dao.getProgress(userId, lessonId)
    }

    suspend fun setProgress(userId: Int, lessonId: Int, completedCount: Int) {
        dao.upsertProgress(
            LessonProgressEntity(
                userId = userId,
                lessonId = lessonId,
                completedExercisesCount = completedCount
            )
        )
    }
}
