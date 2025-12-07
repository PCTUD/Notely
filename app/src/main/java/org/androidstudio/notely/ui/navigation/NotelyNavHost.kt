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

@Composable
fun NotelyNavHost(navController: NavHostController) {
    val context = LocalContext.current

    // Shared UserViewModel for account stuff
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory.fromContext(context)
    )

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
                onStartLesson = { navController.navigate(NavRoutes.Lesson.route) },
                onQuestionnaire = { navController.navigate(NavRoutes.Questionnaire.route) },
                onAccounts = { navController.navigate(NavRoutes.Accounts.route) }
            )
        }

        composable(NavRoutes.Lesson.route) {
            LessonScreen(onExit = { navController.popBackStack() })
        }

        composable(NavRoutes.Questionnaire.route) {
            QuestionnaireScreen { result, score ->
                userViewModel.saveQuestionnaireForCurrentUser(result, score)

                // After questionnaire, send them to Home (or back to Accounts; your choice)
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Accounts.route) { inclusive = true }
                }
            }
        }

    }
}
