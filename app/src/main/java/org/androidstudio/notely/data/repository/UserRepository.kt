package org.androidstudio.notely.data.repository

import org.androidstudio.notely.data.dao.UserDao
import org.androidstudio.notely.data.entity.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun save(user: UserEntity) {
        userDao.insert(user)
    }

    fun getUser() = userDao.getUser()

    suspend fun delete(user: UserEntity) {
        userDao.delete(user)
    }
}