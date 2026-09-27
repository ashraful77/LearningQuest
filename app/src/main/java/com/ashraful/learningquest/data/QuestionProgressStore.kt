package com.ashraful.learningquest.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.ZoneId

private val Context.questionProgressStore by preferencesDataStore(
    name = "question_progress"
)

/**
 * Persistent learner history for curated question-bank questions.
 *
 * Each question keeps its own progress. The static QuestionBank never changes
 * when the learner answers a question.
 */
class QuestionProgressStore(private val context: Context) {

    private val progressKey = stringPreferencesKey("progress_json")

    val progress: Flow<Map<String, QuestionProgress>> =
        context.questionProgressStore.data.map { preferences ->
            decode(preferences[progressKey].orEmpty())
        }

    suspend fun recordAnswer(questionId: String, correct: Boolean) {
        val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay()

        context.questionProgressStore.edit { preferences ->
            val current = decode(preferences[progressKey].orEmpty())
            val previous = current[questionId] ?: QuestionProgress(questionId)

            val updated = QuestionProgressEngine.recordAnswer(
                previous = previous,
                correct = correct,
                todayEpochDay = today,
                answeredEpochSecond = now
            )

            val next = current.toMutableMap()
            next[questionId] = updated
            preferences[progressKey] = encode(next)
        }
    }

    suspend fun resetQuestion(questionId: String) {
        context.questionProgressStore.edit { preferences ->
            val current = decode(preferences[progressKey].orEmpty()).toMutableMap()
            current.remove(questionId)
            preferences[progressKey] = encode(current)
        }
    }

    suspend fun resetAll() {
        context.questionProgressStore.edit { preferences ->
            preferences.remove(progressKey)
        }
    }

    private fun encode(progress: Map<String, QuestionProgress>): String {
        val array = JSONArray()

        progress.values.forEach { item ->
            array.put(
                JSONObject().apply {
                    put("questionId", item.questionId)
                    put("attempts", item.attempts)
                    put("correct", item.correct)
                    put("wrong", item.wrong)
                    put("consecutiveCorrect", item.consecutiveCorrect)
                    put("mastery", item.mastery)
                    put("lastAnsweredEpochDay", item.lastAnsweredEpochDay)
                    put("lastAnsweredEpochSecond", item.lastAnsweredEpochSecond)
                    put("nextReviewEpochDay", item.nextReviewEpochDay)
                }
            )
        }

        return array.toString()
    }

    private fun decode(raw: String): Map<String, QuestionProgress> {
        if (raw.isBlank()) return emptyMap()

        return runCatching {
            val array = JSONArray(raw)
            buildMap {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    val progress = QuestionProgress(
                        questionId = item.getString("questionId"),
                        attempts = item.optInt("attempts", 0),
                        correct = item.optInt("correct", 0),
                        wrong = item.optInt("wrong", 0),
                        consecutiveCorrect = item.optInt("consecutiveCorrect", 0),
                        mastery = item.optInt("mastery", 0),
                        lastAnsweredEpochDay = item.optLong("lastAnsweredEpochDay", 0L),
                        lastAnsweredEpochSecond = item.optLong("lastAnsweredEpochSecond", 0L),
                        nextReviewEpochDay = item.optLong("nextReviewEpochDay", 0L)
                    )
                    put(progress.questionId, progress)
                }
            }
        }.getOrElse { emptyMap() }
    }
}
