package org.androidstudio.notely.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionnaire_responses")
data class QuestionnaireResponseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gradeCompleted: Int,
    val yearsLearning: Int,
    val knowsChords: Boolean,
    val knowsScales: Boolean,
    val score: Int
)