package org.androidstudio.notely.ui.navigation

sealed class NavRoutes(val route: String) {
    object Accounts : NavRoutes("accounts")
    object Home : NavRoutes("home")

    // route has a placeholder for lessonId
    object Lesson : NavRoutes("lesson/{lessonId}") {
        fun routeWithId(id: Int) = "lesson/$id"
    }

    object Questionnaire : NavRoutes("questionnaire")
}
