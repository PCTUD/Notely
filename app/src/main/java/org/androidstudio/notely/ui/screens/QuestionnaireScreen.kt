package org.androidstudio.notely.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview


// -------------------------------------------------------------
// DATA CLASS – answers collected from the questionnaire
// -------------------------------------------------------------
data class QuestionnaireResult(
    val playedBefore: Boolean?,
    val grade4: Boolean?,
    val grade6: Boolean?,
    val grade8: Boolean?,
    val circleOfFifths: Boolean?,
    val practiceFrequency: Int?
)

// -------------------------------------------------------------
// MAIN SCREEN
// -------------------------------------------------------------
@Composable
fun QuestionnaireScreen(
    onSubmit: (QuestionnaireResult) -> Unit
) {
    // Local state for answers
    var playedBefore by remember { mutableStateOf<Boolean?>(null) }
    var grade4 by remember { mutableStateOf<Boolean?>(null) }
    var grade6 by remember { mutableStateOf<Boolean?>(null) }
    var grade8 by remember { mutableStateOf<Boolean?>(null) }
    var circleOfFifths by remember { mutableStateOf<Boolean?>(null) }
    var practiceFreq by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF6A4D))
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Notely",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                QuestionYesNo(
                    text = "Have you ever played piano before?",
                    answer = playedBefore,
                    onAnswer = { playedBefore = it }
                )
            }

            item {
                QuestionYesNo(
                    text = "Did you complete grade 4?",
                    answer = grade4,
                    onAnswer = { grade4 = it }
                )
            }

            item {
                QuestionYesNo(
                    text = "Did you complete grade 6?",
                    answer = grade6,
                    onAnswer = { grade6 = it }
                )
            }

            item {
                QuestionYesNo(
                    text = "Did you complete grade 8?",
                    answer = grade8,
                    onAnswer = { grade8 = it }
                )
            }

            item {
                QuestionYesNo(
                    text = "Do you understand the circle of fifths?",
                    answer = circleOfFifths,
                    onAnswer = { circleOfFifths = it }
                )
            }

            item {
                PracticeFrequencySelector(
                    selected = practiceFreq,
                    onSelect = { practiceFreq = it }
                )
            }

            // Submit button
            item {
                Button(
                    onClick = {
                        onSubmit(
                            QuestionnaireResult(
                                playedBefore,
                                grade4,
                                grade6,
                                grade8,
                                circleOfFifths,
                                practiceFreq
                            )
                        )
                    },
                    enabled = playedBefore != null &&
                            grade4 != null &&
                            grade6 != null &&
                            grade8 != null &&
                            circleOfFifths != null &&
                            practiceFreq != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

//--------------------------------------------------------------
// YES-NO QUESTION CARD
//--------------------------------------------------------------
@Composable
fun QuestionYesNo(
    text: String,
    answer: Boolean?,
    onAnswer: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                YesNoOption("✓", answer == true) { onAnswer(true) }
                YesNoOption("✕", answer == false) { onAnswer(false) }
            }
        }
    }
}

@Composable
fun YesNoOption(symbol: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = symbol,
        fontSize = 28.sp,
        color = if (selected) Color(0xFFFF6A4D) else Color.Black,
        modifier = Modifier.clickable { onClick() }
    )
}

//--------------------------------------------------------------
// PRACTICE FREQUENCY SELECTOR
//--------------------------------------------------------------
@Composable
fun PracticeFrequencySelector(
    selected: Int?,
    onSelect: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("How many times a week do you commit to practice?", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                (1..5).forEach { number ->
                    Text(
                        number.toString(),
                        fontSize = 24.sp,
                        color = if (selected == number) Color(0xFFFF6A4D) else Color.Black,
                        modifier = Modifier.clickable { onSelect(number) }
                    )
                }
            }
        }
    }
}

//--------------------------------------------------------------
// PREVIEW
//--------------------------------------------------------------
@Composable
@Preview(showBackground = true)
fun QuestionnairePreview() {
    QuestionnaireScreen(onSubmit = {})
}
