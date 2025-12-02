package org.androidstudio.notely.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import org.androidstudio.notely.data.dao.ExerciseDao
import org.androidstudio.notely.data.dao.LessonProgressDao
import org.androidstudio.notely.data.dao.QuestionnaireDao
import org.androidstudio.notely.data.dao.UserDao
import org.androidstudio.notely.data.entity.LessonProgressEntity
import org.androidstudio.notely.data.entity.ExerciseEntity
import org.androidstudio.notely.data.entity.QuestionnaireResponseEntity
import org.androidstudio.notely.data.entity.UserEntity

@Database(
    entities = [
        ExerciseEntity::class,
        UserEntity::class,
        LessonProgressEntity::class,
        QuestionnaireResponseEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NotelyDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun userDao(): UserDao
    abstract fun progressDao(): LessonProgressDao
    abstract fun questionnaireDao(): QuestionnaireDao
}