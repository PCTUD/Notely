package org.androidstudio.notely.data.repository

import androidx.lifecycle.LiveData
import org.androidstudio.notely.data.dao.LessonDao
import org.androidstudio.notely.data.model.Lesson


class LessonRepository(private val lessonDao: LessonDao) {

    val allLessons: LiveData<List<Lesson>> = lessonDao.getAllLessons()

    suspend fun insert(lesson: Lesson) {
        lessonDao.insert(lesson)
    }

    suspend fun update(lesson: Lesson) {
        lessonDao.update(lesson)
    }

    suspend fun delete(lesson: Lesson) {
        lessonDao.delete(lesson)
    }

    suspend fun getLesson(id: Int): Lesson? {
        return lessonDao.getLessonById(id)
    }
}
