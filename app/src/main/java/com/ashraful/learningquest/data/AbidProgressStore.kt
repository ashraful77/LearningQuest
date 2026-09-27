package com.ashraful.learningquest.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId

private val Context.abidProgressStore by preferencesDataStore(name = "abid_learning_progress")

data class AbidProgress(
    val stars: Int = 0,
    val xp: Int = 0,
    val streak: Int = 0,
    val totalActivities: Int = 0,
    val letters: Int = 0,
    val numbers: Int = 0,
    val colors: Int = 0,
    val shapes: Int = 0,
    val world: Int = 0,
    val games: Int = 0,
    val todayActivities: Int = 0
) {
    val level: Int get() = (xp / 50) + 1
    val overallProgress: Int
        get() = ((letters + numbers + colors + shapes + world + games) / 6).coerceIn(0, 100)
}

class AbidProgressStore(private val context: Context) {
    private object Keys {
        val STARS = intPreferencesKey("stars")
        val XP = intPreferencesKey("xp")
        val STREAK = intPreferencesKey("streak")
        val TOTAL = intPreferencesKey("total_activities")
        val LETTERS = intPreferencesKey("letters")
        val NUMBERS = intPreferencesKey("numbers")
        val COLORS = intPreferencesKey("colors")
        val SHAPES = intPreferencesKey("shapes")
        val WORLD = intPreferencesKey("world")
        val GAMES = intPreferencesKey("games")
        val TODAY = intPreferencesKey("today_activities")
        val LAST_DAY = intPreferencesKey("last_day")
    }

    val progress: Flow<AbidProgress> = context.abidProgressStore.data.map { p ->
        val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay().toInt()
        val lastDay = p[Keys.LAST_DAY] ?: 0
        AbidProgress(
            stars = p[Keys.STARS] ?: 0,
            xp = p[Keys.XP] ?: 0,
            streak = p[Keys.STREAK] ?: 0,
            totalActivities = p[Keys.TOTAL] ?: 0,
            letters = p[Keys.LETTERS] ?: 0,
            numbers = p[Keys.NUMBERS] ?: 0,
            colors = p[Keys.COLORS] ?: 0,
            shapes = p[Keys.SHAPES] ?: 0,
            world = p[Keys.WORLD] ?: 0,
            games = p[Keys.GAMES] ?: 0,
            todayActivities = if (lastDay == today) p[Keys.TODAY] ?: 0 else 0
        )
    }

    suspend fun completeActivity(category: String, stars: Int = 1, xp: Int = 5) {
        context.abidProgressStore.edit { p ->
            val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay().toInt()
            val lastDay = p[Keys.LAST_DAY] ?: 0
            val newStreak = when {
                lastDay == today -> p[Keys.STREAK] ?: 0
                lastDay == today - 1 -> (p[Keys.STREAK] ?: 0) + 1
                else -> 1
            }
            val todayCount = if (lastDay == today) p[Keys.TODAY] ?: 0 else 0
            p[Keys.STARS] = (p[Keys.STARS] ?: 0) + stars
            p[Keys.XP] = (p[Keys.XP] ?: 0) + xp
            p[Keys.STREAK] = newStreak
            p[Keys.TOTAL] = (p[Keys.TOTAL] ?: 0) + 1
            p[Keys.TODAY] = (todayCount + 1).coerceAtMost(5)
            p[Keys.LAST_DAY] = today

            val key = when (category.lowercase()) {
                "letters" -> Keys.LETTERS
                "numbers" -> Keys.NUMBERS
                "colors" -> Keys.COLORS
                "shapes" -> Keys.SHAPES
                "world" -> Keys.WORLD
                "games" -> Keys.GAMES
                else -> null
            }
            if (key != null) p[key] = ((p[key] ?: 0) + 10).coerceAtMost(100)
        }
    }
}
