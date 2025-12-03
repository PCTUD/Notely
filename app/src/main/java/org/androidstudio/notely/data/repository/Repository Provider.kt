package org.androidstudio.notely.data.repository

import android.content.Context
import org.androidstudio.notely.data.database.DatabaseProvider

object RepositoryProvider {

    fun userRepository(context: Context): UserRepository =
        UserRepository(DatabaseProvider.get(context).userDao())

    fun exerciseRepository(context: Context): ExerciseRepository =
        ExerciseRepository(DatabaseProvider.get(context).exerciseDao())

    fun progressRepository(context: Context): LessonProgressRepository =
        LessonProgressRepository(DatabaseProvider.get(context).progressDao())

    fun questionnaireRepository(context: Context): QuestionnaireRepository =
        QuestionnaireRepository(DatabaseProvider.get(context).questionnaireDao())
}
