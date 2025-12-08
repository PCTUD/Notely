package org.androidstudio.notely.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.androidstudio.notely.data.datastore.ActiveUserStore
import org.androidstudio.notely.data.datastore.UserPreferences
import org.androidstudio.notely.data.repository.LessonProgressRepository
import org.androidstudio.notely.data.repository.RepositoryProvider

class LessonProgressViewModel(
    private val lessonProgressRepository: LessonProgressRepository,
    private val activeUserStore: ActiveUserStore
) : ViewModel() {

    private val _currentUserId = MutableStateFlow<Int?>(null)
    val currentUserId: StateFlow<Int?> = _currentUserId

    init {
        viewModelScope.launch {
            _currentUserId.value = activeUserStore.getActiveUserId()
        }
    }

    // Ensure we always have the active user ID, or null if none.
    private suspend fun requireUserId(): Int? {
        // Always ask ActiveUserStore for the *latest* active user
        val fromStore = activeUserStore.getActiveUserId()
        _currentUserId.value = fromStore
        return fromStore
    }

    // Load progress for a lesson as 0f..1f (20 steps: 0.05 each).
    suspend fun getProgress(lessonId: Int): Float {
        val userId = requireUserId() ?: return 0f
        val entity = lessonProgressRepository.getProgress(userId, lessonId)
        val steps = entity?.completedExercisesCount ?: 0
        return (steps.coerceIn(0, 20) / 20f)
    }

    // Save progress as 0f..1f into 0..20 steps.
    fun setProgress(lessonId: Int, progress: Float) {
        viewModelScope.launch {
            val userId = requireUserId() ?: return@launch
            val clamped = progress.coerceIn(0f, 1f)
            val steps = (clamped * 20f).toInt()
            lessonProgressRepository.setProgress(userId, lessonId, steps)
        }
    }

    companion object Factory {
        fun fromContext(context: Context): LessonProgressViewModelFactory {
            return LessonProgressViewModelFactory(
                lessonProgressRepository = RepositoryProvider.progressRepository(context),
                activeUserStore = ActiveUserStore(UserPreferences(context))
            )
        }
    }
}

class LessonProgressViewModelFactory(
    private val lessonProgressRepository: LessonProgressRepository,
    private val activeUserStore: ActiveUserStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonProgressViewModel::class.java)) {
            return LessonProgressViewModel(lessonProgressRepository, activeUserStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
