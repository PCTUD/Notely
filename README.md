# Notely 🎹

Notely is a multi-user Android music learning app built with: Jetpack Compose, RoomDB, and Kotlin.  
It helps beginner piano students track their progress through simple lessons, record their musical experience via a questionnaire, and maintain per-user lesson progress.

---

## Features

### Multi-user accounts

- Create multiple local user profiles.
- Each user has:
    - A name
    - An emoji avatar
    - A computed skill score based on a short questionnaire.
- Switch between accounts from the **Accounts** screen.
- **Delete** accounts from the list.

### Musical ability questionnaire

- Runs when a new account is created (and can be accessed from Home).
- Questions include:
    - Have you played piano before?
    - Finished grades 4 / 6 / 8?
    - Understand the circle of fifths?
    - How many times per week will you practice? (1–5)
- Each “yes” answer is worth **1 point**.
- Practice frequency contributes **0.5 points per practice** (e.g. 5×/week = 2.5 pts).
- Maximum score: **7.5**.
- The score is **stored in Room** and displayed beside the user’s name on the Accounts screen.

### Lessons & progress

- **Home screen** shows 4 tiles:
    - Melody
    - Harmony
    - Chords
    - Scales
- Each tile has:
    - A short description
    - A **progress bar** (0–100%)
- Tapping a tile opens a shared **Lesson screen**, configured by `LessonType`.

## Setup & Running

1. Clone the project into Android Studio.
2. Ensure you have:
    - Android Studio (Giraffe/Koala+)
    - Kotlin + Compose support enabled.
3. Build the project to download dependencies.
4. Run on an emulator or physical device.

> Note: the piano keyboard expects note samples (e.g. `c4.wav`, `cs4.wav`, …) in `app/src/main/res/raw/`.  
> These should be short piano note audio files named to match the IDs used in `LessonScreen`.

---

## How it satisfies the coursework specs

- **Input screen:**
    - `AccountScreen` (name input), `QuestionnaireScreen` (quiz answers).
- **Room database:**
    - `UserEntity`, `QuestionnaireResponseEntity`, `LessonProgressEntity` via Room.
- **Recycling list from Room:**
    - `AccountScreen` uses `LazyColumn` populated from `getUsers()` (Room `Flow`).
- **Full CRUD:**
    - INSERT, SELECT, UPDATE, DELETE all implemented on app data (not login).
- **Screens & persistence:**
    - List screen (Accounts), input screens (Accounts & Questionnaire), extra screens (Lesson & Home), state persisted via Room + DataStore.
- **ActivityResultLauncher:**
    - Used on `LessonScreen` to launch the photo picker and receive an image URI.
- **Extra Android features:**
    - Sound playback (piano keyboard), touch/drag gestures (floating lesson photo), DataStore for active user.

---

## Future Improvements

- Add more guided lesson content for each `LessonType` (Melody, Harmony, Chords, Scales).
- Add a “Reset lesson progress” action per user/lesson.
- Add unlockable lessons based on progress or questionnaire score.
- Add basic analytics (e.g. most-practised lesson per user).
