package com.ashraful.learningquest.data

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId

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
        val PROFILE = stringPreferencesKey("profile")
        val TODAY_PROGRESS = intPreferencesKey("today_progress")
        val LAST_PROGRESS_DAY = intPreferencesKey("last_progress_day")
        val TOTAL_QUESTIONS = intPreferencesKey("total_questions")
        val CORRECT_ANSWERS = intPreferencesKey("correct_answers")
        val OWNED_GIFTS = stringPreferencesKey("owned_gifts")
        val EQUIPPED_GIFT = stringPreferencesKey("equipped_gift")
    }

    val profile: Flow<String?> =
        context.gameDataStore.data.map { it[Keys.PROFILE] }

    fun ownedGiftIds(): Flow<Set<String>> =
        context.gameDataStore.data.map { preferences ->
            preferences[Keys.OWNED_GIFTS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

    val equippedGiftId: Flow<String?> =
        context.gameDataStore.data.map { it[Keys.EQUIPPED_GIFT] }

    suspend fun saveProfile(value: String) {
        context.gameDataStore.edit { it[Keys.PROFILE] = value }
    }

    suspend fun purchaseGift(gift: Gift): Boolean {
        var purchased = false
        context.gameDataStore.edit { preferences ->
            val currentCoins = preferences[Keys.COINS] ?: 0
            val owned = preferences[Keys.OWNED_GIFTS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableSet()
                ?: mutableSetOf()

            if (gift.id !in owned && currentCoins >= gift.price) {
                owned.add(gift.id)
                preferences[Keys.COINS] = currentCoins - gift.price
                preferences[Keys.OWNED_GIFTS] = owned.joinToString(",")
                purchased = true
            }
        }
        return purchased
    }

    suspend fun equipGift(giftId: String) {
        context.gameDataStore.edit { preferences ->
            val owned = preferences[Keys.OWNED_GIFTS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()

            if (giftId in owned) {
                preferences[Keys.EQUIPPED_GIFT] = giftId
            }
        }
    }

    suspend fun clearEquippedGift() {
        context.gameDataStore.edit { preferences ->
            preferences.remove(Keys.EQUIPPED_GIFT)
        }
    }

    val gameData: Flow<GameData> =
        context.gameDataStore.data.map { preferences ->

            val totalQuestions = preferences[Keys.TOTAL_QUESTIONS] ?: 0
            val correctAnswers = preferences[Keys.CORRECT_ANSWERS] ?: 0

            val achievementCount = listOf(
                (preferences[Keys.XP] ?: 0) >= 50,
                (preferences[Keys.STREAK] ?: 0) >= 3,
                (preferences[Keys.COINS] ?: 0) >= 100,
                (preferences[Keys.MATH_SCORE] ?: 0) >= 10,
                (preferences[Keys.ENGLISH_SCORE] ?: 0) >= 10,
                (preferences[Keys.SCIENCE_SCORE] ?: 0) >= 10,
                (preferences[Keys.PUZZLE_SCORE] ?: 0) >= 10,
                (preferences[Keys.LEVEL] ?: 1) >= 5,
                totalQuestions >= 100,
                totalQuestions >= 20 &&
                    correctAnswers * 100 >= totalQuestions * 90
            ).count { it }

            val today = LocalDate.now(ZoneId.systemDefault())
                .toEpochDay()
                .toInt()

            GameData(
                coins = preferences[Keys.COINS] ?: 0,
                xp = preferences[Keys.XP] ?: 0,
                level = preferences[Keys.LEVEL] ?: 1,
                streak = preferences[Keys.STREAK] ?: 0,
                mathScore = preferences[Keys.MATH_SCORE] ?: 0,
                englishScore = preferences[Keys.ENGLISH_SCORE] ?: 0,
                scienceScore = preferences[Keys.SCIENCE_SCORE] ?: 0,
                puzzleScore = preferences[Keys.PUZZLE_SCORE] ?: 0,
                mathDifficulty = preferences[Keys.MATH_DIFFICULTY] ?: 1,
                totalQuestions = totalQuestions,
                correctAnswers = correctAnswers,
                achievementCount = achievementCount,
                todayProgress =
                    if ((preferences[Keys.LAST_PROGRESS_DAY] ?: 0) == today) {
                        preferences[Keys.TODAY_PROGRESS] ?: 0
                    } else {
                        0
                    }
            )
        }

    suspend fun recordAnswer(correct: Boolean) {
        context.gameDataStore.edit { preferences ->
            preferences[Keys.TOTAL_QUESTIONS] =
                (preferences[Keys.TOTAL_QUESTIONS] ?: 0) + 1

            if (correct) {
                preferences[Keys.CORRECT_ANSWERS] =
                    (preferences[Keys.CORRECT_ANSWERS] ?: 0) + 1
            }
        }
    }

    suspend fun addReward(coins: Int, xp: Int) {
        context.gameDataStore.edit { preferences ->

            val currentCoins = preferences[Keys.COINS] ?: 0
            val currentXp = preferences[Keys.XP] ?: 0
            val currentLevel = preferences[Keys.LEVEL] ?: 1
            val currentStreak = preferences[Keys.STREAK] ?: 0
            val lastActivityDay = preferences[Keys.LAST_ACTIVITY_DAY] ?: 0

            val today = LocalDate.now(ZoneId.systemDefault())
                .toEpochDay()
                .toInt()

            val newStreak = when {
                lastActivityDay == today -> currentStreak
                lastActivityDay == today - 1 -> currentStreak + 1
                else -> 1
            }

            val newXp = currentXp + xp

            val progressDay =
                if (lastActivityDay == today) {
                    preferences[Keys.TODAY_PROGRESS] ?: 0
                } else {
                    0
                }

            val newProgress = (progressDay + 1).coerceAtMost(3)
            val newLevel = (newXp / 100) + 1

            preferences[Keys.COINS] = currentCoins + coins
            preferences[Keys.XP] = newXp
            preferences[Keys.LEVEL] = maxOf(currentLevel, newLevel)
            preferences[Keys.STREAK] = newStreak
            preferences[Keys.LAST_ACTIVITY_DAY] = today
            preferences[Keys.TODAY_PROGRESS] = newProgress
            preferences[Keys.LAST_PROGRESS_DAY] = today
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
                val newCorrectStreak = currentCorrectStreak + 1

                preferences[intPreferencesKey("math_correct_streak")] =
                    newCorrectStreak
                preferences[intPreferencesKey("math_wrong_streak")] = 0

                if (newCorrectStreak >= 3) {
                    preferences[Keys.MATH_DIFFICULTY] =
                        (currentDifficulty + 1).coerceAtMost(10)
                    preferences[intPreferencesKey("math_correct_streak")] = 0
                }
            } else {
                val newWrongStreak = currentWrongStreak + 1

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
