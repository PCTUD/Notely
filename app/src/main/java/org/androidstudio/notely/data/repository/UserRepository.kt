package org.androidstudio.notely.data.repository


import kotlinx.coroutines.flow.Flow
import org.androidstudio.notely.data.dao.UserDao
import org.androidstudio.notely.data.entity.UserEntity

class UserRepository(private val dao: UserDao) {

    fun getUsers(): Flow<List<UserEntity>> = dao.getUsers()

    suspend fun createUser(user: UserEntity) = dao.insertUser(user)

    suspend fun deleteUser(user: UserEntity) = dao.deleteUser(user)

    suspend fun updateExperienceLabel(userId: Int, label: String?) =
        dao.updateExperienceLabel(userId, label)

}
