package org.androidstudio.notely.data.repository

import kotlinx.coroutines.flow.Flow
import org.androidstudio.notely.data.dao.QuestionnaireDao
import org.androidstudio.notely.data.entity.QuestionnaireResponseEntity

class QuestionnaireRepository(
    private val questionnaireDao: QuestionnaireDao
) {

    suspend fun saveResponse(response: QuestionnaireResponseEntity) {
        questionnaireDao.insertResponse(response)
    }

    fun getLatestResponseForUser(userId: Int): Flow<QuestionnaireResponseEntity?> {
        return questionnaireDao.getLatestResponseForUser(userId)
    }

    suspend fun clearResponsesForUser(userId: Int) {
        questionnaireDao.clearAllForUser(userId)
    }
}

