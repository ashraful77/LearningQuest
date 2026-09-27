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


    val mathGeometry = listOf(
        BankQuestion("MATH_GEOM_001", "Math", "Geometry", 2, "How many sides does a triangle have?", listOf("2", "3", "4", "5"), 1, "A triangle has 3 sides."),
        BankQuestion("MATH_GEOM_002", "Math", "Geometry", 2, "How many sides does a rectangle have?", listOf("3", "4", "5", "6"), 1, "A rectangle has 4 sides."),
        BankQuestion("MATH_GEOM_003", "Math", "Geometry", 3, "How many corners does a square have?", listOf("2", "3", "4", "5"), 2, "A square has 4 corners."),
        BankQuestion("MATH_MEASURE_001", "Math", "Measurement", 2, "Which unit is commonly used to measure the length of a pencil?", listOf("Kilometre", "Metre", "Centimetre", "Litre"), 2, "A pencil is conveniently measured in centimetres."),
        BankQuestion("MATH_MONEY_001", "Math", "Money", 2, "A pencil costs ₹8. How much do 3 pencils cost?", listOf("₹16", "₹20", "₹24", "₹32"), 2, "8 × 3 = 24."),
        BankQuestion("MATH_TIME_001", "Math", "Time", 2, "How many minutes are there in one hour?", listOf("30", "45", "60", "100"), 2, "One hour has 60 minutes.")
    )

    val englishGrammar = listOf(
        BankQuestion("ENG_GRAMMAR_002", "English", "Grammar", 2, "Choose the correct word: She ___ a book.", listOf("read", "reads", "reading", "are read"), 1, "With 'she', the verb is 'reads'."),
        BankQuestion("ENG_GRAMMAR_003", "English", "Grammar", 2, "Choose the correct word: They ___ playing.", listOf("is", "am", "are", "was"), 2, "'They' takes 'are'."),
        BankQuestion("ENG_GRAMMAR_004", "English", "Grammar", 3, "Which word is a pronoun?", listOf("Rita", "School", "They", "Beautiful"), 2, "'They' is a pronoun."),
        BankQuestion("ENG_GRAMMAR_005", "English", "Grammar", 3, "Which word is an adjective?", listOf("Quickly", "Beautiful", "Run", "Teacher"), 1, "'Beautiful' describes a noun."),
        BankQuestion("ENG_VOCAB_002", "English", "Vocabulary", 2, "Which word means the same as 'big'?", listOf("Small", "Large", "Short", "Thin"), 1, "'Large' means big."),
        BankQuestion("ENG_VOCAB_003", "English", "Vocabulary", 2, "Which word is the opposite of 'early'?", listOf("Fast", "Late", "Soon", "First"), 1, "The opposite of early is late.")
    )

    val sciencePlants = listOf(
        BankQuestion("SCI_PLANT_002", "Science", "Plants", 2, "Which part of a plant makes most of its food?", listOf("Root", "Leaf", "Flower", "Seed"), 1, "Leaves make food using sunlight."),
        BankQuestion("SCI_PLANT_003", "Science", "Plants", 2, "What do roots mainly take from the soil?", listOf("Water and minerals", "Sunlight", "Air only", "Flowers"), 0, "Roots absorb water and minerals."),
        BankQuestion("SCI_SPACE_001", "Science", "Space", 2, "Which planet is called the Red Planet?", listOf("Earth", "Mars", "Venus", "Jupiter"), 1, "Mars appears reddish because of iron-rich material on its surface."),
        BankQuestion("SCI_SPACE_002", "Science", "Space", 2, "What is Earth's natural satellite?", listOf("Sun", "Mars", "Moon", "Venus"), 2, "The Moon is Earth's natural satellite."),
        BankQuestion("SCI_BODY_002", "Science", "Human Body", 2, "Which organ helps us think?", listOf("Heart", "Brain", "Lungs", "Stomach"), 1, "The brain controls thinking and many body functions."),
        BankQuestion("SCI_BODY_003", "Science", "Human Body", 2, "Which organ helps us breathe?", listOf("Heart", "Brain", "Lungs", "Kidney"), 2, "The lungs help us take in oxygen.")
    )

    val all: List<BankQuestion>
        get() = mathWordProblems + mathGeometry + english + englishGrammar + science + sciencePlants

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
