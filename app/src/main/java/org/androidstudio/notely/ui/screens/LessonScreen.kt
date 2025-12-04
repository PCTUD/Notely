package org.androidstudio.notely.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LessonScreen(
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Musical Direction",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(28.dp))

        // --- Piano Keys ---
        PianoKeyboard()

        Spacer(modifier = Modifier.height(32.dp))

        // --- Progress Bar ---
        LessonProgressBar(progress = 0.6f)
    }
}

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
