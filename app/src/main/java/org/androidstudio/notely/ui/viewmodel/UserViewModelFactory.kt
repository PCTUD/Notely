package org.androidstudio.notely.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.androidstudio.notely.data.datastore.ActiveUserStore
import org.androidstudio.notely.data.datastore.UserPreferences
import org.androidstudio.notely.data.repository.QuestionnaireRepository
import org.androidstudio.notely.data.repository.RepositoryProvider
import org.androidstudio.notely.data.repository.UserRepository

class UserViewModelFactory(
    private val userRepository: UserRepository,
    private val questionnaireRepository: QuestionnaireRepository,
    private val activeUserStore: ActiveUserStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(
                userRepository = userRepository,
                questionnaireRepository = questionnaireRepository,
                activeUserStore = activeUserStore
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        fun fromContext(context: Context): UserViewModelFactory {
            return UserViewModelFactory(
                userRepository = RepositoryProvider.userRepository(context),
                questionnaireRepository = RepositoryProvider.questionnaireRepository(context),
                activeUserStore = ActiveUserStore(
                    UserPreferences(context)
                )
            )
        }
    }
}

