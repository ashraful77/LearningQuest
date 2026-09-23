package com.ashraful.learningquest.data

data class GameData(
    val coins: Int = 0,
    val xp: Int = 0,
    val level: Int = 1,
    val streak: Int = 0,
    val mathScore: Int = 0,
    val englishScore: Int = 0,
    val scienceScore: Int = 0,
    val puzzleScore: Int = 0,
    val mathDifficulty: Int = 1
) {
    val xpForNextLevel: Int
        get() = level * 100

    val xpProgress: Float
        get() = (xp % xpForNextLevel).toFloat() / xpForNextLevel
}