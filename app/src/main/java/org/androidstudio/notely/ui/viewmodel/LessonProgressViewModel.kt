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

    // Load progress for a lesson as 0f..1f (10 steps).
    suspend fun getProgress(lessonId: Int): Float {
        val userId = currentUserId.value ?: activeUserStore.getActiveUserId()
        userId ?: return 0f

        val entity = lessonProgressRepository.getProgress(userId, lessonId)
        val steps = entity?.completedExercisesCount ?: 0
        return (steps.coerceIn(0, 10) / 10f)
    }

    // Save progress as 0f..1f into 0..10 steps.
    fun setProgress(lessonId: Int, progress: Float) {
        viewModelScope.launch {
            val userId = currentUserId.value ?: activeUserStore.getActiveUserId()
            userId ?: return@launch

            val steps = (progress.coerceIn(0f, 1f) * 10f).toInt()
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
