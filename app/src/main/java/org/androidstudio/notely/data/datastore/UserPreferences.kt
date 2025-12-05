package org.androidstudio.notely.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_PREFS = "user_preferences"

val Context.dataStore by preferencesDataStore(name = USER_PREFS)

object UserPreferencesKeys {
    val ACTIVE_USER_ID = intPreferencesKey("active_user_id")
    val ACTIVE_USER_NAME = stringPreferencesKey("active_user_name")
}

class UserPreferences(private val context: Context) {

    val activeUserId: Flow<Int?> = context.dataStore.data.map { prefs ->
        prefs[UserPreferencesKeys.ACTIVE_USER_ID]
    }

    val activeUserName: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[UserPreferencesKeys.ACTIVE_USER_NAME]
    }

    suspend fun setActiveUser(id: Int, name: String) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.ACTIVE_USER_ID] = id
            prefs[UserPreferencesKeys.ACTIVE_USER_NAME] = name
        }
    }

    suspend fun clearActiveUser() {
        context.dataStore.edit { prefs ->
            prefs.remove(UserPreferencesKeys.ACTIVE_USER_ID)
            prefs.remove(UserPreferencesKeys.ACTIVE_USER_NAME)
        }
    }
}
