// interacts with the database to manipulate questionnaire data
package org.androidstudio.notely.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.androidstudio.notely.data.entity.QuestionnaireResponseEntity

@Dao
interface QuestionnaireDao {
    //sets user response in the database, defining the user's experience level
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: QuestionnaireResponseEntity)

    @Query("SELECT * FROM questionnaire_responses ORDER BY id DESC LIMIT 1")
    fun getLatestResponse(): Flow<QuestionnaireResponseEntity?>

    @Query("DELETE FROM questionnaire_responses")
    suspend fun clearAll()
}
