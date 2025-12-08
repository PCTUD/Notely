package org.androidstudio.notely.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.androidstudio.notely.ui.screens.HomeScreen
import org.androidstudio.notely.ui.screens.LessonScreen
import org.androidstudio.notely.ui.screens.QuestionnaireScreen
import org.androidstudio.notely.ui.screens.AccountScreen
import org.androidstudio.notely.ui.viewmodel.UserViewModel
import org.androidstudio.notely.ui.viewmodel.UserViewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.androidstudio.notely.ui.screens.LessonType
import org.androidstudio.notely.ui.viewmodel.LessonProgressViewModel
import org.androidstudio.notely.ui.viewmodel.LessonProgressViewModelFactory


@Composable
fun NotelyNavHost(navController: NavHostController) {
    val context = LocalContext.current

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory.fromContext(context)
    )

    val lessonProgressViewModel: LessonProgressViewModel = viewModel(
        factory = LessonProgressViewModel.fromContext(context)
    )

    // Which lesson card did we tap?
    val selectedLessonType = remember { mutableStateOf(LessonType.MELODY) }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Accounts.route
    ) {
        composable(NavRoutes.Accounts.route) {
            AccountScreen(
                viewModel = userViewModel,
                onContinue = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Accounts.route) { inclusive = true }
                    }
                },
                onNewAccountCreated = {
                    navController.navigate(NavRoutes.Questionnaire.route)
                }
            )
        }

        composable(NavRoutes.Home.route) {
            HomeScreen(
                lessonProgressViewModel = lessonProgressViewModel,
                onStartLesson = { lessonType ->
                    selectedLessonType.value = lessonType
                    navController.navigate(NavRoutes.Lesson.route)
                },
                onQuestionnaire = { navController.navigate(NavRoutes.Questionnaire.route) },
                onAccounts = { navController.navigate(NavRoutes.Accounts.route) }
            )
        }

        composable(NavRoutes.Lesson.route) {
            LessonScreen(
                lessonType = selectedLessonType.value,
                progressViewModel = lessonProgressViewModel,
                onExit = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Questionnaire.route) {
            QuestionnaireScreen { result, score ->
                userViewModel.saveQuestionnaireForCurrentUser(result, score)
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Accounts.route) { inclusive = true }
                }
            }
        }
    }
}
