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
import kotlinx.coroutines.launch
import org.androidstudio.notely.ui.viewmodel.UserViewModel



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
    userViewModel: UserViewModel,
    onDone: () -> Unit
) {
    val scope = rememberCoroutineScope()

    // Make sure we know who the active user is
    LaunchedEffect(Unit) {
        userViewModel.loadActiveUser()
    }

    val currentUserId by userViewModel.currentUserId.collectAsState()

    // 1..5 slider for experience
    var sliderValue by remember { mutableStateOf(2f) }

    val label = remember(sliderValue) {
        when (sliderValue.toInt()) {
            1 -> "Complete beginner"
            2 -> "Some experience"
            3 -> "Comfortable"
            4 -> "Confident"
            else -> "Advanced"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quick questionnaire",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "How confident are you with music theory?",
            style = MaterialTheme.typography.bodyLarge
        )

        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 1f..5f,
            steps = 3, // 5 positions total
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    val id = currentUserId
                    if (id != null) {
                        userViewModel.setExperienceLabel(id, label)
                    }
                    onDone()
                }
            }
        ) {
            Text("Save and go back")
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


