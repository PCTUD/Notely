// data/dao/UserDao.kt
package org.androidstudio.notely.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import org.androidstudio.notely.data.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY name COLLATE NOCASE")
    fun getUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("UPDATE users SET abilityScore = :score WHERE id = :userId")
    suspend fun updateAbilityScore(userId: Int, score: Double)

}

