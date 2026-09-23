package com.ashraful.learningquest.data

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.gameDataStore by preferencesDataStore(
    name = "learning_quest_data"
)

class GameDataStore(private val context: Context) {

    private object Keys {
        val COINS = intPreferencesKey("coins")
        val XP = intPreferencesKey("xp")
        val LEVEL = intPreferencesKey("level")
        val STREAK = intPreferencesKey("streak")
        val MATH_SCORE = intPreferencesKey("math_score")
        val ENGLISH_SCORE = intPreferencesKey("english_score")
        val SCIENCE_SCORE = intPreferencesKey("science_score")
        val PUZZLE_SCORE = intPreferencesKey("puzzle_score")
        val MATH_DIFFICULTY = intPreferencesKey("math_difficulty")
        val LAST_ACTIVITY_DAY = intPreferencesKey("last_activity_day")
    }

    val gameData: Flow<GameData> =
        context.gameDataStore.data.map { preferences ->

            GameData(
                coins = preferences[Keys.COINS] ?: 0,
                xp = preferences[Keys.XP] ?: 0,
                level = preferences[Keys.LEVEL] ?: 1,
                streak = preferences[Keys.STREAK] ?: 0,
                mathScore = preferences[Keys.MATH_SCORE] ?: 0,
                englishScore = preferences[Keys.ENGLISH_SCORE] ?: 0,
                scienceScore = preferences[Keys.SCIENCE_SCORE] ?: 0,
                puzzleScore = preferences[Keys.PUZZLE_SCORE] ?: 0,
                mathDifficulty = preferences[Keys.MATH_DIFFICULTY] ?: 1
            )
        }

    suspend fun addReward(
        coins: Int,
        xp: Int
    ) {
        context.gameDataStore.edit { preferences ->

            val currentCoins = preferences[Keys.COINS] ?: 0
            val currentXp = preferences[Keys.XP] ?: 0
            val currentLevel = preferences[Keys.LEVEL] ?: 1

            val newXp = currentXp + xp
            val newLevel = (newXp / 100) + 1

            preferences[Keys.COINS] = currentCoins + coins
            preferences[Keys.XP] = newXp
            preferences[Keys.LEVEL] =
                maxOf(currentLevel, newLevel)
        }
    }

    suspend fun recordMathAnswer(correct: Boolean) {

        context.gameDataStore.edit { preferences ->

            val currentDifficulty =
                preferences[Keys.MATH_DIFFICULTY] ?: 1

            val currentCorrectStreak =
                preferences[intPreferencesKey("math_correct_streak")] ?: 0

            val currentWrongStreak =
                preferences[intPreferencesKey("math_wrong_streak")] ?: 0

            if (correct) {

                val newCorrectStreak =
                    currentCorrectStreak + 1

                preferences[intPreferencesKey("math_correct_streak")] =
                    newCorrectStreak

                preferences[intPreferencesKey("math_wrong_streak")] = 0

                if (newCorrectStreak >= 3) {

                    preferences[Keys.MATH_DIFFICULTY] =
                        (currentDifficulty + 1).coerceAtMost(10)

                    preferences[intPreferencesKey("math_correct_streak")] = 0
                }

            } else {

                val newWrongStreak =
                    currentWrongStreak + 1

                preferences[intPreferencesKey("math_wrong_streak")] =
                    newWrongStreak

                preferences[intPreferencesKey("math_correct_streak")] = 0

                if (newWrongStreak >= 2) {

                    preferences[Keys.MATH_DIFFICULTY] =
                        (currentDifficulty - 1).coerceAtLeast(1)

                    preferences[intPreferencesKey("math_wrong_streak")] = 0
                }
            }
        }
    }
}