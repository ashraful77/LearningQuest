package com.ashraful.learningquest.data

data class Question(
    val question: String,
    val options: List<String>,
    val answer: String
)

data class QuizSection(
    val name: String,
    val questions: List<Question>
)

val englishSections = listOf(

    QuizSection("Vocabulary", listOf(
        Question("A bird can ___", listOf("fly", "read", "write"), "fly"),
        Question("A cat says ___", listOf("meow", "moo", "bark"), "meow"),
        Question("A dog says ___", listOf("bark", "roar", "meow"), "bark"),
        Question("A cow gives us ___", listOf("milk", "juice", "water"), "milk"),
        Question("We use our eyes to ___", listOf("see", "hear", "smell"), "see"),
        Question("We use our ears to ___", listOf("hear", "see", "taste"), "hear"),
        Question("Which is a fruit?", listOf("Apple", "Carrot", "Potato"), "Apple"),
        Question("Which is an animal?", listOf("Tiger", "Table", "Chair"), "Tiger"),
        Question("Which is a bird?", listOf("Parrot", "Lion", "Fish"), "Parrot"),
        Question("Which is a vehicle?", listOf("Car", "Tree", "House"), "Car")
    )),

    QuizSection("Opposites", listOf(
        Question("Opposite of HOT?", listOf("Cold", "Big", "Fast"), "Cold"),
        Question("Opposite of BIG?", listOf("Small", "Tall", "Long"), "Small"),
        Question("Opposite of HAPPY?", listOf("Sad", "Fast", "Bright"), "Sad"),
        Question("Opposite of FAST?", listOf("Slow", "Quick", "Early"), "Slow"),
        Question("Opposite of OLD?", listOf("Young", "Big", "Long"), "Young"),
        Question("Opposite of DAY?", listOf("Night", "Morning", "Light"), "Night"),
        Question("Opposite of UP?", listOf("Down", "Over", "High"), "Down"),
        Question("Opposite of OPEN?", listOf("Closed", "Wide", "Big"), "Closed"),
        Question("Opposite of FULL?", listOf("Empty", "Heavy", "Large"), "Empty"),
        Question("Opposite of CLEAN?", listOf("Dirty", "Fresh", "Bright"), "Dirty")
    )),

    QuizSection("Grammar", listOf(
        Question("He ___ to school every day.", listOf("goes", "go", "going"), "goes"),
        Question("They ___ football.", listOf("play", "plays", "playing"), "play"),
        Question("I ___ a student.", listOf("am", "is", "are"), "am"),
        Question("She ___ happy.", listOf("is", "am", "are"), "is"),
        Question("We ___ friends.", listOf("are", "is", "am"), "are"),
        Question("He ___ a bicycle.", listOf("has", "have", "having"), "has"),
        Question("They ___ two cats.", listOf("have", "has", "having"), "have"),
        Question("She ___ very fast.", listOf("runs", "run", "running"), "runs"),
        Question("I ___ my homework.", listOf("do", "does", "doing"), "do"),
        Question("The boys ___ cricket.", listOf("play", "plays", "playing"), "play")
    )),

    QuizSection("Spelling", listOf(
        Question("Correct spelling?", listOf("Beautiful", "Beautifull", "Beutiful"), "Beautiful"),
        Question("Correct spelling?", listOf("School", "Skool", "Scool"), "School"),
        Question("Correct spelling?", listOf("Friend", "Freind", "Frend"), "Friend"),
        Question("Correct spelling?", listOf("Morning", "Mornning", "Mornin"), "Morning"),
        Question("Correct spelling?", listOf("Because", "Becuase", "Becaus"), "Because"),
        Question("Correct spelling?", listOf("Library", "Libary", "Librery"), "Library"),
        Question("Correct spelling?", listOf("Teacher", "Techer", "Teachar"), "Teacher"),
        Question("Correct spelling?", listOf("Animal", "Anemal", "Animel"), "Animal"),
        Question("Correct spelling?", listOf("Children", "Childern", "Childrens"), "Children"),
        Question("Correct spelling?", listOf("Computer", "Comuter", "Computar"), "Computer")
    )),

    QuizSection("Parts of Speech", listOf(
        Question("Which is a noun?", listOf("Teacher", "Quickly", "Run"), "Teacher"),
        Question("Which is a verb?", listOf("Jump", "Beautiful", "School"), "Jump"),
        Question("Which is an adjective?", listOf("Beautiful", "Run", "School"), "Beautiful"),
        Question("Which is an adverb?", listOf("Quickly", "Happy", "Book"), "Quickly"),
        Question("Which is a pronoun?", listOf("He", "Table", "Run"), "He"),
        Question("Which is a noun?", listOf("Garden", "Slowly", "Jump"), "Garden"),
        Question("Which is a verb?", listOf("Sing", "Happy", "Blue"), "Sing"),
        Question("Which is an adjective?", listOf("Tall", "Run", "School"), "Tall"),
        Question("Which is an adverb?", listOf("Slowly", "House", "Red"), "Slowly"),
        Question("Which is a pronoun?", listOf("They", "Teacher", "Walk"), "They")
    )),

    QuizSection("Tenses", listOf(
        Question("Past tense of GO?", listOf("Went", "Goed", "Going"), "Went"),
        Question("Past tense of EAT?", listOf("Ate", "Eated", "Eating"), "Ate"),
        Question("Past tense of SEE?", listOf("Saw", "Seed", "Seeing"), "Saw"),
        Question("Past tense of COME?", listOf("Came", "Comed", "Coming"), "Came"),
        Question("Past tense of WRITE?", listOf("Wrote", "Writed", "Writing"), "Wrote"),
        Question("Past tense of RUN?", listOf("Ran", "Runned", "Running"), "Ran"),
        Question("Past tense of DRINK?", listOf("Drank", "Drinked", "Drinking"), "Drank"),
        Question("Past tense of SING?", listOf("Sang", "Singed", "Singing"), "Sang"),
        Question("Past tense of TAKE?", listOf("Took", "Taked", "Taking"), "Took"),
        Question("Past tense of MAKE?", listOf("Made", "Maked", "Making"), "Made")
    )),

    QuizSection("Sentence", listOf(
        Question("Which sentence is correct?", listOf("He is tall.", "He are tall.", "He am tall."), "He is tall."),
        Question("Which sentence is correct?", listOf("They are happy.", "They is happy.", "They am happy."), "They are happy."),
        Question("Which sentence is correct?", listOf("I am ready.", "I is ready.", "I are ready."), "I am ready."),
        Question("Which sentence is correct?", listOf("She likes mangoes.", "She like mangoes.", "She liking mangoes."), "She likes mangoes."),
        Question("Which sentence is correct?", listOf("We play cricket.", "We plays cricket.", "We playing cricket."), "We play cricket."),
        Question("Which sentence is correct?", listOf("He has a pen.", "He have a pen.", "He having a pen."), "He has a pen."),
        Question("Which sentence is correct?", listOf("I like apples.", "I likes apples.", "I liking apples."), "I like apples."),
        Question("Which sentence is correct?", listOf("She is reading.", "She are reading.", "She am reading."), "She is reading."),
        Question("Which sentence is correct?", listOf("The sun is bright.", "The sun are bright.", "The sun am bright."), "The sun is bright."),
        Question("Which sentence is correct?", listOf("They have a car.", "They has a car.", "They having a car."), "They have a car.")
    )),

    QuizSection("Plurals", listOf(
        Question("Plural of CAT?", listOf("Cats", "Cates", "Cat"), "Cats"),
        Question("Plural of BOX?", listOf("Boxes", "Boxs", "Box"), "Boxes"),
        Question("Plural of CHILD?", listOf("Children", "Childs", "Childes"), "Children"),
        Question("Plural of MAN?", listOf("Men", "Mans", "Manes"), "Men"),
        Question("Plural of TOOTH?", listOf("Teeth", "Tooths", "Toothes"), "Teeth"),
        Question("Plural of BOOK?", listOf("Books", "Bookes", "Book"), "Books"),
        Question("Plural of BUS?", listOf("Buses", "Buss", "Bus"), "Buses"),
        Question("Plural of FOOT?", listOf("Feet", "Foots", "Feets"), "Feet"),
        Question("Plural of WOMAN?", listOf("Women", "Womans", "Womanes"), "Women"),
        Question("Plural of MOUSE?", listOf("Mice", "Mouses", "Mousees"), "Mice")
    )),

    QuizSection("Articles", listOf(
        Question("I have ___ apple.", listOf("an", "a", "the"), "an"),
        Question("She has ___ dog.", listOf("a", "an", "are"), "a"),
        Question("He is ___ honest boy.", listOf("an", "a", "the"), "an"),
        Question("This is ___ orange.", listOf("an", "a", "are"), "an"),
        Question("I saw ___ elephant.", listOf("an", "a", "is"), "an"),
        Question("She has ___ umbrella.", listOf("an", "a", "are"), "an"),
        Question("He bought ___ book.", listOf("a", "an", "are"), "a"),
        Question("I saw ___ tiger.", listOf("a", "an", "is"), "a"),
        Question("She ate ___ egg.", listOf("an", "a", "the"), "an"),
        Question("He is ___ good boy.", listOf("a", "an", "are"), "a")
    )),

    QuizSection("Rhyming Words", listOf(
        Question("Which word rhymes with CAT?", listOf("Bat", "Dog", "Sun"), "Bat"),
        Question("Which word rhymes with SUN?", listOf("Fun", "Cat", "Bed"), "Fun"),
        Question("Which word rhymes with BALL?", listOf("Tall", "Tree", "Fish"), "Tall"),
        Question("Which word rhymes with STAR?", listOf("Car", "Book", "Pen"), "Car"),
        Question("Which word rhymes with LIGHT?", listOf("Night", "Dog", "Cup"), "Night"),
        Question("Which word rhymes with HEN?", listOf("Pen", "Dog", "Sun"), "Pen"),
        Question("Which word rhymes with TREE?", listOf("Bee", "Cat", "Run"), "Bee"),
        Question("Which word rhymes with FOX?", listOf("Box", "Sun", "Car"), "Box"),
        Question("Which word rhymes with CAKE?", listOf("Lake", "Dog", "Pen"), "Lake"),
        Question("Which word rhymes with MOON?", listOf("Spoon", "Cat", "Fish"), "Spoon")
    ))
)

