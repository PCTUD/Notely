package org.androidstudio.notely.data.repository

import android.content.Context
import org.androidstudio.notely.data.database.DatabaseProvider
import org.androidstudio.notely.data.repository.*
import org.androidstudio.notely.data.dao.*



object RepositoryProvider {

    fun userRepository(context: Context): UserRepository =
        UserRepository(DatabaseProvider.get(context).userDao())

    fun exerciseRepository(context: Context): ExerciseRepository =  // Note: This function will likely be deleted
        ExerciseRepository(DatabaseProvider.get(context).exerciseDao())

    fun progressRepository(context: Context): LessonProgressRepository = // Note: This function will likely be deleted
        LessonProgressRepository(DatabaseProvider.get(context).lessonProgressDao())

    fun questionnaireRepository(context: Context): QuestionnaireRepository =
        QuestionnaireRepository(DatabaseProvider.get(context).questionnaireDao())
}
