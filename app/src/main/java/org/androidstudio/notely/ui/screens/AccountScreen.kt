package org.androidstudio.notely.ui.screens

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.androidstudio.notely.data.entity.UserEntity
import org.androidstudio.notely.data.repository.RandomEmojiProvider
import org.androidstudio.notely.ui.viewmodel.UserViewModel


@Composable
fun AccountScreen(
    viewModel: UserViewModel,
    onContinue: () -> Unit,             //select existing account
    onNewAccountCreated: () -> Unit     //create new account
) {
    val scope = rememberCoroutineScope()

    // Load current active user on entry
    LaunchedEffect(Unit) {
        viewModel.loadActiveUser()
    }

    val users by viewModel
        .getAllUsers()
        .collectAsState(initial = emptyList<UserEntity>())

    val currentUserId by viewModel.currentUserId.collectAsState()

    var name by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf(RandomEmojiProvider.randomEmoji()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Who’s playing today?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Existing users
        if (users.isNotEmpty()) {
            Text(
                text = "Choose an account:",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(users) { user ->
                    AccountRow(
                        user = user,
                        isActive = user.id == currentUserId,
                        onClick = {
                            scope.launch {
                                viewModel.setActiveUser(user.id, user.name)
                                onContinue()
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // New account
        Text(
            text = "Create a new account",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Emoji: $emoji", style = MaterialTheme.typography.bodyLarge)

            Button(onClick = { emoji = RandomEmojiProvider.randomEmoji() }) {
                Text("Shuffle")
            }
        }

        Button(
            onClick = {
                if (name.isNotBlank()) {
                    scope.launch {
                        // Add the user (and make them active, if your VM doesn’t already)
                        viewModel.addUser(name.trim(), emoji)

                        // Optionally: ensure the new user is set as active here
                        // viewModel.setActiveUserToMostRecent()  <-- whatever function you have

                        name = ""
                        emoji = RandomEmojiProvider.randomEmoji()

                        // Navigate to questionnaire
                        onNewAccountCreated()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create account")
        }
    }
}

@Composable
private fun AccountRow(
    user: UserEntity,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${user.emoji}  ${user.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    user.abilityScore?.let { score ->
                        Text(
                            text = "•  Skill ${"%.1f".format(score)}/7.5",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                if (isActive) {
                    Text(
                        text = "Current",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
