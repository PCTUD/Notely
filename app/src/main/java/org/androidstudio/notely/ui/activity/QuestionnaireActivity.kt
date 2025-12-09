package org.androidstudio.notely.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import org.androidstudio.notely.R
import org.androidstudio.notely.ui.viewmodel.UserViewModel
import org.androidstudio.notely.ui.viewmodel.UserViewModelFactory



class QuestionnaireActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory.fromContext(this)
        )[UserViewModel::class.java]

        val submit = findViewById<Button>(R.id.submitButton)

        submit.setOnClickListener {
            val playedBefore = getYesNo(R.id.qPlayedBefore)
            val grade4 = getYesNo(R.id.qGrade4)
            val grade6 = getYesNo(R.id.qGrade6)
            val grade8 = getYesNo(R.id.qGrade8)
            val circle = getYesNo(R.id.qCircle)
            val practice = getNumber(R.id.qPractice)

            if (
                playedBefore == null ||
                grade4 == null ||
                grade6 == null ||
                grade8 == null ||
                circle == null ||
                practice == null
            ) {
                Toast.makeText(
                    this,
                    "Please answer all questions",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val score = calculateScore(
                playedBefore,
                grade4,
                grade6,
                grade8,
                circle,
                practice
            )

            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }

    // YES/NO helper
    private fun getYesNo(groupId: Int): Boolean? {
        val group = findViewById<RadioGroup>(groupId)
        val checked = group.checkedRadioButtonId
        if (checked == -1) return null
        val text = findViewById<RadioButton>(checked).text.toString()
        return text == "Yes"
    }

    // Number helper (1–5)
    private fun getNumber(groupId: Int): Int? {
        val group = findViewById<RadioGroup>(groupId)
        val checked = group.checkedRadioButtonId
        if (checked == -1) return null
        return findViewById<RadioButton>(checked).text.toString().toInt()
    }

    // EXACT SAME scoring logic as Compose version
    private fun calculateScore(
        playedBefore: Boolean,
        grade4: Boolean,
        grade6: Boolean,
        grade8: Boolean,
        circle: Boolean,
        practice: Int
    ): Double {
        var score = 0.0

        listOf(playedBefore, grade4, grade6, grade8, circle).forEach {
            if (it) score += 1.0
        }

        score += practice * 0.5
        return score
    }
}
