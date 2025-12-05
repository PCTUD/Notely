package org.androidstudio.notely.data.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ActiveUserStore(private val userPreferences: UserPreferences) {

    val activeUserId: Flow<Int?> = userPreferences.activeUserId
    val activeUserName: Flow<String?> = userPreferences.activeUserName

    suspend fun getActiveUserId(): Int? = activeUserId.first()
    suspend fun getActiveUserName(): String? = activeUserName.first()

    suspend fun setActiveUser(id: Int, name: String) {
        userPreferences.setActiveUser(id, name)
    }

    suspend fun clearActiveUser() {
        userPreferences.clearActiveUser()
    }
}

