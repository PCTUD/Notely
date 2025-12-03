package org.androidstudio.notely.data.repository

import org.androidstudio.notely.data.dao.UserDao
import org.androidstudio.notely.data.entity.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: UserEntity) =
        userDao.insertUser(user)

    suspend fun getUserById(id: Int): UserEntity? =
        userDao.getUserById(id)

    suspend fun deleteUser(user: UserEntity) =
        userDao.deleteUser(user)
}
