package com.ashraful.learningquest.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.GameDataStore
import com.ashraful.learningquest.data.Question
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

private data class PuzzleSection(
    val name: String,
    val questions: List<Question>
)

private val puzzleSections = listOf(

    PuzzleSection(
        "Number Puzzles",
        listOf(
            Question("2, 4, 6, ?", listOf("7", "8", "9"), "8"),
            Question("5, 10, 15, ?", listOf("18", "20", "25"), "20"),
            Question("10, 20, 30, ?", listOf("35", "40", "50"), "40"),
            Question("3, 6, 9, ?", listOf("10", "12", "15"), "12"),
            Question("1, 3, 5, ?", listOf("6", "7", "8"), "7"),
            Question("20, 18, 16, ?", listOf("14", "13", "12"), "14"),
            Question("4, 8, 12, ?", listOf("14", "16", "18"), "16"),
            Question("7, 14, 21, ?", listOf("27", "28", "30"), "28"),
            Question("30, 25, 20, ?", listOf("15", "10", "18"), "15"),
            Question("6, 12, 18, ?", listOf("22", "24", "26"), "24")
        )
    ),

    PuzzleSection(
        "Logic Puzzles",
        listOf(
            Question("Which is different?", listOf("Apple", "Mango", "Car"), "Car"),
            Question("Which is different?", listOf("Dog", "Cat", "Table"), "Table"),
            Question("Which is different?", listOf("Red", "Blue", "Banana"), "Banana"),
            Question("Which comes first?", listOf("Wake up", "Sleep", "Go home"), "Wake up"),
            Question("Which comes last?", listOf("Breakfast", "Lunch", "Dinner"), "Dinner"),
            Question("Which is the smallest?", listOf("Ant", "Elephant", "Dog"), "Ant"),
            Question("Which is the largest?", listOf("Mouse", "Cat", "Elephant"), "Elephant"),
            Question("Which does not belong?", listOf("Bus", "Car", "Apple"), "Apple"),
            Question("Which is used for writing?", listOf("Pen", "Spoon", "Cup"), "Pen"),
            Question("Which is used for cutting?", listOf("Scissors", "Pillow", "Plate"), "Scissors")
        )
    ),

    PuzzleSection(
        "Pattern Puzzles",
        listOf(
            Question("A, B, C, ?", listOf("D", "E", "F"), "D"),
            Question("B, D, F, ?", listOf("G", "H", "I"), "H"),
            Question("1, 2, 4, ?", listOf("6", "8", "10"), "8"),
            Question("2, 4, 8, ?", listOf("10", "12", "16"), "16"),
            Question("5, 10, 20, ?", listOf("25", "30", "40"), "40"),
            Question("100, 90, 80, ?", listOf("60", "70", "75"), "70"),
            Question("1, 4, 7, ?", listOf("9", "10", "11"), "10"),
            Question("10, 15, 20, ?", listOf("25", "30", "35"), "25"),
            Question("3, 5, 7, ?", listOf("8", "9", "10"), "9"),
            Question("2, 5, 8, ?", listOf("10", "11", "12"), "11")
        )
    ),

    PuzzleSection(
        "Multiplication Puzzles",
        listOf(
            Question("2 × 4 = ?", listOf("6", "8", "10"), "8"),
            Question("3 × 5 = ?", listOf("12", "15", "18"), "15"),
            Question("4 × 6 = ?", listOf("20", "24", "28"), "24"),
            Question("5 × 7 = ?", listOf("30", "35", "40"), "35"),
            Question("6 × 8 = ?", listOf("42", "48", "54"), "48"),
            Question("7 × 3 = ?", listOf("18", "21", "24"), "21"),
            Question("8 × 4 = ?", listOf("28", "32", "36"), "32"),
            Question("9 × 5 = ?", listOf("40", "45", "50"), "45"),
            Question("10 × 6 = ?", listOf("50", "60", "70"), "60"),
            Question("7 × 7 = ?", listOf("42", "49", "56"), "49")
        )
    ),

    PuzzleSection(
        "Riddles",
        listOf(
            Question(
                "I have hands but cannot clap. What am I?",
                listOf("Clock", "Dog", "Chair"),
                "Clock"
            ),
            Question(
                "I am yellow and shine in the sky. What am I?",
                listOf("Sun", "Moon", "Cloud"),
                "Sun"
            ),
            Question(
                "I have four legs but cannot walk.",
                listOf("Table", "Bird", "Fish"),
                "Table"
            ),
            Question(
                "I am full of pages but I am not a tree.",
                listOf("Book", "Ball", "Cup"),
                "Book"
            ),
            Question(
                "I can fly but I am not a bird.",
                listOf("Airplane", "Cow", "Fish"),
                "Airplane"
            ),
            Question(
                "I have teeth but cannot eat.",
                listOf("Comb", "Dog", "Cat"),
                "Comb"
            ),
            Question(
                "I am cold and made of water.",
                listOf("Ice", "Fire", "Sand"),
                "Ice"
            ),
            Question(
                "I have wheels and carry people.",
                listOf("Car", "Tree", "Book"),
                "Car"
            ),
            Question(
                "I shine at night in the sky.",
                listOf("Moon", "Tree", "Road"),
                "Moon"
            ),
            Question(
                "I am used to tell time.",
                listOf("Clock", "Shoe", "Plate"),
                "Clock"
            )
        )
    )
)

