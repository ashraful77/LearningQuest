package com.ashraful.learningquest.data

import java.time.LocalDate
import java.time.ZoneId
import kotlin.random.Random

/**
 * Selects curated questions according to learner history.
 *
 * Priority:
 * 1. Never answered questions
 * 2. Questions due for review
 * 3. Questions answered incorrectly
 * 4. Questions with low mastery
 * 5. Mastered questions only occasionally
 *
 * Weighted randomness prevents the same predictable order every time.
 */
object AdaptiveQuestionEngine {

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

        val pool = questions
            .asSequence()
            .filter { it.id !in excludeIds }
            .filter { subject == null || it.subject.equals(subject, ignoreCase = true) }
            .filter { topic == null || it.topic.equals(topic, ignoreCase = true) }
            .filter { difficulty == null || it.difficulty == difficulty }
            .toList()

        if (pool.isEmpty()) return emptyList()

        val selected = mutableListOf<BankQuestion>()
        val remaining = pool.toMutableList()

        repeat(minOf(count, remaining.size)) {
            val weights = remaining.map {
                selectionWeight(
                    question = it,
                    progress = progress[it.id],
                    today = today
                )
            }

            val index = weightedIndex(weights)
            selected += remaining.removeAt(index)
        }

        return selected
    }

    private fun selectionWeight(
        question: BankQuestion,
        progress: QuestionProgress?,
        today: Long
    ): Int {
        if (progress == null || progress.attempts == 0) {
            return 100
        }

        if (progress.wrong > progress.correct &&
            progress.consecutiveCorrect == 0
        ) {
            return 120
        }

        if (progress.nextReviewEpochDay <= today) {
            return 90
        }

        return when {
            progress.consecutiveCorrect >= 3 -> 8
            progress.consecutiveCorrect == 2 -> 25
            progress.consecutiveCorrect == 1 -> 45
            progress.mastery == 0 -> 80
            else -> 35
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
