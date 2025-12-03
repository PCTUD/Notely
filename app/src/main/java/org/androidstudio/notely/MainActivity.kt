package org.androidstudio.notely

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.androidstudio.notely.ui.screens.HomeScreen
import org.androidstudio.notely.ui.screens.LessonDetailScreen
import org.androidstudio.notely.ui.screens.QuestionnaireScreen
import org.androidstudio.notely.ui.theme.NotelyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotelyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotelyApp()
                }
            }
        }
    }
}

@Composable
fun NotelyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "questionnaire"
    ) {

        composable("questionnaire") {
            QuestionnaireScreen(
                onComplete = { navController.navigate("home") }
            )
        }

        composable("home") {
            HomeScreen(
                onOpenLesson = { navController.navigate("lesson") }
            )
        }

        composable("lesson") {
            LessonScreen()
        }
    }
}
