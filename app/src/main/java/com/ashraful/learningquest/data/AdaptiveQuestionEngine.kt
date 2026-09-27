package com.ashraful.learningquest.data

import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Selects curated questions according to learner history.
 *
 * The engine deliberately combines:
 * - weak-topic targeting
 * - review scheduling
 * - wrong-answer recovery
 * - recent-question cooldown
 * - weighted randomness
 */
object AdaptiveQuestionEngine {

    private const val CORRECT_COOLDOWN_SECONDS = 30 * 60L
    private const val WRONG_COOLDOWN_SECONDS = 5 * 60L

    fun select(
        questions: List<BankQuestion>,
        progress: Map<String, QuestionProgress>,
        count: Int,
        subject: String? = null,
        topic: String? = null,
        difficulty: Int? = null,
        excludeIds: Set<String> = emptySet()
    ): List<BankQuestion> {
        if (count <= 0) return emptyList()

        val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
        val now = System.currentTimeMillis() / 1000L

        val basePool = questions
            .asSequence()
            .filter { it.id !in excludeIds }
            .filter { subject == null || it.subject.equals(subject, ignoreCase = true) }
            .filter { topic == null || it.topic.equals(topic, ignoreCase = true) }
            .filter { difficulty == null || it.difficulty == difficulty }
            .toList()

        if (basePool.isEmpty()) return emptyList()

        // Keep recently answered questions out when there are enough alternatives.
        val freshPool = basePool.filter {
            !isRecentlyAnswered(progress[it.id], now)
        }

        val pool = if (freshPool.size >= minOf(count, basePool.size)) {
            freshPool
        } else {
            basePool
        }

        val selected = mutableListOf<BankQuestion>()
        val remaining = pool.toMutableList()
        val topicWeakness = topicWeakness(progress, basePool)

        repeat(minOf(count, remaining.size)) {
            val weights = remaining.map {
                selectionWeight(
                    question = it,
                    progress = progress[it.id],
                    today = today,
                    topicWeakness = topicWeakness[it.topic] ?: 0
                )
            }

            val index = weightedIndex(weights)
            selected += remaining.removeAt(index)
        }

        return selected
    }

    private fun isRecentlyAnswered(
        progress: QuestionProgress?,
        now: Long
    ): Boolean {
        if (progress == null || progress.attempts == 0) return false
        if (progress.lastAnsweredEpochSecond <= 0L) return false

        val cooldown = if (progress.consecutiveCorrect == 0) {
            WRONG_COOLDOWN_SECONDS
        } else {
            CORRECT_COOLDOWN_SECONDS
        }

        return now - progress.lastAnsweredEpochSecond in 0 until cooldown
    }

    private fun topicWeakness(
        progress: Map<String, QuestionProgress>,
        pool: List<BankQuestion>
    ): Map<String, Int> {
        return pool.groupBy { it.topic }.mapValues { (_, questions) ->
            val attempted = questions.mapNotNull { progress[it.id] }
                .filter { it.attempts > 0 }

            if (attempted.isEmpty()) {
                0
            } else {
                val totalAttempts = attempted.sumOf { it.attempts }.coerceAtLeast(1)
                val totalCorrect = attempted.sumOf { it.correct }
                val accuracy = totalCorrect * 100.0 / totalAttempts

                // Lower accuracy = higher topic priority.
                (100.0 - accuracy).roundToInt().coerceIn(0, 100)
            }
        }
    }

    private fun selectionWeight(
        question: BankQuestion,
        progress: QuestionProgress?,
        today: Long,
        topicWeakness: Int
    ): Int {
        if (progress == null || progress.attempts == 0) {
            return 100 + topicWeakness
        }

        if (progress.wrong > progress.correct &&
            progress.consecutiveCorrect == 0
        ) {
            return 150 + topicWeakness
        }

        if (progress.nextReviewEpochDay <= today) {
            return 110 + topicWeakness
        }

        return when {
            progress.consecutiveCorrect >= 3 -> 8
            progress.consecutiveCorrect == 2 -> 25 + topicWeakness / 3
            progress.consecutiveCorrect == 1 -> 50 + topicWeakness / 2
            progress.mastery == 0 -> 90 + topicWeakness
            else -> 35 + topicWeakness / 2
        }
    }

    private fun weightedIndex(weights: List<Int>): Int {
        val total = weights.sum().coerceAtLeast(1)
        var target = Random.nextInt(total)

        weights.forEachIndexed { index, weight ->
            target -= weight
            if (target < 0) return index
        }

        return weights.lastIndex
    }

    fun mixedTest(
        questions: List<BankQuestion>,
        progress: Map<String, QuestionProgress>,
        count: Int = 20
    ): List<BankQuestion> =
        select(
            questions = questions,
            progress = progress,
            count = count
        )

    fun subjectTest(
        subject: String,
        progress: Map<String, QuestionProgress>,
        count: Int = 10
    ): List<BankQuestion> =
        select(
            questions = QuestionBank.all,
            progress = progress,
            count = count,
            subject = subject
        )
}
