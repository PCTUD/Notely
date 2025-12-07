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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import org.androidstudio.notely.ui.navigation.NavRoutes


@Composable
fun NotelyNavHost(navController: NavHostController) {
    val context = LocalContext.current

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
                }
            )
        }

        composable(NavRoutes.Home.route) {
            HomeScreen(
                onSelectLesson = { lessonId ->
                    navController.navigate(NavRoutes.Lesson.routeWithId(lessonId))
                },
                onQuestionnaire = { navController.navigate(NavRoutes.Questionnaire.route) },
                onAccounts = { navController.navigate(NavRoutes.Accounts.route) }
            )
        }

        composable(
            route = NavRoutes.Lesson.route,
            arguments = listOf(navArgument("lessonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getInt("lessonId") ?: 1

            LessonScreen(
                lessonId = lessonId,
                onExit = { navController.popBackStack() }
            )
        }


        composable(NavRoutes.Questionnaire.route) {
            QuestionnaireScreen(
                userViewModel = userViewModel,
                onDone = { navController.popBackStack() }
            )
        }
    }
}
