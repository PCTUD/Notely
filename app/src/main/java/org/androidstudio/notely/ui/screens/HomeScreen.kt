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

@Composable
fun HomeScreen(
    lessonProgressViewModel: LessonProgressViewModel,
    onStartLesson: (LessonType) -> Unit,
    onQuestionnaire: () -> Unit,
    onAccounts: () -> Unit
) {
    // One progress value per lesson
    var melodyProgress by remember { mutableStateOf(0f) }
    var harmonyProgress by remember { mutableStateOf(0f) }
    var chordsProgress by remember { mutableStateOf(0f) }
    var scalesProgress by remember { mutableStateOf(0f) }

    // Load from DB when the screen first shows
    LaunchedEffect(Unit) {
        melodyProgress = lessonProgressViewModel.getProgress(LessonType.MELODY.lessonId)
        harmonyProgress = lessonProgressViewModel.getProgress(LessonType.HARMONY.lessonId)
        chordsProgress = lessonProgressViewModel.getProgress(LessonType.CHORDS.lessonId)
        scalesProgress = lessonProgressViewModel.getProgress(LessonType.SCALES.lessonId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Notely",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 32.dp),
            color = Color(0xFFFF6A4D)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notely",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Accounts",
                modifier = Modifier.clickable { onAccounts() },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ExerciseCard(
                title = "Open this melody exercise in order to continue your melody learning journey",
                progress = melodyProgress,
                icon = "♪",
                onClick = { onStartLesson(LessonType.MELODY) }
            )

            ExerciseCard(
                title = "Begin your Harmony exercise",
                progress = harmonyProgress,
                icon = "𝄞",
                onClick = { onStartLesson(LessonType.HARMONY) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ExerciseCard(
                title = "Begin your chord exercise",
                progress = chordsProgress,
                icon = "🎵",
                onClick = { onStartLesson(LessonType.CHORDS) }
            )

            ExerciseCard(
                title = "Begin your scale exercise",
                progress = scalesProgress,
                icon = "♩",
                onClick = { onStartLesson(LessonType.SCALES) }
            )
        }
    }
}

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
            .clickable { onClick() },
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
            Text(
                text = icon,
                fontSize = 32.sp,
                color = Color(0xFFFF6A4D),
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp),
                lineHeight = 14.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(bottom = 8.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .padding(1.dp)
                        .background(Color(0xFFFF6A4D))
                )
            }
        }
    }
}