@Composable
fun PuzzleScreen(onBack: () -> Unit) {

    var selectedSection by remember {
        mutableStateOf<PuzzleSection?>(null)
    }

    val section = selectedSection

    if (section == null) {

        PuzzleSectionScreen(
            onBack = onBack,
            onSectionSelected = {
                selectedSection = it
            }
        )

    } else {

        PuzzleQuizScreen(
            section = section,
            onBack = {
                selectedSection = null
            }
        )
    }
}

@Composable
private fun PuzzleSectionScreen(
    onBack: () -> Unit,
    onSectionSelected: (PuzzleSection) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF8ED), Color.White)))
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("‹ Home")
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("🧩 Puzzles", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9A5A00))
                Text("Train your thinking with patterns, logic and riddles", fontSize = 13.sp, color = Color(0xFF71809A))
            }
        }

        Spacer(Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEED7)),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.8f)) {
                    Text("🧩", modifier = Modifier.padding(12.dp), fontSize = 22.sp)
                }
                Spacer(Modifier.width(13.dp))
                Column {
                    Text("Choose a Challenge", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9A5A00))
                    Text("10 questions • Earn XP & Coins", fontSize = 12.sp, color = Color(0xFF71809A))
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        puzzleSections.forEach { section ->
            Card(
                onClick = { onSectionSelected(section) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shape = CircleShape, color = Color(0xFFFFEED7)) {
                        Text("▶", modifier = Modifier.padding(11.dp), fontSize = 13.sp, color = Color(0xFF9A5A00))
                    }
                    Spacer(Modifier.width(13.dp))
                    Column(Modifier.weight(1f)) {
                        Text(section.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9A5A00))
                        Text("10 Questions", fontSize = 12.sp, color = Color(0xFF7A8798))
                    }
                    Text("›", fontSize = 28.sp, color = Color(0xFF9A5A00))
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Text("Complete challenges to earn rewards 🌟", fontSize = 12.sp, color = Color(0xFF8A96A8), modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
private fun PuzzleQuizScreen(
    section: PuzzleSection,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val dataStore = remember { GameDataStore(context) }
    val scope = rememberCoroutineScope()

    val gameData by dataStore.gameData.collectAsState(
        initial = null
    )

    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<String?>(null) }
    var correct by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }

    var earnedCoins by remember { mutableIntStateOf(0) }
    var earnedXp by remember { mutableIntStateOf(0) }

    val question = section.questions[index]
    val shuffledOptions = remember(section, index) { question.options.shuffled() }

    val infiniteTransition =
        rememberInfiniteTransition(label = "puzzleCelebration")

    val celebrationScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(650),
            repeatMode = RepeatMode.Reverse
        ),
        label = "puzzleScale"
    )

    if (finished) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFFFF8ED), Color.White)))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(60.dp))

            Text(
                "Challenge Complete!",
                fontSize = 30.sp,
                color = Color(0xFF9A5A00)
            )

            Spacer(Modifier.height(25.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(celebrationScale),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEED2)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        section.name,
                        fontSize = 22.sp
                    )

                    Spacer(Modifier.height(15.dp))

                    Text(
                        "$score / 10",
                        fontSize = 42.sp,
                        color = Color(0xFF9A5A00)
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "Coins Earned: $earnedCoins",
                        fontSize = 18.sp
                    )

                    Text(
                        "XP Earned: $earnedXp",
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {
                    index = 0
                    score = 0
                    answered = false
                    selected = null
                    correct = false
                    finished = false
                    earnedCoins = 0
                    earnedXp = 0
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE58A19)
                )
            ) {
                Text("Play Again", fontSize = 19.sp)
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Back to Puzzles", fontSize = 18.sp)
            }
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF8ED), Color.White)))
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedButton(
                onClick = onBack,
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("< Puzzles")
            }

            Text(
                "Coins: ${gameData?.coins ?: 0}",
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(15.dp))

        Text(
            section.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF9A5A00)
        )

        Text(
            "Question ${index + 1} / 10",
            fontSize = 17.sp,
            color = Color(0xFF806A4D)
        )

        Spacer(Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = {
                (index + 1) / 10f
            },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFE58A19)
        )

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {

            Text(
                question.question,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp),
                fontSize = 23.sp,
                color = Color(0xFF704300)
            )
        }

        Spacer(Modifier.height(18.dp))

        shuffledOptions.forEach { option ->

            val isSelected = option == selected

            val buttonColor = when {

                !answered -> Color.White

                isSelected && correct ->
                    Color(0xFF20B957)

                isSelected && !correct ->
                    Color(0xFFE94055)

                option == question.answer && answered ->
                    Color(0xFF20B957)

                else -> Color.White
            }

            Button(
                onClick = {

                    if (!answered) {

                        answered = true
                        selected = option

                        if (option == question.answer) {

                            correct = true
                            score++
                            earnedCoins += 10
                            earnedXp += 10

                            scope.launch {
                                dataStore.addReward(
                                    coins = 10,
                                    xp = 10
                                )
                            }

                        } else {
                            correct = false
                        }
                    }
                },
                enabled = !answered,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor =
                        if (
                            answered &&
                            (isSelected ||
                                    option == question.answer)
                        ) {
                            Color.White
                        } else {
                            Color(0xFF704300)
                        }
                )
            ) {

                Text(
                    option,
                    fontSize = 19.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(15.dp))

        AnimatedVisibility(visible = answered) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    if (correct) {
                        "Correct! +10 Coins +10 XP"
                    } else {
                        "Correct answer: ${question.answer}"
                    },
                    fontSize = 17.sp,
                    color =
                        if (correct) {
                            Color(0xFF159447)
                        } else {
                            Color(0xFFD52E45)
                        }
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {

                        if (index == section.questions.lastIndex) {

                            finished = true

                        } else {

                            index++
                            answered = false
                            selected = null
                            correct = false
                        }
                    },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE58A19)
                    )
                ) {

                    Text(
                        if (index == section.questions.lastIndex) {
                            "See Result"
                        } else {
                            "Next Question >"
                        },
                        fontSize = 17.sp
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Text(
            "Score: $score / 10",
            fontSize = 17.sp
        )
    }
}
@Composable
private fun CelebrationOverlay() {
    var visible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(900)
        visible = false
    }
    if (!visible) return
    val transition = rememberInfiniteTransition(label = "win")
    val scale by transition.animateFloat(0.92f, 1.08f, infiniteRepeatable(tween(500), RepeatMode.Reverse), label = "scale")
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF1565C0), Color(0xFF00BFA5), Color(0xFFFFC107))).copy(alpha = 0.96f)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.scale(scale)) {
            Text("🎉", fontSize = 72.sp)
            Text("CORRECT!", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("⭐ +10 XP   🪙 +10 Coins ⭐", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
