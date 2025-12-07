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

class UserViewModel(
    private val userRepository: UserRepository,
    private val activeUserStore: ActiveUserStore
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
        }
    }

    fun addUser(name: String, emoji: String) {
        viewModelScope.launch {
            val user = UserEntity(
                name = name,
                emoji = emoji,
                experienceLabel = "Not set",
                experiencePoints = 0
            )
            userRepository.createUser(user)
        }
    }

    fun setExperienceLabel(userId: Int, label: String) {
        viewModelScope.launch {
            userRepository.updateExperienceLabel(userId, label)
        }
    }

}
