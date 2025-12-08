package org.androidstudio.notely.data.repository

import org.androidstudio.notely.data.dao.LessonProgressDao
import org.androidstudio.notely.data.entity.LessonProgressEntity

class LessonProgressRepository(private val dao: LessonProgressDao) {

    suspend fun getProgress(userId: Int, lessonId: Int): LessonProgressEntity? {
        return dao.getProgress(userId, lessonId)
    }

    suspend fun setProgress(userId: Int, lessonId: Int, completedCount: Int) {
        // Check if already have a row for the user + lesson
        val existing = dao.getProgress(userId, lessonId)

        val entity = if (existing == null) {
            LessonProgressEntity(
                userId = userId,
                lessonId = lessonId,
                completedExercisesCount = completedCount
            )
        } else {
            existing.copy(completedExercisesCount = completedCount)
        }

        // insert() uses OnConflictStrategy.REPLACE on the primary key (id),
        // this will update the row if it already exists.
        dao.insert(entity)
    }
}
