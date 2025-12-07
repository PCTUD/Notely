package org.androidstudio.notely.data.repository


import kotlinx.coroutines.flow.Flow
import org.androidstudio.notely.data.dao.UserDao
import org.androidstudio.notely.data.entity.UserEntity

class UserRepository(private val dao: UserDao) {

    fun getUsers(): Flow<List<UserEntity>> = dao.getUsers()

    suspend fun createUser(user: UserEntity): Int {
        return dao.insertUser(user).toInt()   // <— return new PK
    }

    suspend fun deleteUser(user: UserEntity) = dao.deleteUser(user)

    suspend fun setAbilityScore(userId: Int, score: Double) {
        dao.updateAbilityScore(userId, score)
    }
}


