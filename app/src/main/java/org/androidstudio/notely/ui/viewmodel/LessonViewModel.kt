package org.androidstudio.notely.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.androidstudio.notely.data.model.Lesson
import org.androidstudio.notely.data.repository.LessonRepository

class LessonViewModel(private val repository: LessonRepository) : ViewModel() {

    val allLessons: LiveData<List<Lesson>> = repository.allLessons

    fun insert(lesson: Lesson) = viewModelScope.launch {
        repository.insert(lesson)
    }

    fun update(lesson: Lesson) = viewModelScope.launch {
        repository.update(lesson)
    }

    fun delete(lesson: Lesson) = viewModelScope.launch {
        repository.delete(lesson)
    }

    fun getLesson(id: Int): LiveData<Lesson?> {
        val result = MutableLiveData<Lesson?>()
        viewModelScope.launch {
            result.postValue(repository.getLesson(id))
        }
        return result
    }
}

class LessonViewModelFactory(private val repository: LessonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LessonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
