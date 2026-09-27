package com.ashraful.learningquest.data

/**
 * Curated question-bank question.
 *
 * Unlike auto-generated Maths questions, every bank question has a
 * permanent ID so the app can remember the learner's history.
 */
data class BankQuestion(
    val id: String,
    val subject: String,
    val topic: String,
    val difficulty: Int,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String = "",
    val familyId: String? = null
) {
    init {
        require(id.isNotBlank()) { "Question ID cannot be blank" }
        require(options.size == 4) { "Bank questions must have exactly 4 options: $id" }
        require(correctIndex in 0..3) { "Invalid correct option for: $id" }
        require(difficulty in 1..10) { "Difficulty must be 1..10: $id" }
    }

    val correctAnswer: String
        get() = options[correctIndex]
}

/**
 * Learner-specific history for one curated question.
 *
 * This is intentionally separate from BankQuestion so the question bank
 * stays static while learner progress changes over time.
 */
data class QuestionProgress(
    val questionId: String,
    val attempts: Int = 0,
    val correct: Int = 0,
    val wrong: Int = 0,
    val consecutiveCorrect: Int = 0,
    val mastery: Int = 0,
    val lastAnsweredEpochDay: Long = 0L,
    val nextReviewEpochDay: Long = 0L
) {
    val accuracy: Int
        get() = if (attempts == 0) 0 else (correct * 100) / attempts

    val isMastered: Boolean
        get() = consecutiveCorrect >= 3
}

/**
 * Central question bank.
 *
 * Add new curated questions here with a permanent unique ID.
 * Auto-generated Maths questions remain in MathScreen and do not use this bank.
 */
object QuestionBank {

    val mathWordProblems = listOf(
        BankQuestion(
            id = "MATH_WORD_001",
            subject = "Math",
            topic = "Addition",
            difficulty = 2,
            prompt = "Arifa has 24 stickers and gets 18 more. How many stickers does she have now?",
            options = listOf("32", "40", "42", "44"),
            correctIndex = 2,
            explanation = "24 + 18 = 42."
        ),
        BankQuestion(
            id = "MATH_WORD_002",
            subject = "Math",
            topic = "Subtraction",
            difficulty = 2,
            prompt = "There are 50 books on a shelf. 17 are taken away. How many books remain?",
            options = listOf("23", "33", "37", "43"),
            correctIndex = 1,
            explanation = "50 - 17 = 33."
        ),
        BankQuestion(
            id = "MATH_WORD_003",
            subject = "Math",
            topic = "Multiplication",
            difficulty = 3,
            prompt = "There are 6 boxes with 4 pencils in each box. How many pencils are there?",
            options = listOf("10", "20", "24", "28"),
            correctIndex = 2,
            explanation = "6 × 4 = 24."
        ),
        BankQuestion(
            id = "MATH_WORD_004",
            subject = "Math",
            topic = "Division",
            difficulty = 3,
            prompt = "24 candies are shared equally among 6 children. How many candies does each child get?",
            options = listOf("3", "4", "5", "6"),
            correctIndex = 1,
            explanation = "24 ÷ 6 = 4."
        )
    )

    val english = listOf(
        BankQuestion(
            id = "ENG_VOCAB_001",
            subject = "English",
            topic = "Vocabulary",
            difficulty = 2,
            prompt = "Which word means the opposite of 'happy'?",
            options = listOf("Bright", "Sad", "Fast", "Kind"),
            correctIndex = 1,
            explanation = "The opposite of happy is sad."
        ),
        BankQuestion(
            id = "ENG_GRAMMAR_001",
            subject = "English",
            topic = "Grammar",
            difficulty = 2,
            prompt = "Choose the correct sentence.",
            options = listOf(
                "She are happy.",
                "She am happy.",
                "She is happy.",
                "She be happy."
            ),
            correctIndex = 2,
            explanation = "We use 'is' with 'she'."
        )
    )

    val science = listOf(
        BankQuestion(
            id = "SCI_PLANT_001",
            subject = "Science",
            topic = "Plants",
            difficulty = 2,
            prompt = "Which part of a plant usually absorbs water from the soil?",
            options = listOf("Flower", "Root", "Fruit", "Leaf"),
            correctIndex = 1,
            explanation = "Roots absorb water and minerals from the soil."
        ),
        BankQuestion(
            id = "SCI_BODY_001",
            subject = "Science",
            topic = "Human Body",
            difficulty = 2,
            prompt = "Which organ pumps blood around the body?",
            options = listOf("Brain", "Lungs", "Heart", "Stomach"),
            correctIndex = 2,
            explanation = "The heart pumps blood around the body."
        )
    )

    val all: List<BankQuestion>
        get() = mathWordProblems + english + science

    fun bySubject(subject: String): List<BankQuestion> =
        all.filter { it.subject.equals(subject, ignoreCase = true) }

    fun findById(id: String): BankQuestion? =
        all.firstOrNull { it.id == id }
}

/**
 * Calculates the next learner state after an answer.
 *
 * 3 consecutive correct answers = mastered.
 * A wrong answer resets the consecutive streak and reduces mastery.
 */
object QuestionProgressEngine {

    fun recordAnswer(
        previous: QuestionProgress,
        correct: Boolean,
        todayEpochDay: Long
    ): QuestionProgress {
        val attempts = previous.attempts + 1

        return if (correct) {
            val consecutive = previous.consecutiveCorrect + 1
            val mastery = (previous.mastery + 1).coerceAtMost(3)
            val reviewGap = when {
                consecutive >= 3 -> 14L
                consecutive == 2 -> 7L
                else -> 3L
            }

            previous.copy(
                attempts = attempts,
                correct = previous.correct + 1,
                consecutiveCorrect = consecutive,
                mastery = mastery,
                lastAnsweredEpochDay = todayEpochDay,
                nextReviewEpochDay = todayEpochDay + reviewGap
            )
        } else {
            previous.copy(
                attempts = attempts,
                wrong = previous.wrong + 1,
                consecutiveCorrect = 0,
                mastery = (previous.mastery - 1).coerceAtLeast(0),
                lastAnsweredEpochDay = todayEpochDay,
                nextReviewEpochDay = todayEpochDay + 1
            )
        }
    }
}
