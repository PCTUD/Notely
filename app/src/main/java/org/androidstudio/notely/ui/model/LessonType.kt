package org.androidstudio.notely.ui.model

enum class LessonType(
    val lessonId: Int,
    val title: String,
    val subtitle: String
) {
    MELODY(
        lessonId = 1,
        title = "Melodies 1",
        subtitle = "Musical Direction"
    ),
    HARMONY(
        lessonId = 2,
        title = "Harmony 1",
        subtitle = "Harmonic Shapes"
    ),
    CHORDS(
        lessonId = 3,
        title = "Chords 1",
        subtitle = "Triad Building"
    ),
    SCALES(
        lessonId = 4,
        title = "Scales 1",
        subtitle = "Scale Patterns"
    )
}