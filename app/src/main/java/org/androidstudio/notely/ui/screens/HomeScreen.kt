package org.androidstudio.notely.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import org.androidstudio.notely.ui.viewmodel.LessonProgressViewModel

/* HomeScreen: Notely title bar, link to the Accounts screen, and a 2×2 grid
of lesson cards. Each card displays lesson-specific progress loaded from
Room via LessonProgressViewModel for the current user. */

@Composable
fun HomeScreen(
    lessonProgressViewModel: LessonProgressViewModel,
    onStartLesson: (LessonType) -> Unit,
    onQuestionnaire: () -> Unit,
    onAccounts: () -> Unit
) {
    // Per-lesson progress state (0f..1f)
    var melodyProgress by remember { mutableStateOf(0f) }
    var harmonyProgress by remember { mutableStateOf(0f) }
    var chordsProgress by remember { mutableStateOf(0f) }
    var scalesProgress by remember { mutableStateOf(0f) }

    // Load progress from Room when the screen first appears
    LaunchedEffect(Unit) {
        melodyProgress = lessonProgressViewModel.getProgress(LessonType.MELODY.lessonId)
        harmonyProgress = lessonProgressViewModel.getProgress(LessonType.HARMONY.lessonId)
        chordsProgress = lessonProgressViewModel.getProgress(LessonType.CHORDS.lessonId)
        scalesProgress = lessonProgressViewModel.getProgress(LessonType.SCALES.lessonId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App title header
        Text(
            text = "Notely",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color(0xFFFF6A4D)
        )

        // Top row: app title + Accounts link
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Small Notely label (left)
            Text(
                text = "Notely",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6A4D)
            )

            // Accounts navigation (right)
            Text(
                text = "Accounts",
                modifier = Modifier.clickable { onAccounts() },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6A4D)
            )
        }

        // Row 1: Melody + Harmony cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ExerciseCard(
                title = "Open this melody exercise in order to continue your melody learning journey",
                progress = melodyProgress,
                icon = "♪",
                onClick = { onStartLesson(LessonType.MELODY) } // Open melody lesson
            )

            ExerciseCard(
                title = "Begin your Harmony exercise",
                progress = harmonyProgress,
                icon = "𝄞",
                onClick = { onStartLesson(LessonType.HARMONY) } // Open harmony lesson
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Row 2: Chords + Scales cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ExerciseCard(
                title = "Begin your chord exercise",
                progress = chordsProgress,
                icon = "♬",
                onClick = { onStartLesson(LessonType.CHORDS) } // Open chords lesson
            )

            ExerciseCard(
                title = "Begin your scale exercise",
                progress = scalesProgress,
                icon = "♩",
                onClick = { onStartLesson(LessonType.SCALES) } // Open scales lesson
            )
        }
    }
}

/* Lesson tile card.
Reusable composable that displays a single lesson tile with an icon, a short
description, and a red progress bar whose fill is driven by [progress].*/
@Composable
private fun ExerciseCard(
    title: String,
    progress: Float,
    icon: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(220.dp)
            .clickable { onClick() }, // Navigate to chosen lesson
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.Gray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Lesson icon
            Text(
                text = icon,
                fontSize = 32.sp,
                color = Color(0xFFFF6A4D),
                modifier = Modifier.padding(top = 8.dp)
            )

            // Lesson description
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp),
                lineHeight = 14.sp
            )

            // Progress bar container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(bottom = 8.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            ) {
                // Red progress segment
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress) // Width proportional to progress 0f..1f
                        .padding(1.dp)
                        .background(Color(0xFFFF6A4D))
                )
            }
        }
    }
}