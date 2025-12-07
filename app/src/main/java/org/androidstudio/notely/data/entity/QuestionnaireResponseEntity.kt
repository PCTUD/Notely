package org.androidstudio.notely.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionnaire_responses")
data class QuestionnaireResponseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // link a response to a specific user – match the type to UserEntity.id
    val userId: Int,

    val playedBefore: Boolean,
    val grade4: Boolean,
    val grade6: Boolean,
    val grade8: Boolean,
    val circleOfFifths: Boolean,
    val practiceFrequency: Int,

    val score: Double
)

