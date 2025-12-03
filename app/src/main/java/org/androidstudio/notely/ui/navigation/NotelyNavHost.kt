package org.androidstudio.notely.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.androidstudio.notely.ui.screens.HomeScreen
import org.androidstudio.notely.ui.screens.QuestionnaireScreen
import org.androidstudio.notely.ui.screens.LessonScreen

@Composable
fun NotelyNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.QUESTIONNAIRE // first-time flow
    ) {

        composable(NavRoutes.QUESTIONNAIRE) {
            QuestionnaireScreen(
                onSubmit = {
                    // After saving the result, go to home screen
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.QUESTIONNAIRE) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.HOME) {
            HomeScreen(
                onSelectLesson = {
                    navController.navigate(NavRoutes.LESSON)
                }
            )
        }

        composable(NavRoutes.LESSON) {
            LessonScreen(
                onExit = {
                    navController.popBackStack()
                }
            )
        }
    }
}
