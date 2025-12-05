package org.androidstudio.notely.data.repository

object RandomEmojiProvider {

    private val emojiSet = listOf(
        "🎸", "🎧", "🎹", "🥁", "🎤",
        "🎶", "🎷", "🎺", "🎻", "🎼"
    )

    fun randomEmoji(): String {
        return emojiSet.random()
    }
}
