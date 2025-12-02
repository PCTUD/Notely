package org.androidstudio.notely.data.dao

import androidx.room.*
import org.androidstudio.notely.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users ORDER BY id LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Delete
    suspend fun delete(user: UserEntity)
}
