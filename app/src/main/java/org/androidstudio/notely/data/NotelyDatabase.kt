package org.androidstudio.notely.data

import androidx.room.Database
import androidx.room.RoomDatabase
import org.androidstudio.notely.data.dao.ExerciseDao
import org.androidstudio.notely.data.dao.LessonPhotoDao
import org.androidstudio.notely.data.dao.LessonProgressDao
import org.androidstudio.notely.data.dao.QuestionnaireDao
import org.androidstudio.notely.data.dao.UserDao
import org.androidstudio.notely.data.entity.ExerciseEntity
import org.androidstudio.notely.data.entity.LessonPhotoEntity
import org.androidstudio.notely.data.entity.LessonProgressEntity
import org.androidstudio.notely.data.entity.QuestionnaireResponseEntity
import org.androidstudio.notely.data.entity.UserEntity

@Database(
    entities = [
        ExerciseEntity::class,
        LessonProgressEntity::class,
        QuestionnaireResponseEntity::class,
        UserEntity::class,
        LessonPhotoEntity::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class NotelyDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun lessonProgressDao(): LessonProgressDao
    abstract fun questionnaireDao(): QuestionnaireDao
    abstract fun userDao(): UserDao
    abstract fun lessonPhotoDao(): LessonPhotoDao   // <-- only ONE photo DAO method
}
