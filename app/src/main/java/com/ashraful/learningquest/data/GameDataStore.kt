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
        val ANISH_DIAMONDS = intPreferencesKey("anish_diamonds")
        val ANISH_OWNED_ITEMS = stringPreferencesKey("anish_owned_items")
        val ANISH_TOTAL_QUESTIONS = intPreferencesKey("anish_total_questions")
        val ANISH_CORRECT_ANSWERS = intPreferencesKey("anish_correct_answers")
        val ANISH_ACHIEVEMENTS = stringPreferencesKey("anish_achievements")
        val ANISH_ANSWERED_QUESTIONS = stringPreferencesKey("anish_answered_questions")
        val ANISH_DAILY_REWARD_DATE = stringPreferencesKey("anish_daily_reward_date")
        val ANISH_DAILY_REWARDED_QUESTIONS = stringPreferencesKey("anish_daily_rewarded_questions")
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
        val SEEN_GIFTS = stringPreferencesKey("seen_gifts")
        val REAL_TEST_HISTORY = stringPreferencesKey("real_test_history")
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

    fun newGiftIds(): Flow<Set<String>> =
        context.gameDataStore.data.map { preferences ->
            val owned = preferences[Keys.OWNED_GIFTS]
                ?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
            val seen = preferences[Keys.SEEN_GIFTS]
                ?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
            owned - seen
        }

    val realTestHistory: Flow<List<String>> =
        context.gameDataStore.data.map { preferences ->
            preferences[Keys.REAL_TEST_HISTORY]
                ?.split("||")?.filter { it.isNotBlank() } ?: emptyList()
        }

    suspend fun recordRealTest(subject: String, score: Int, total: Int, seconds: Long) {
        context.gameDataStore.edit { preferences ->
            val old = preferences[Keys.REAL_TEST_HISTORY]
                ?.split("||")?.filter { it.isNotBlank() }?.toMutableList()
                ?: mutableListOf()
            val entry = subject + "|" + score + "|" + total + "|" + seconds + "|" + System.currentTimeMillis()
            old.add(0, entry)
            preferences[Keys.REAL_TEST_HISTORY] = old.take(10).joinToString("||")
        }
    }

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

    suspend fun markGiftsSeen(giftIds: Set<String>) {
        if (giftIds.isEmpty()) return
        context.gameDataStore.edit { preferences ->
            val seen = preferences[Keys.SEEN_GIFTS]
                ?.split(",")?.filter { it.isNotBlank() }?.toMutableSet()
                ?: mutableSetOf()
            seen.addAll(giftIds)
            preferences[Keys.SEEN_GIFTS] = seen.joinToString(",")
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
                diamonds = preferences[Keys.ANISH_DIAMONDS] ?: 0,
                anishTotalQuestions = preferences[Keys.ANISH_TOTAL_QUESTIONS] ?: 0,
                anishCorrectAnswers = preferences[Keys.ANISH_CORRECT_ANSWERS] ?: 0,
                anishAchievements = preferences[Keys.ANISH_ACHIEVEMENTS]
                    ?.split(",")?.filter { it.isNotBlank() }?.mapNotNull { it.toIntOrNull() }?.toSet()
                    ?: emptySet(),
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

    data class AnishAttemptResult(
        val dailyRewarded: Boolean,
        val firstEver: Boolean,
        val milestoneBonus: Int
    )

    fun anishAnsweredQuestionIds(): Flow<Set<String>> =
        context.gameDataStore.data.map { preferences ->
            preferences[Keys.ANISH_ANSWERED_QUESTIONS]
                ?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
        }

    suspend fun recordAnishQuestionAttempt(
        questionId: String,
        correct: Boolean
    ): AnishAttemptResult {
        var dailyRewarded = false
        var firstEver = false
        var milestoneBonus = 0
        val today = LocalDate.now(ZoneId.systemDefault()).toString()

        context.gameDataStore.edit { preferences ->
            val answered = preferences[Keys.ANISH_ANSWERED_QUESTIONS]
                ?.split(",")?.filter { it.isNotBlank() }?.toMutableSet()
                ?: mutableSetOf()

            firstEver = answered.add(questionId)
            if (firstEver) {
                val total = (preferences[Keys.ANISH_TOTAL_QUESTIONS] ?: 0) + 1
                val correctTotal = (preferences[Keys.ANISH_CORRECT_ANSWERS] ?: 0) +
                    if (correct) 1 else 0
                val achieved = preferences[Keys.ANISH_ACHIEVEMENTS]
                    ?.split(",")?.filter { it.isNotBlank() }?.mapNotNull { it.toIntOrNull() }?.toMutableSet()
                    ?: mutableSetOf()

                val milestones = listOf(5 to 10, 20 to 25, 50 to 50, 100 to 100)
                for ((threshold, bonus) in milestones) {
                    if (total >= threshold && threshold !in achieved) {
                        achieved.add(threshold)
                        milestoneBonus += bonus
                    }
                }

                preferences[Keys.ANISH_TOTAL_QUESTIONS] = total
                preferences[Keys.ANISH_CORRECT_ANSWERS] = correctTotal
                preferences[Keys.ANISH_ACHIEVEMENTS] = achieved.joinToString(",")
            }
            preferences[Keys.ANISH_ANSWERED_QUESTIONS] = answered.joinToString(",")

            val savedDate = preferences[Keys.ANISH_DAILY_REWARD_DATE]
            val rewardedToday = if (savedDate == today) {
                preferences[Keys.ANISH_DAILY_REWARDED_QUESTIONS]
                    ?.split(",")?.filter { it.isNotBlank() }?.toMutableSet()
                    ?: mutableSetOf()
            } else {
                mutableSetOf()
            }

            if (questionId !in rewardedToday) {
                rewardedToday.add(questionId)
                preferences[Keys.ANISH_DAILY_REWARD_DATE] = today
                preferences[Keys.ANISH_DAILY_REWARDED_QUESTIONS] = rewardedToday.joinToString(",")
                preferences[Keys.ANISH_DIAMONDS] =
                    (preferences[Keys.ANISH_DIAMONDS] ?: 0) + 5
                dailyRewarded = true
            } else if (savedDate != today) {
                preferences[Keys.ANISH_DAILY_REWARD_DATE] = today
                preferences[Keys.ANISH_DAILY_REWARDED_QUESTIONS] = ""
            }

            if (milestoneBonus > 0) {
                preferences[Keys.ANISH_DIAMONDS] =
                    (preferences[Keys.ANISH_DIAMONDS] ?: 0) + milestoneBonus
            }
        }
        return AnishAttemptResult(dailyRewarded, firstEver, milestoneBonus)
    }


    suspend fun addAnishDiamonds(amount: Int) {
        if (amount <= 0) return
        context.gameDataStore.edit { preferences ->
            preferences[Keys.ANISH_DIAMONDS] =
                (preferences[Keys.ANISH_DIAMONDS] ?: 0) + amount
        }
    }

    fun anishOwnedItemIds(): Flow<Set<String>> =
        context.gameDataStore.data.map { preferences ->
            preferences[Keys.ANISH_OWNED_ITEMS]
                ?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
        }

    suspend fun buyAnishItem(itemId: String, price: Int): Boolean {
        var purchased = false
        context.gameDataStore.edit { preferences ->
            val diamonds = preferences[Keys.ANISH_DIAMONDS] ?: 0
            val owned = preferences[Keys.ANISH_OWNED_ITEMS]
                ?.split(",")?.filter { it.isNotBlank() }?.toMutableSet() ?: mutableSetOf()
            if (itemId !in owned && diamonds >= price) {
                owned.add(itemId)
                preferences[Keys.ANISH_DIAMONDS] = diamonds - price
                preferences[Keys.ANISH_OWNED_ITEMS] = owned.joinToString(",")
                purchased = true
            }
        }
        return purchased
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
