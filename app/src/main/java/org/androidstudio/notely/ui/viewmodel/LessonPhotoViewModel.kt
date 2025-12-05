package org.androidstudio.notely.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.androidstudio.notely.data.repository.LessonPhotoRepository

class LessonPhotoViewModel(
    private val repo: LessonPhotoRepository
) : ViewModel() {

    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri

    fun loadPhoto(lessonId: Int) {
        viewModelScope.launch {
            _photoUri.value = repo.getLessonPhoto(lessonId)?.uri
        }
    }

    fun setPhoto(lessonId: Int, uri: String) {
        viewModelScope.launch {
            repo.setLessonPhoto(lessonId, uri)
            _photoUri.value = uri
        }
    }

    fun clearPhoto(lessonId: Int) {
        viewModelScope.launch {
            repo.removeLessonPhoto(lessonId)
            _photoUri.value = null
        }
    }
}
