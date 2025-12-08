package org.androidstudio.notely.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.androidstudio.notely.data.datastore.ActiveUserStore
import org.androidstudio.notely.data.entity.UserEntity
import org.androidstudio.notely.data.repository.UserRepository
import org.androidstudio.notely.data.repository.QuestionnaireRepository
import org.androidstudio.notely.data.entity.QuestionnaireResponseEntity
import org.androidstudio.notely.ui.screens.QuestionnaireResult


/* UserViewModel: central state holder for user accounts. Exposes a
Flow of all users from Room, tracks the currently active user via
ActiveUserStore, creates new users, deletes users, and saves detailed
questionnaire responses plus a computed ability score for the active
user.*/

class UserViewModel(
    private val userRepository: UserRepository,
    private val activeUserStore: ActiveUserStore,
    private val questionnaireRepository: QuestionnaireRepository
) : ViewModel() {

    // Currently active user id (or null if none)
    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId

    // All users stream from Room
    fun getAllUsers(): Flow<List<UserEntity>> = userRepository.getUsers()

    // Load active user id from DataStore
    fun loadActiveUser() {
        viewModelScope.launch {
            _currentUserId.value = activeUserStore.getActiveUserId()
        }
    }

    // Set a specific user as active
    fun setActiveUser(userId: Int, userName: String) {
        viewModelScope.launch {
            activeUserStore.setActiveUser(userId, userName)
            _currentUserId.value = userId
        }
    }

    // Create user and immediately mark them active
    fun addUser(name: String, emoji: String) {
        viewModelScope.launch {
            val trimmed = name.trim()
            val userId = userRepository.createUser(
                UserEntity(name = trimmed, emoji = emoji)
            )
            // Newly created user becomes the active one
            activeUserStore.setActiveUser(userId, trimmed)
            _currentUserId.value = userId
        }
    }

    // Save questionnaire + ability score for current user
    fun saveQuestionnaireForCurrentUser(
        result: QuestionnaireResult,
        score: Double
    ) {
        viewModelScope.launch {
            val userId = currentUserId.value ?: return@launch

            // 1) Save detailed questionnaire response
            val entity = result.toEntity(userId, score)
            questionnaireRepository.saveResponse(entity)

            // 2) Write summary score onto the user row
            userRepository.setAbilityScore(userId, score)
        }
    }

    // Map questionnaire result → Room entity
    private fun QuestionnaireResult.toEntity(
        userId: Int,
        score: Double
    ): QuestionnaireResponseEntity {
        return QuestionnaireResponseEntity(
            userId = userId,
            playedBefore = playedBefore!!,
            grade4 = grade4!!,
            grade6 = grade6!!,
            grade8 = grade8!!,
            circleOfFifths = circleOfFifths!!,
            practiceFrequency = practiceFrequency!!,
            score = score
        )
    }

    // Delete a user, optionally clearing active id
    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
            // Clear active id if we just deleted the active user
            if (currentUserId.value == user.id) {
                _currentUserId.value = null
            }
        }
    }
}
