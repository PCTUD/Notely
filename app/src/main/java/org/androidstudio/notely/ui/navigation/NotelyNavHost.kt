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


/* NotelyNavHost: central navigation graph for the app. Wires up
Account, Home, Lesson, and Questionnaire screens, sharing the
UserViewModel and LessonProgressViewModel across destinations. */

@Composable
fun NotelyNavHost(navController: NavHostController) {
    val context = LocalContext.current

    // Shared UserViewModel (accounts, ability scores, active user)
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory.fromContext(context))

    // Shared LessonProgressViewModel (per-user, per-lesson progress)
    val lessonProgressViewModel: LessonProgressViewModel = viewModel(
        factory = LessonProgressViewModel.fromContext(context))

    // Remember which lesson tile was tapped on Home
    val selectedLessonType = remember { mutableStateOf(LessonType.MELODY) }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Accounts.route)
    {
        // Accounts → choose or create user profile
        composable(NavRoutes.Accounts.route) {
            AccountScreen(
                viewModel = userViewModel,
                onContinue = {
                    // After selecting an account, go to Home and clear back stack
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Accounts.route) { inclusive = true }
                    }
                },
                onNewAccountCreated = {
                    // After creating a new account, go to questionnaire
                    navController.navigate(NavRoutes.Questionnaire.route)
                }
            )
        }

        // Home → lesson hub + navigation to Accounts / Questionnaire / Lesson
        composable(NavRoutes.Home.route) {
            HomeScreen(
                lessonProgressViewModel = lessonProgressViewModel,
                onStartLesson = { lessonType ->
                    // Store selected lesson and navigate to Lesson screen
                    selectedLessonType.value = lessonType
                    navController.navigate(NavRoutes.Lesson.route)
                },
                onQuestionnaire = { navController.navigate(NavRoutes.Questionnaire.route) },
                onAccounts = { navController.navigate(NavRoutes.Accounts.route) }
            )
        }

        // Lesson → shows the currently selected LessonType
        composable(NavRoutes.Lesson.route) {
            LessonScreen(
                lessonType = selectedLessonType.value,
                progressViewModel = lessonProgressViewModel,
                onExit = { navController.popBackStack() } // Back to previous screen
            )
        }

        // Questionnaire → compute and save ability score for active user
        composable(NavRoutes.Questionnaire.route) {
            QuestionnaireScreen { result, score ->
                userViewModel.saveQuestionnaireForCurrentUser(result, score)
                // After questionnaire, go to Home and remove Accounts from back stack
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Accounts.route) { inclusive = true }
                }
            }
        }
    }
}