val scienceSections = listOf(

    QuizSection("Our Earth", listOf(
        Question("Which planet do we live on?", listOf("Earth", "Mars", "Jupiter"), "Earth"),
        Question("What gives Earth light?", listOf("Sun", "Moon", "Cloud"), "Sun"),
        Question("How many continents are there?", listOf("7", "5", "8"), "7"),
        Question("How many oceans are there?", listOf("5", "3", "7"), "5"),
        Question("Which is the largest ocean?", listOf("Pacific", "Indian", "Atlantic"), "Pacific"),
        Question("What covers most of Earth's surface?", listOf("Water", "Trees", "Buildings"), "Water"),
        Question("What is the shape of Earth?", listOf("Round", "Square", "Triangle"), "Round"),
        Question("Which is Earth's natural satellite?", listOf("Moon", "Sun", "Mars"), "Moon"),
        Question("Where do we live?", listOf("Earth", "Moon", "Sun"), "Earth"),
        Question("What do we breathe?", listOf("Air", "Stone", "Sand"), "Air")
    )),

    QuizSection("Plants", listOf(
        Question("What do plants need to make food?", listOf("Sunlight", "Plastic", "Stone"), "Sunlight"),
        Question("Which part absorbs water?", listOf("Roots", "Flower", "Fruit"), "Roots"),
        Question("Which part makes food?", listOf("Leaves", "Roots", "Stem"), "Leaves"),
        Question("Which part supports the plant?", listOf("Stem", "Fruit", "Seed"), "Stem"),
        Question("What grows into a new plant?", listOf("Seed", "Stone", "Leaf"), "Seed"),
        Question("Which part attracts insects?", listOf("Flower", "Root", "Stem"), "Flower"),
        Question("What gas do plants release?", listOf("Oxygen", "Smoke", "Helium"), "Oxygen"),
        Question("Which is a tree?", listOf("Mango", "Rose", "Grass"), "Mango"),
        Question("Which is a flower?", listOf("Rose", "Potato", "Carrot"), "Rose"),
        Question("Plants need water to ___", listOf("grow", "sleep", "run"), "grow")
    )),

    QuizSection("Animals", listOf(
        Question("How many legs does a spider have?", listOf("8", "6", "4"), "8"),
        Question("Which animal gives us milk?", listOf("Cow", "Lion", "Tiger"), "Cow"),
        Question("Which animal is called the king of the jungle?", listOf("Lion", "Cow", "Rabbit"), "Lion"),
        Question("Which animal has a long trunk?", listOf("Elephant", "Horse", "Dog"), "Elephant"),
        Question("Which animal can fly?", listOf("Bird", "Dog", "Cow"), "Bird"),
        Question("Which animal lives in water?", listOf("Fish", "Tiger", "Horse"), "Fish"),
        Question("Which animal has black and white stripes?", listOf("Zebra", "Lion", "Elephant"), "Zebra"),
        Question("Which animal gives us wool?", listOf("Sheep", "Tiger", "Fish"), "Sheep"),
        Question("Which animal is known for hopping?", listOf("Kangaroo", "Cow", "Horse"), "Kangaroo"),
        Question("Which is the largest land animal?", listOf("Elephant", "Dog", "Cat"), "Elephant")
    )),

    QuizSection("Human Body", listOf(
        Question("Which organ helps us breathe?", listOf("Lungs", "Heart", "Stomach"), "Lungs"),
        Question("Which organ pumps blood?", listOf("Heart", "Lungs", "Brain"), "Heart"),
        Question("Which organ helps us think?", listOf("Brain", "Heart", "Liver"), "Brain"),
        Question("How many eyes do humans normally have?", listOf("2", "3", "4"), "2"),
        Question("How many ears do humans normally have?", listOf("2", "1", "3"), "2"),
        Question("Which body part helps us walk?", listOf("Legs", "Ears", "Eyes"), "Legs"),
        Question("Which body part helps us smell?", listOf("Nose", "Ear", "Hand"), "Nose"),
        Question("Which body part helps us taste?", listOf("Tongue", "Foot", "Eye"), "Tongue"),
        Question("What protects the brain?", listOf("Skull", "Hand", "Knee"), "Skull"),
        Question("What covers our body?", listOf("Skin", "Bone", "Blood"), "Skin")
    )),

    QuizSection("Space", listOf(
        Question("Which star is closest to Earth?", listOf("Sun", "Moon", "Mars"), "Sun"),
        Question("Which planet is known as the Red Planet?", listOf("Mars", "Earth", "Venus"), "Mars"),
        Question("Which planet is the largest?", listOf("Jupiter", "Earth", "Mars"), "Jupiter"),
        Question("Which planet has rings?", listOf("Saturn", "Earth", "Mercury"), "Saturn"),
        Question("What is Earth's natural satellite?", listOf("Moon", "Sun", "Mars"), "Moon"),
        Question("Which planet is closest to the Sun?", listOf("Mercury", "Earth", "Jupiter"), "Mercury"),
        Question("Which planet is known as the Blue Planet?", listOf("Earth", "Mars", "Venus"), "Earth"),
        Question("What do astronauts travel in?", listOf("Spacecraft", "Bus", "Train"), "Spacecraft"),
        Question("Where do astronauts travel?", listOf("Space", "Ocean", "Forest"), "Space"),
        Question("Is the Sun a star?", listOf("Yes", "No", "Maybe"), "Yes")
    ))
)

/*
 * Kept for the existing ScienceScreen.
 * We will replace that screen with section-based Science next.
 */
val scienceQuestions = scienceSections
    .first()
    .questions

val puzzleQuestions = listOf(
    Question("2 × 4 = ?", listOf("6", "8", "10", "12"), "8"),
    Question("3 × 5 = ?", listOf("12", "15", "18", "20"), "15"),
    Question("10 + 10 = ?", listOf("15", "18", "20", "25"), "20"),
    Question("18 - 9 = ?", listOf("7", "8", "9", "10"), "9"),
    Question("4 × 6 = ?", listOf("20", "22", "24", "26"), "24")
)