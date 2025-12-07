package org.androidstudio.notely.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.border
import androidx.compose.ui.layout.ContentScale


@Composable
fun LessonScreen(
    onExit: () -> Unit
) {
    // ---- Local state for the chosen photo ----
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // ---- ActivityResultLauncher for picking an image from gallery ----
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        // called when the user picks an image (or cancels)
        if (uri != null) {
            photoUri = uri
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ===== Main lesson content =====
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // --- Header Row ---
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
                    text = "Melodies 1",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Musical Direction",
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- Piano Keys (your existing composable) ---
            PianoKeyboard()

            Spacer(modifier = Modifier.height(24.dp))

            // --- Button to add / replace lesson photo ---
            Button(
                onClick = { photoPickerLauncher.launch("image/*") }
            ) {
                Text(text = if (photoUri == null) "Add lesson photo" else "Change lesson photo")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Progress Bar (your existing composable) ---
            LessonProgressBar(progress = 0.6f)
        }

        // ===== Floating, draggable photo overlay =====
        FloatingLessonPhoto(photoUri = photoUri)
    }
}

/*
@Composable
//private fun FloatingLessonPhoto(
    photoUri: Uri?
) {
    if (photoUri == null) return

    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    Box(
        modifier = Modifier
            .size(140.dp)
            // Start somewhere in the bottom-right-ish area
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .align(Alignment.BottomEnd) // starting anchor
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.2f))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
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
*/

@Composable
fun PianoKeyboard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {

        // --- White Keys Row ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            repeat(7) {
                WhiteKey()
            }
        }

        // --- Black Keys Row ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(22.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 6.dp)
        ) {
            BlackKey()
            BlackKey()
            Spacer(modifier = Modifier.width(40.dp)) // gap between E/F
            BlackKey()
            BlackKey()
            BlackKey()
        }
    }
}

@Composable
fun WhiteKey() {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(180.dp)
            .background(Color.White)
            .border(2.dp, Color.Black)
    )
}

@Composable
fun BlackKey() {
    Box(
        modifier = Modifier
            .width(32.dp)
            .height(120.dp)
            .background(Color.Black, shape = RoundedCornerShape(4.dp))
    )
}

@Composable
fun LessonProgressBar(progress: Float) {
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
private fun FloatingLessonPhoto(
    photoUri: Uri?
) {
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

