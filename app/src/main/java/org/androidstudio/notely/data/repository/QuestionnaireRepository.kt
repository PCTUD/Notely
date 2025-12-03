package org.androidstudio.notely.data.repository

import kotlinx.coroutines.flow.Flow
import org.androidstudio.notely.data.dao.QuestionnaireDao
import org.androidstudio.notely.data.entity.QuestionnaireResponseEntity

class QuestionnaireRepository
    (private val questionnaireDao: QuestionnaireDao)
{

    suspend fun saveResponse(response: QuestionnaireResponseEntity) {
        questionnaireDao.insertResponse(response)
    }

    fun getLatestResponse(): Flow<QuestionnaireResponseEntity?> {
        return questionnaireDao.getLatestResponse()
    }

    suspend fun clearResponses() {
        questionnaireDao.clearAll()
    }
}
