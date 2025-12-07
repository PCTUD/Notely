package org.androidstudio.notely.ui.navigation


sealed class NavRoutes(val route: String) {
    object Accounts : NavRoutes("accounts")
    object Home : NavRoutes("home")
    object Lesson : NavRoutes("lesson")
    object Questionnaire : NavRoutes("questionnaire")
    }