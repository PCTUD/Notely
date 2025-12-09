package org.androidstudio.notely.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.androidstudio.notely.R
import org.androidstudio.notely.ui.viewmodel.LessonProgressViewModel
import org.androidstudio.notely.ui.model.LessonType


class HomeActivity : AppCompatActivity() {

    private lateinit var lessonProgressViewModel: LessonProgressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // ViewModel setup
        lessonProgressViewModel = ViewModelProvider(
            this,
            LessonProgressViewModel.fromContext(this)
        )[LessonProgressViewModel::class.java]

        // Accounts button
        findViewById<Button>(R.id.accountsButton).setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
            finish()
        }

        // Lesson cards
        setupLessonButton(R.id.melodyCard, LessonType.MELODY)
        setupLessonButton(R.id.harmonyCard, LessonType.HARMONY)
        setupLessonButton(R.id.chordsCard, LessonType.CHORDS)
        setupLessonButton(R.id.scalesCard, LessonType.SCALES)
    }

    private fun setupLessonButton(viewId: Int, type: LessonType) {
        findViewById<View>(viewId).setOnClickListener {
            val intent = Intent(this, LessonActivity::class.java)
            intent.putExtra("lessonType", type)
            startActivity(intent)
        }
    }
}
