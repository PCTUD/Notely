package org.androidstudio.notely.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlin.math.roundToInt
import androidx.compose.foundation.clickable

// Simple enum so we can mix & match the three exercise UIs
private enum class ExerciseType { MELODY, RHYTHM, INTERVALS }

// Static definitions: 4 lessons, each reusing the 3 exercises in a different order
private val lessonDefinitions: Map<Int, List<ExerciseType>> = mapOf(
    1 to listOf(ExerciseType.MELODY, ExerciseType.RHYTHM, ExerciseType.INTERVALS),
    2 to listOf(ExerciseType.RHYTHM, ExerciseType.INTERVALS, ExerciseType.MELODY),
    3 to listOf(ExerciseType.INTERVALS, ExerciseType.MELODY, ExerciseType.RHYTHM),
    4 to listOf(ExerciseType.RHYTHM, ExerciseType.MELODY, ExerciseType.INTERVALS)
)

@Composable
private fun MelodyExerciseContent() {
    Text(
        text = "Play the melody on the keyboard below.",
        style = MaterialTheme.typography.bodyLarge
    )

    Spacer(modifier = Modifier.height(16.dp))
    PianoKeyboard()
}

@Composable
private fun RhythmExerciseContent() {
    Text(
        text = "Tap the rhythm by clapping or tapping the beat.",
        style = MaterialTheme.typography.bodyLarge
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Simple placeholder UI
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("1  2  3  4  | 1  2  3  4")
        Text("♩ ♪ ♪ ♩   | ♩ ♩ ♪ ♪")
    }
}

@Composable
private fun IntervalsExerciseContent() {
    Text(
        text = "Listen for the interval and identify it.",
        style = MaterialTheme.typography.bodyLarge
    )

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Example questions:")
        Text("• Is this a major or minor third?")
        Text("• Is this interval going up or down?")
    }
}


@Composable
fun LessonScreen(
    lessonId: Int,
    onExit: () -> Unit
) {
    val exercises = lessonDefinitions[lessonId] ?: lessonDefinitions[1]!!
    var currentIndex by remember { mutableStateOf(0) }

    // ---- photo picker state (shared for all 3 parts) ----
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) photoUri = uri
    }

    val part = exercises[currentIndex]
    val progressFraction = (currentIndex + 0) / 3f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "←",
                    fontSize = 32.sp,
                    modifier = Modifier
                        .clickable { onExit() }
                        .padding(end = 12.dp)
                )

                Text(
                    text = "Lesson $lessonId • Part ${currentIndex + 1} / 3",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = when (part) {
                    ExerciseType.MELODY -> "Melody exercise"
                    ExerciseType.RHYTHM -> "Rhythm exercise"
                    ExerciseType.INTERVALS -> "Intervals exercise"
                },
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // MAIN EXERCISE CONTENT
            when (part) {
                ExerciseType.MELODY -> MelodyExerciseContent()
                ExerciseType.RHYTHM -> RhythmExerciseContent()
                ExerciseType.INTERVALS -> IntervalsExerciseContent()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // pick / change photo
            Button(onClick = { photoPickerLauncher.launch("image/*") }) {
                Text(if (photoUri == null) "Add lesson photo" else "Change lesson photo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LessonProgressBar(progress = (progressFraction))

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation between parts
            Button(
                onClick = {
                    if (currentIndex < exercises.lastIndex) {
                        currentIndex++
                    } else {
                        onExit()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(if (currentIndex < exercises.lastIndex) "Next part" else "Finish lesson")
            }
        }

        // Floating draggable photo overlay
        FloatingLessonPhoto(photoUri = photoUri)
    }

}

@Composable
private fun LessonProgressBar(progress: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Lesson progress",
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(Color(0xFFFF6A4D), RoundedCornerShape(4.dp))
            )
        }
    }
}


@Composable
private fun PianoKeyboard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // White keys row
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            repeat(7) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(180.dp)
                        .background(Color.White)
                        .border(2.dp, Color.Black)
                )
            }
        }

        // Black keys row (simple, stylised)
        Row(
            horizontalArrangement = Arrangement.spacedBy(22.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 6.dp)
        ) {
            @Composable
            fun blackKey() = Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(120.dp)
                    .background(Color.Black, shape = RoundedCornerShape(4.dp))
            )

            blackKey()
            blackKey()
            Spacer(modifier = Modifier.width(40.dp)) // E–F gap
            blackKey()
            blackKey()
            blackKey()
        }
    }
}

@Composable
private fun FloatingLessonPhoto(photoUri: Uri?) {
    if (photoUri == null) return

    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    Box(
        modifier = Modifier
            .size(140.dp)
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.2f))
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    offset += dragAmount
                }
            }
            .padding(2.dp)
    ) {
        AsyncImage(
            model = photoUri,
            contentDescription = "Lesson photo",
            modifier = Modifier.fillMaxSize()
        )
    }
}
