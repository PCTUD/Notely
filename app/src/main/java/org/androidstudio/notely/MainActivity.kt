package org.androidstudio.notely

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import org.androidstudio.notely.ui.navigation.NotelyNavHost
import org.androidstudio.notely.ui.theme.NotelyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotelyTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navigation given to NotelyNavHost
                    val navController = rememberNavController()
                    NotelyNavHost(navController)
                }
            }
        }
    }
}