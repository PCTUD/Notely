// interacts with the database to manipulate questionnaire data
package org.androidstudio.notely.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.androidstudio.notely.data.entity.QuestionnaireResponseEntity

@Dao
interface QuestionnaireDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: QuestionnaireResponseEntity)

    @Query("""
        SELECT * FROM questionnaire_responses
        WHERE userId = :userId
        ORDER BY id DESC
        LIMIT 1
    """)
    fun getLatestResponseForUser(userId: Int): Flow<QuestionnaireResponseEntity?>

    @Query("DELETE FROM questionnaire_responses WHERE userId = :userId")
    suspend fun clearAllForUser(userId: Int)
}
