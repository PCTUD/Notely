package org.androidstudio.notely.data.repository

import org.androidstudio.notely.data.dao.LessonProgressDao
import org.androidstudio.notely.data.entity.LessonProgressEntity

class LessonProgressRepository(private val progressDao: LessonProgressDao) {

    suspend fun insertProgress(progress: LessonProgressEntity) =
        progressDao.insertProgress(progress)

    suspend fun getProgressForExercise(exerciseId: Int) =
        progressDao.getProgressForExercise(exerciseId)

    suspend fun updateProgress(progress: LessonProgressEntity) =
        progressDao.updateProgress(progress)
}
