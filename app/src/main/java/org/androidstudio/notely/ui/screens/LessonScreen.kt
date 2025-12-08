package org.androidstudio.notely.ui.screens

import android.media.SoundPool
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlin.math.roundToInt
import org.androidstudio.notely.R
import org.androidstudio.notely.ui.viewmodel.LessonProgressViewModel
import androidx.compose.ui.graphics.Color.Companion.Black

/* LessonScreen: shows a single lesson with a back arrow, lesson title,
a playable piano keyboard, a gallery-picked lesson photo and a red
progress bar. Progress is stored per-user, per-lesson via Room and
updated each time the user plays notes. */

@Composable
fun LessonScreen(
    lessonType: LessonType,
    progressViewModel: LessonProgressViewModel,
    onExit: () -> Unit
) {
    val context = LocalContext.current

    // SoundPool: load short note samples
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(4)
            .build()
    }

    // Note name → loaded sound id
    val noteSounds = remember {
        mapOf(
            "C4" to soundPool.load(context, R.raw.c4, 1),
            "C#4" to soundPool.load(context, R.raw.cs4, 1),
            "D4" to soundPool.load(context, R.raw.d4, 1),
            "D#4" to soundPool.load(context, R.raw.ds4, 1),
            "E4" to soundPool.load(context, R.raw.e4, 1),
            "F4" to soundPool.load(context, R.raw.f4, 1),
            "F#4" to soundPool.load(context, R.raw.fs4, 1),
            "G4" to soundPool.load(context, R.raw.g4, 1),
            "G#4" to soundPool.load(context, R.raw.gs4, 1),
            "A5" to soundPool.load(context, R.raw.a5, 1),
            "A#5" to soundPool.load(context, R.raw.as5, 1),
            "B5" to soundPool.load(context, R.raw.b5, 1)
        )
    }

    // Release SoundPool when composable leaves
    DisposableEffect(Unit) {
        onDispose { soundPool.release() }
    }

    // Progress (0f..1f) backed by Room
    var progress by remember { mutableStateOf(0f) }

    // Load saved progress for this lesson
    LaunchedEffect(lessonType) {
        progress = progressViewModel.getProgress(lessonType.lessonId)
    }

    // Play a note + bump progress
    fun playNote(note: String) {
        noteSounds[note]?.let { id ->
            soundPool.play(id, 1f, 1f, 1, 0, 1f)
        }
        val newProgress = (progress + 0.05f).coerceAtMost(1f)
        progress = newProgress
        progressViewModel.setProgress(lessonType.lessonId, newProgress)
    }

    // Photo Uri for lesson overlay
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Gallery picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) photoUri = uri
    }

    // Main lesson layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Back arrow + title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "←",
                    fontSize = 32.sp,
                    modifier = Modifier
                        .clickable { onExit() }
                        .padding(end = 12.dp),
                    color = Black
                )

                Text(
                    text = lessonType.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold, color = Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Lesson subtitle
            Text(
                text = lessonType.subtitle,
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Piano keyboard interaction
            PianoKeyboard(onKeyPressed = ::playNote)

            Spacer(modifier = Modifier.height(24.dp))

            // Add or change lesson photo
            Button(onClick = { photoPickerLauncher.launch("image/*") }) {
                Text(text = if (photoUri == null) "Add lesson photo" else "Change lesson photo")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Progress bar linked to Room
            LessonProgressBar(progress = progress)
        }

        // Draggable lesson photo overlay
        FloatingLessonPhoto(photoUri = photoUri)
    }
}

// ---------- Piano keyboard composables ----------

/* PianoKeyboard: one-octave keyboard with clickable white and black keys.
Each key calls [onKeyPressed] with a note name when tapped. */
@Composable
fun PianoKeyboard(onKeyPressed: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // White keys: C D E F G A B
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            WhiteKey { onKeyPressed("C4") }
            WhiteKey { onKeyPressed("D4") }
            WhiteKey { onKeyPressed("E4") }
            WhiteKey { onKeyPressed("F4") }
            WhiteKey { onKeyPressed("G4") }
            WhiteKey { onKeyPressed("A5") } // note sample naming
            WhiteKey { onKeyPressed("B5") }
        }

        // Black keys: C#, D#,   F#, G#, A#
        Row(
            horizontalArrangement = Arrangement.spacedBy(22.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 6.dp)
        ) {
            BlackKey { onKeyPressed("C#4") }
            BlackKey { onKeyPressed("D#4") }
            Spacer(modifier = Modifier.width(40.dp)) // gap between E/F
            BlackKey { onKeyPressed("F#4") }
            BlackKey { onKeyPressed("G#4") }
            BlackKey { onKeyPressed("A#5") }
        }
    }
}

/* WhiteKey: single white piano key box. */
@Composable
fun WhiteKey(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(180.dp)
            .background(Color.White)
            .border(2.dp, Color.Black)
            .clickable { onClick() }
    )
}

/* BlackKey: single black piano key box. */
@Composable
fun BlackKey(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(32.dp)
            .height(120.dp)
            .background(Color.Black, shape = RoundedCornerShape(4.dp))
            .clickable { onClick() }
    )
}

// ---------- Progress bar & floating photo ----------

/* LessonProgressBar: labelled red progress bar for the current lesson. */
@Composable
fun LessonProgressBar(progress: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            color = Black,
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

/* FloatingLessonPhoto: draggable lesson image overlay chosen from gallery. */
@Composable
private fun FloatingLessonPhoto(photoUri: Uri?) {
    if (photoUri == null) return

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.2f))
            .pointerInput(Unit) {
                // Drag gesture updates overlay offset
                detectDragGestures { _, dragAmount ->
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        AsyncImage(
            model = photoUri,
            contentDescription = "Lesson Photo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}