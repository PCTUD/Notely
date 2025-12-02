package org.androidstudio.notely.data.dao

import androidx.room.*
import org.androidstudio.notely.data.entity.QuestionnaireResponseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionnaireDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(response: QuestionnaireResponseEntity)

    @Query("SELECT * FROM questionnaire_responses ORDER BY timestamp DESC")
    fun getResponses(): Flow<List<QuestionnaireResponseEntity>>
}
