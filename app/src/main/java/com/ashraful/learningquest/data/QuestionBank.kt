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
    val lastAnsweredEpochSecond: Long = 0L,
    val nextReviewEpochDay: Long = 0L
) {
    val accuracy: Int
        get() = if (attempts == 0) 0 else (correct * 100) / attempts

    val isMastered: Boolean
        get() = consecutiveCorrect >= 3
}



/**
 * Snapshot of learner performance by topic.
 * Used by the Learning Hub to explain where practice is needed.
 */
data class TopicProgress(
    val topic: String,
    val attempts: Int,
    val correct: Int,
    val accuracy: Int,
    val masteredQuestions: Int,
    val totalQuestions: Int
) {
    val isWeak: Boolean
        get() = attempts >= 2 && accuracy < 70

    val isStrong: Boolean
        get() = attempts >= 3 && accuracy >= 85
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


    val mathMore = listOf(
        BankQuestion("MATH_FRAC_001", "Math", "Fractions", 3, "Which fraction is equal to one half?", listOf("1/3", "2/4", "2/3", "3/4"), 1, "2/4 simplifies to 1/2."),
        BankQuestion("MATH_FRAC_002", "Math", "Fractions", 3, "Which fraction is larger?", listOf("1/4", "1/2", "1/5", "1/8"), 1, "One half is larger than the other listed fractions."),
        BankQuestion("MATH_MEASURE_002", "Math", "Measurement", 3, "How many centimetres are in 1 metre?", listOf("10", "50", "100", "1000"), 2, "1 metre = 100 centimetres."),
        BankQuestion("MATH_TIME_002", "Math", "Time", 3, "A class starts at 9:00 and lasts 1 hour. When does it finish?", listOf("9:30", "10:00", "10:30", "11:00"), 1, "9:00 + 1 hour = 10:00."),
        BankQuestion("MATH_MONEY_002", "Math", "Money", 3, "You have ₹50 and spend ₹18. How much is left?", listOf("₹22", "₹28", "₹32", "₹38"), 2, "50 - 18 = 32."),
        BankQuestion("MATH_LOGIC_001", "Math", "Logic", 3, "What number comes next: 5, 10, 15, 20, ?", listOf("22", "24", "25", "30"), 2, "The pattern adds 5 each time.")
    )

    val englishMore = listOf(
        BankQuestion("ENG_GRAMMAR_006", "English", "Grammar", 2, "Choose the correct article: I saw ___ elephant.", listOf("a", "an", "thee", "no"), 1, "We use 'an' before a vowel sound."),
        BankQuestion("ENG_GRAMMAR_007", "English", "Grammar", 3, "What is the past tense of 'go'?", listOf("goed", "going", "went", "goes"), 2, "The past tense of go is went."),
        BankQuestion("ENG_GRAMMAR_008", "English", "Grammar", 3, "Which sentence uses a capital letter correctly?", listOf("my name is arifa.", "My name is Arifa.", "my Name is Arifa.", "My name is arifa."), 1, "Names and the beginning of a sentence use capital letters."),
        BankQuestion("ENG_VOCAB_004", "English", "Vocabulary", 2, "Which word means the same as 'quick'?", listOf("Slow", "Fast", "Heavy", "Quiet"), 1, "Fast means quick."),
        BankQuestion("ENG_VOCAB_005", "English", "Vocabulary", 3, "Which word is a noun?", listOf("Run", "Beautiful", "Teacher", "Quickly"), 2, "Teacher is a naming word, or noun."),
        BankQuestion("ENG_READING_001", "English", "Reading", 2, "If a story says 'Rina carried an umbrella because it was raining', why did she carry it?", listOf("It was sunny", "It was raining", "She was swimming", "She was sleeping"), 1, "The story says it was raining.")
    )

    val scienceMore = listOf(
        BankQuestion("SCI_PLANT_004", "Science", "Plants", 3, "Which part of a plant usually develops into a fruit after flowering?", listOf("Root", "Flower", "Stem", "Leaf"), 1, "In flowering plants, the ovary in the flower develops into the fruit."),
        BankQuestion("SCI_ANIMAL_001", "Science", "Animals", 2, "Which animal is commonly known as a mammal?", listOf("Frog", "Cow", "Snake", "Fish"), 1, "A cow is a mammal."),
        BankQuestion("SCI_MATTER_001", "Science", "Matter", 2, "Which state of matter has a fixed shape?", listOf("Solid", "Liquid", "Gas", "Steam only"), 0, "A solid has a fixed shape."),
        BankQuestion("SCI_MATTER_002", "Science", "Matter", 3, "What happens to ice when it is heated enough?", listOf("It freezes", "It melts", "It becomes soil", "It disappears instantly"), 1, "Ice melts into liquid water when heated."),
        BankQuestion("SCI_EARTH_001", "Science", "Earth", 2, "What gives Earth most of its light and heat?", listOf("Moon", "Sun", "Stars only", "Clouds"), 1, "The Sun provides most of Earth's light and heat."),
        BankQuestion("SCI_ENV_001", "Science", "Environment", 3, "Which action helps reduce waste?", listOf("Throwing everything away", "Reusing useful items", "Leaving taps running", "Burning all plastic"), 1, "Reusing items can reduce the amount of waste.")
    )


    val mathExpansion = listOf(
        BankQuestion("MATH_WORD_005", "Math", "Addition", 2, "A shop has 35 red balloons and 27 blue balloons. How many balloons are there altogether?", listOf("52", "62", "72", "82"), 1, "35 + 27 = 62."),
        BankQuestion("MATH_WORD_006", "Math", "Subtraction", 2, "A farmer has 64 mangoes and sells 29. How many mangoes remain?", listOf("25", "35", "45", "55"), 1, "64 - 29 = 35."),
        BankQuestion("MATH_WORD_007", "Math", "Multiplication", 3, "There are 7 rows with 5 chairs in each row. How many chairs are there?", listOf("25", "30", "35", "40"), 2, "7 × 5 = 35."),
        BankQuestion("MATH_WORD_008", "Math", "Division", 3, "32 pencils are put equally into 4 boxes. How many pencils go in each box?", listOf("6", "7", "8", "9"), 2, "32 ÷ 4 = 8."),
        BankQuestion("MATH_GEOM_004", "Math", "Geometry", 3, "Which shape has no corners?", listOf("Triangle", "Square", "Circle", "Rectangle"), 2, "A circle has no corners."),
        BankQuestion("MATH_GEOM_005", "Math", "Geometry", 3, "How many equal sides does a square have?", listOf("2", "3", "4", "5"), 2, "All 4 sides of a square are equal."),
        BankQuestion("MATH_GEOM_006", "Math", "Geometry", 3, "Which shape has exactly one curved boundary and no straight sides?", listOf("Circle", "Triangle", "Square", "Rectangle"), 0, "A circle has a curved boundary and no straight sides."),
        BankQuestion("MATH_MEASURE_003", "Math", "Measurement", 3, "Which is longer?", listOf("1 metre", "50 centimetres", "20 centimetres", "10 centimetres"), 0, "1 metre is 100 centimetres."),
        BankQuestion("MATH_MEASURE_004", "Math", "Measurement", 3, "How many millimetres are in 1 centimetre?", listOf("5", "10", "50", "100"), 1, "1 centimetre = 10 millimetres."),
        BankQuestion("MATH_MONEY_003", "Math", "Money", 3, "A toy costs ₹35 and you pay ₹50. How much change should you get?", listOf("₹5", "₹10", "₹15", "₹20"), 2, "50 - 35 = 15."),
        BankQuestion("MATH_MONEY_004", "Math", "Money", 3, "Two notebooks cost ₹25 each. What is the total cost?", listOf("₹40", "₹45", "₹50", "₹55"), 2, "25 × 2 = 50."),
        BankQuestion("MATH_TIME_003", "Math", "Time", 3, "How many hours are there in one day?", listOf("12", "18", "24", "30"), 2, "One day has 24 hours."),
        BankQuestion("MATH_TIME_004", "Math", "Time", 3, "What time is 30 minutes after 4:00?", listOf("4:15", "4:30", "5:00", "5:30"), 1, "30 minutes after 4:00 is 4:30."),
        BankQuestion("MATH_FRAC_003", "Math", "Fractions", 3, "What is one quarter written as a fraction?", listOf("1/2", "1/3", "1/4", "2/4"), 2, "One quarter is 1/4."),
        BankQuestion("MATH_FRAC_004", "Math", "Fractions", 4, "Which fraction is equal to 3/4?", listOf("2/4", "6/8", "3/8", "4/8"), 1, "3/4 = 6/8."),
        BankQuestion("MATH_LOGIC_002", "Math", "Logic", 3, "What number comes next: 2, 4, 6, 8, ?", listOf("9", "10", "11", "12"), 1, "The pattern adds 2."),
        BankQuestion("MATH_LOGIC_003", "Math", "Logic", 4, "Which number does not belong: 2, 4, 6, 9?", listOf("2", "4", "6", "9"), 3, "9 is odd while the others are even."),
        BankQuestion("MATH_LOGIC_004", "Math", "Logic", 4, "If 3 pencils cost ₹15, how much does 1 pencil cost?", listOf("₹3", "₹5", "₹8", "₹10"), 1, "15 ÷ 3 = 5."),
        BankQuestion("MATH_COMPARE_001", "Math", "Comparison", 2, "Which number is greatest?", listOf("47", "74", "57", "67"), 1, "74 is the greatest."),
        BankQuestion("MATH_COMPARE_002", "Math", "Comparison", 2, "Which number is smallest?", listOf("31", "13", "23", "33"), 1, "13 is the smallest.")
    )

    val englishExpansion = listOf(
        BankQuestion("ENG_GRAMMAR_009", "English", "Grammar", 2, "Choose the correct word: He ___ to school every day.", listOf("go", "goes", "going", "gone"), 1, "He goes to school every day."),
        BankQuestion("ENG_GRAMMAR_010", "English", "Grammar", 2, "Choose the correct word: We ___ friends.", listOf("is", "am", "are", "was"), 2, "We use 'are' with 'we'."),
        BankQuestion("ENG_GRAMMAR_011", "English", "Grammar", 3, "Which word is a verb?", listOf("Jump", "Blue", "Happy", "Garden"), 0, "Jump is an action word, or verb."),
        BankQuestion("ENG_GRAMMAR_012", "English", "Grammar", 3, "Which word is a noun?", listOf("Run", "Beautiful", "School", "Quickly"), 2, "School is a naming word, or noun."),
        BankQuestion("ENG_GRAMMAR_013", "English", "Grammar", 3, "Choose the correct plural of 'child'.", listOf("Childs", "Childes", "Children", "Childrens"), 2, "The plural of child is children."),
        BankQuestion("ENG_GRAMMAR_014", "English", "Grammar", 3, "Choose the correct sentence.", listOf("They is ready.", "They are ready.", "They am ready.", "They be ready."), 1, "They takes the verb are."),
        BankQuestion("ENG_VOCAB_006", "English", "Vocabulary", 2, "Which word means the opposite of 'hot'?", listOf("Warm", "Cold", "Dry", "Bright"), 1, "The opposite of hot is cold."),
        BankQuestion("ENG_VOCAB_007", "English", "Vocabulary", 2, "Which word means the same as 'small'?", listOf("Tiny", "Huge", "Long", "Heavy"), 0, "Tiny means very small."),
        BankQuestion("ENG_VOCAB_008", "English", "Vocabulary", 3, "Which word means the same as 'begin'?", listOf("End", "Start", "Stop", "Close"), 1, "Begin means start."),
        BankQuestion("ENG_VOCAB_009", "English", "Vocabulary", 3, "Which word is the opposite of 'clean'?", listOf("Neat", "Dirty", "Fresh", "Bright"), 1, "The opposite of clean is dirty."),
        BankQuestion("ENG_READING_002", "English", "Reading", 2, "Rafi planted a seed and watered it every day. What was he trying to grow?", listOf("A plant", "A book", "A chair", "A toy"), 0, "A seed can grow into a plant."),
        BankQuestion("ENG_READING_003", "English", "Reading", 3, "Mina wore a raincoat before going outside. What was the weather likely to be?", listOf("Rainy", "Very dry", "Snowy only", "Windless"), 0, "A raincoat is used to stay dry in rain."),
        BankQuestion("ENG_GRAMMAR_015", "English", "Grammar", 3, "Which sentence starts with a capital letter correctly?", listOf("arifa likes books.", "Arifa likes books.", "arifa Likes books.", "ARifa likes books."), 1, "A sentence and a person's name begin with capital letters."),
        BankQuestion("ENG_GRAMMAR_016", "English", "Grammar", 3, "Which word is an adverb?", listOf("Slowly", "Slow", "Runner", "Run"), 0, "Slowly tells how an action happens."),
        BankQuestion("ENG_VOCAB_010", "English", "Vocabulary", 3, "What does 'enormous' mean?", listOf("Very small", "Very large", "Very quiet", "Very young"), 1, "Enormous means very large."),
        BankQuestion("ENG_VOCAB_011", "English", "Vocabulary", 2, "Which word rhymes with 'cat'?", listOf("Sun", "Hat", "Dog", "Tree"), 1, "Cat and hat rhyme."),
        BankQuestion("ENG_READING_004", "English", "Reading", 3, "Sara had 5 apples and gave 2 to her brother. How many did she have left?", listOf("2", "3", "4", "7"), 1, "5 - 2 = 3."),
        BankQuestion("ENG_GRAMMAR_017", "English", "Grammar", 4, "Choose the correct past tense: Yesterday, I ___ a story.", listOf("read", "reads", "reading", "will read"), 0, "Read is used for the past in this sentence."),
        BankQuestion("ENG_GRAMMAR_018", "English", "Grammar", 3, "Which punctuation mark ends a question?", listOf(".", ",", "?", "!"), 2, "A question ends with a question mark."),
        BankQuestion("ENG_VOCAB_012", "English", "Vocabulary", 2, "Which word describes something that is not heavy?", listOf("Light", "Hard", "Tall", "Wide"), 0, "Light can mean not heavy.")
    )

    val scienceExpansion = listOf(
        BankQuestion("SCI_PLANT_005", "Science", "Plants", 2, "Which gas do plants use to make food?", listOf("Oxygen", "Carbon dioxide", "Helium", "Hydrogen"), 1, "Plants use carbon dioxide during photosynthesis."),
        BankQuestion("SCI_PLANT_006", "Science", "Plants", 3, "Which part usually carries water from roots toward the leaves?", listOf("Stem", "Flower", "Fruit", "Seed"), 0, "The stem transports water and minerals."),
        BankQuestion("SCI_ANIMAL_002", "Science", "Animals", 2, "Which animal lays eggs?", listOf("Cow", "Hen", "Cat", "Dog"), 1, "A hen lays eggs."),
        BankQuestion("SCI_ANIMAL_003", "Science", "Animals", 3, "Which animal is a herbivore?", listOf("Lion", "Cow", "Tiger", "Eagle"), 1, "A cow mainly eats plants."),
        BankQuestion("SCI_BODY_004", "Science", "Human Body", 2, "Which organ helps us breathe?", listOf("Lungs", "Heart", "Brain", "Stomach"), 0, "The lungs help us breathe."),
        BankQuestion("SCI_BODY_005", "Science", "Human Body", 3, "Which organ helps digest food?", listOf("Stomach", "Brain", "Ear", "Eye"), 0, "The stomach helps digest food."),
        BankQuestion("SCI_BODY_006", "Science", "Human Body", 3, "Which body part helps us hear?", listOf("Eye", "Ear", "Nose", "Hand"), 1, "The ear helps us hear."),
        BankQuestion("SCI_SPACE_003", "Science", "Space", 2, "Which star is closest to Earth?", listOf("The Moon", "The Sun", "Mars", "Jupiter"), 1, "The Sun is the closest star to Earth."),
        BankQuestion("SCI_SPACE_004", "Science", "Space", 3, "Which planet do we live on?", listOf("Mars", "Venus", "Earth", "Saturn"), 2, "We live on Earth."),
        BankQuestion("SCI_EARTH_002", "Science", "Earth", 2, "What do we call moving air?", listOf("Wind", "Rain", "Cloud", "Soil"), 0, "Moving air is called wind."),
        BankQuestion("SCI_EARTH_003", "Science", "Earth", 3, "Which covers much of Earth's surface?", listOf("Water", "Sand only", "Buildings", "Roads"), 0, "Water covers much of Earth's surface."),
        BankQuestion("SCI_MATTER_003", "Science", "Matter", 2, "Which state of matter can flow and takes the shape of its container?", listOf("Solid", "Liquid", "Rock", "Ice"), 1, "Liquids flow and take the shape of their container."),
        BankQuestion("SCI_MATTER_004", "Science", "Matter", 3, "Water turns into ice when it is?", listOf("Heated", "Frozen", "Boiled", "Burned"), 1, "Water freezes into ice when cooled enough."),
        BankQuestion("SCI_ENV_002", "Science", "Environment", 2, "Which item can usually be recycled?", listOf("Paper", "Smoke", "Sunlight", "Rain"), 0, "Paper can be recycled."),
        BankQuestion("SCI_ENV_003", "Science", "Environment", 3, "Which action saves water?", listOf("Leaving the tap open", "Turning off the tap when not needed", "Breaking pipes", "Wasting clean water"), 1, "Turning off unused taps saves water."),
        BankQuestion("SCI_ENERGY_001", "Science", "Energy", 2, "Which is a source of light?", listOf("Sun", "Stone", "Chair", "Book"), 0, "The Sun is a natural source of light."),
        BankQuestion("SCI_ENERGY_002", "Science", "Energy", 3, "Which object uses electricity?", listOf("Electric fan", "Stone", "Pencil", "Paper"), 0, "An electric fan uses electrical energy."),
        BankQuestion("SCI_WEATHER_001", "Science", "Weather", 2, "Which instrument is used to measure temperature?", listOf("Thermometer", "Ruler", "Clock", "Compass"), 0, "A thermometer measures temperature."),
        BankQuestion("SCI_WEATHER_002", "Science", "Weather", 3, "Dark clouds often suggest that what may happen?", listOf("Rain", "Summer vacation", "An earthquake", "A rainbow always"), 0, "Dark clouds can bring rain."),
        BankQuestion("SCI_MATERIALS_001", "Science", "Materials", 3, "Which material is transparent?", listOf("Clear glass", "Wood", "Brick", "Stone"), 0, "Clear glass lets light pass through.")
    )

    val all: List<BankQuestion>
        get() = mathWordProblems + mathGeometry + mathMore + mathExpansion + QuestionBankExpansion2.math + english + englishGrammar + englishMore + englishExpansion + QuestionBankExpansion2.english + science + sciencePlants + scienceMore + scienceExpansion + QuestionBankExpansion2.science

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
        todayEpochDay: Long,
        answeredEpochSecond: Long = 0L
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
                lastAnsweredEpochSecond = answeredEpochSecond,
                nextReviewEpochDay = todayEpochDay + reviewGap
            )
        } else {
            previous.copy(
                attempts = attempts,
                wrong = previous.wrong + 1,
                consecutiveCorrect = 0,
                mastery = (previous.mastery - 1).coerceAtLeast(0),
                lastAnsweredEpochDay = todayEpochDay,
                lastAnsweredEpochSecond = answeredEpochSecond,
                nextReviewEpochDay = todayEpochDay + 1
            )
        }
    }
}
