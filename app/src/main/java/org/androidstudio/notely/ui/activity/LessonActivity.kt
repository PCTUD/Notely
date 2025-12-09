package org.androidstudio.notely.ui.activity

import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.androidstudio.notely.R
import org.androidstudio.notely.ui.model.LessonType
import org.androidstudio.notely.ui.viewmodel.LessonProgressViewModel

class LessonActivity : AppCompatActivity() {

    private lateinit var viewModel: LessonProgressViewModel
    private lateinit var soundPool: SoundPool
    private val sounds = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        // ---- ViewModel ----
        viewModel = ViewModelProvider(
            this,
            LessonProgressViewModel.fromContext(this)
        )[LessonProgressViewModel::class.java]

        val lessonType = intent.getSerializableExtra("lessonType") as LessonType

        findViewById<TextView>(R.id.lessonTitle).text = lessonType.title
        findViewById<TextView>(R.id.lessonSubtitle).text = lessonType.subtitle

        findViewById<View>(R.id.backButton).setOnClickListener { finish() }

        // ---- SoundPool ----
        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .build()

        loadSounds()
        setupKeys()
        positionBlackKeys()

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        lifecycleScope.launch {
            val progress = viewModel.getProgress(lessonType.lessonId)
            progressBar.progress = (progress * 100).toInt()
        }
    }

    // ------------ SOUND LOADING ------------

    private fun loadSounds() {
        val noteRes: Map<String, Int> = mapOf(
            "c4" to R.raw.c4,
            "cs4" to R.raw.cs4,
            "d4" to R.raw.d4,
            "ds4" to R.raw.ds4,
            "e4" to R.raw.e4,
            "f4" to R.raw.f4,
            "fs4" to R.raw.fs4,
            "g4" to R.raw.g4,
            "gs4" to R.raw.gs4,
            "a5" to R.raw.a5,
            "as5" to R.raw.as5,
            "b5" to R.raw.b5
        )

        noteRes.forEach { (note, resId) ->
            sounds[note] = soundPool.load(this, resId, 1)
        }
    }

    private fun setupKeys() {
        val keyMap: Map<Int, String> = mapOf(
            R.id.keyC4 to "c4",
            R.id.keyCs4 to "cs4",
            R.id.keyD4 to "d4",
            R.id.keyDs4 to "ds4",
            R.id.keyE4 to "e4",
            R.id.keyF4 to "f4",
            R.id.keyFs4 to "fs4",
            R.id.keyG4 to "g4",
            R.id.keyGs4 to "gs4",
            R.id.keyA5 to "a5",
            R.id.keyAs5 to "as5",
            R.id.keyB5 to "b5"
        )

        keyMap.forEach { (viewId, note) ->
            findViewById<View>(viewId).setOnClickListener {
                playNote(note)
            }
        }
    }

    private fun playNote(note: String) {
        sounds[note]?.let {
            soundPool.play(it, 1f, 1f, 1, 0, 1f)
        }
    }

    // ------------ PIANO LAYOUT ------------

    /**
     * Positions black keys between white keys based on actual measured width.
     * Must be called AFTER setContentView.
     */
    private fun positionBlackKeys() {
        val piano = findViewById<View>(R.id.pianoContainer) ?: return

        piano.post {
            val totalWidth = piano.width.toFloat()
            if (totalWidth <= 0f) return@post

            val whiteKeyWidth = totalWidth / 7f

            fun placeBlackKey(viewId: Int, centerIndex: Float) {
                val key = findViewById<View>(viewId) ?: return
                val keyWidth = key.width.toFloat()
                val centerX = centerIndex * whiteKeyWidth
                key.translationX = centerX - keyWidth / 2f
            }

            // C D E F G A B  -> indices 0..6
            placeBlackKey(R.id.keyCs4, 0.5f) // between C (0) and D (1)
            placeBlackKey(R.id.keyDs4, 1.5f) // between D (1) and E (2)
            placeBlackKey(R.id.keyFs4, 3.5f) // between F (3) and G (4)
            placeBlackKey(R.id.keyGs4, 4.5f) // between G (4) and A (5)
            placeBlackKey(R.id.keyAs5, 5.5f) // between A (5) and B (6)
        }
    }

    // ------------ LIFECYCLE ------------

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}
