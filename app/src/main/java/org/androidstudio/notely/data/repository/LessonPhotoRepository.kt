package org.androidstudio.notely.data.repository

import org.androidstudio.notely.data.dao.LessonPhotoDao
import org.androidstudio.notely.data.entity.LessonPhotoEntity

class LessonPhotoRepository(
    private val dao: LessonPhotoDao
) {

    suspend fun setLessonPhoto(lessonId: Int, uri: String) {
        // Always replace existing photo
        dao.deletePhotoForLesson(lessonId)
        dao.insertPhoto(
            LessonPhotoEntity(
                lessonId = lessonId,
                uri = uri
            )
        )
    }

    suspend fun getLessonPhoto(lessonId: Int): LessonPhotoEntity? =
        dao.getPhotoForLesson(lessonId)

    suspend fun removeLessonPhoto(lessonId: Int) =
        dao.deletePhotoForLesson(lessonId)
}
