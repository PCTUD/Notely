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



class UserViewModel(
    private val userRepository: UserRepository,
    private val activeUserStore: ActiveUserStore,
    private val questionnaireRepository: QuestionnaireRepository
) : ViewModel() {

    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId

    // IMPORTANT: Flow<List<UserEntity>>
    fun getAllUsers(): Flow<List<UserEntity>> = userRepository.getUsers()

    fun loadActiveUser() {
        viewModelScope.launch {
            _currentUserId.value = activeUserStore.getActiveUserId()
        }
    }

    fun setActiveUser(userId: Int, userName: String) {
        viewModelScope.launch {
            activeUserStore.setActiveUser(userId, userName)
            _currentUserId.value = userId
        }
    }

    fun addUser(name: String, emoji: String) {
        viewModelScope.launch {
            val trimmed = name.trim()
            val userId = userRepository.createUser(
                UserEntity(name = trimmed, emoji = emoji)
            )
            // newly created user becomes the active one
            activeUserStore.setActiveUser(userId, trimmed)
            _currentUserId.value = userId         // <— crucial line
        }
    }

    fun saveQuestionnaireForCurrentUser(
        result: QuestionnaireResult,
        score: Double
    ) {
        viewModelScope.launch {
            val userId = currentUserId.value ?: return@launch

            // 1) Save detailed response
            val entity = result.toEntity(userId, score)
            questionnaireRepository.saveResponse(entity)

            // 2) Save summary score on the user
            userRepository.setAbilityScore(userId, score)
        }
    }

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

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
            // (optional) if you want to clear currentUserId when deleting the active user:
            if (currentUserId.value == user.id) {
                _currentUserId.value = null
            }
        }
    }
}


