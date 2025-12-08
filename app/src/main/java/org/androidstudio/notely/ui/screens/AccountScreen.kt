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
import androidx.compose.material3.TextButton

/* AccountScreen: lets the user pick an existing account or create a new
one. Existing accounts are listed from Room, showing per-user emoji and
ability score. New accounts are created with a name and random emoji,
and the active user is updated when an account is selected. */

@Composable
fun AccountScreen(
    viewModel: UserViewModel,
    onContinue: () -> Unit,             // navigate with existing account
    onNewAccountCreated: () -> Unit     // navigate after new account
) {
    val scope = rememberCoroutineScope()

    // Load current active user on entry
    LaunchedEffect(Unit) {
        viewModel.loadActiveUser()
    }

    // all users from Room
    val users by viewModel
        .getAllUsers()
        .collectAsState(initial = emptyList<UserEntity>())

    // Currently active user id
    val currentUserId by viewModel.currentUserId.collectAsState()

    // New account name + emoji
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

        // Existing users section
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
                            // Set active user then continue
                            scope.launch {
                                viewModel.setActiveUser(user.id, user.name)
                                onContinue()
                            }
                        },
                        onDelete = {
                            // Remove this user from Room
                            viewModel.deleteUser(user)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // New account section
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
            // Show chosen emoji
            Text(text = "Emoji: $emoji", style = MaterialTheme.typography.bodyLarge)

            // Shuffle emoji button
            Button(onClick = { emoji = RandomEmojiProvider.randomEmoji() }) {
                Text("Shuffle")
            }
        }

        // Create account button
        Button(
            onClick = {
                if (name.isNotBlank()) {
                    val trimmed = name.trim()
                    viewModel.addUser(trimmed, emoji)
                    name = ""
                    emoji = RandomEmojiProvider.randomEmoji()
                    onNewAccountCreated()   // navigate to Questionnaire
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create account")
        }
    }
}

/* AccountRow: one account row showing emoji, name, optional ability
score, current-user label, and a delete button. */
@Composable
private fun AccountRow(
    user: UserEntity,
    isActive: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // select this user
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
            // LEFT: emoji + name + skill
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

                    // Optional ability score label
                    user.abilityScore?.let { score ->
                        Text(
                            text = "• Skill ${"%.1f".format(score)}/7.5",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // RIGHT: "Current" label + Delete button
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isActive) {
                    Text(
                        text = "Current",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
