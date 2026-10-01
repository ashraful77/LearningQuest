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
import com.ashraful.learningquest.data.QuizSection
import com.ashraful.learningquest.data.scienceSections
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun ScienceScreen(onBack: () -> Unit) {

    var selectedSection by remember {
        mutableStateOf<QuizSection?>(null)
    }

    val section = selectedSection

    if (section == null) {

        ScienceSectionScreen(
            onBack = onBack,
            onSectionSelected = {
                selectedSection = it
            }
        )

    } else {

        ScienceQuizScreen(
            section = section,
            onBack = {
                selectedSection = null
            }
        )
    }
}

@Composable
private fun ScienceSectionScreen(
    onBack: () -> Unit,
    onSectionSelected: (QuizSection) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF1FBF6), Color.White)))
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
                Text("🔬 Science", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF23754A))
                Text("Explore nature, life, space and the world around you", fontSize = 13.sp, color = Color(0xFF71809A))
            }
        }

        Spacer(Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE5F6EC)),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.8f)) {
                    Text("🔬", modifier = Modifier.padding(12.dp), fontSize = 22.sp)
                }
                Spacer(Modifier.width(13.dp))
                Column {
                    Text("Choose a Challenge", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF23754A))
                    Text("10 questions • Earn XP & Coins", fontSize = 12.sp, color = Color(0xFF71809A))
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        scienceSections.forEach { section ->
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
                    Surface(shape = CircleShape, color = Color(0xFFE5F6EC)) {
                        Text("▶", modifier = Modifier.padding(11.dp), fontSize = 13.sp, color = Color(0xFF23754A))
                    }
                    Spacer(Modifier.width(13.dp))
                    Column(Modifier.weight(1f)) {
                        Text(section.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF23754A))
                        Text("10 Questions", fontSize = 12.sp, color = Color(0xFF7A8798))
                    }
                    Text("›", fontSize = 28.sp, color = Color(0xFF23754A))
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Text("Complete challenges to earn rewards 🌟", fontSize = 12.sp, color = Color(0xFF8A96A8), modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
private fun ScienceQuizScreen(
    section: QuizSection,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val dataStore = remember {
        GameDataStore(context)
    }

    val scope = rememberCoroutineScope()

    val gameData by dataStore.gameData.collectAsState(
        initial = null
    )

    var index by remember {
        mutableIntStateOf(0)
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var answered by remember {
        mutableStateOf(false)
    }

    var selected by remember {
        mutableStateOf<String?>(null)
    }

    var correct by remember {
        mutableStateOf(false)
    }

    var finished by remember {
        mutableStateOf(false)
    }

    var earnedCoins by remember {
        mutableIntStateOf(0)
    }

    var rewardGiven by remember { mutableStateOf(false) }
    var earnedXp by remember {
        mutableIntStateOf(0)
    }

    val question = section.questions[index]
    val shuffledOptions = remember(section, index) { question.options.shuffled() }

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "scienceCelebration"
        )

    val celebrationScale by
    infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(650),
                repeatMode =
                    RepeatMode.Reverse
            ),
        label = "scienceScale"
    )

    if (finished) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFF1FBF6), Color.White)))
                .padding(24.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                Modifier.height(60.dp)
            )

            Text(
                text = "🎉 Challenge Complete!",
                fontSize = 30.sp,
                color = Color(0xFF176B45)
            )

            Spacer(
                Modifier.height(25.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(celebrationScale),
                shape =
                    RoundedCornerShape(28.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFFE6F8ED)
                    )
            ) {

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = section.name,
                        fontSize = 22.sp
                    )

                    Spacer(
                        Modifier.height(15.dp)
                    )

                    Text(
                        text = "$score / 10",
                        fontSize = 42.sp,
                        color = Color(0xFF176B45)
                    )

                    Spacer(
                        Modifier.height(10.dp)
                    )

                    Text(
                        text =
                            "Coins Earned: $earnedCoins",
                        fontSize = 18.sp
                    )

                    Text(
                        text =
                            "XP Earned: $earnedXp",
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(
                Modifier.height(30.dp)
            )

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
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF258A5A)
                    )
            ) {

                Text(
                    text = "Play Again",
                    fontSize = 19.sp
                )
            }

            OutlinedButton(
                onClick = onBack,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                shape =
                    RoundedCornerShape(18.dp)
            ) {

                Text(
                    text = "Back to Sections",
                    fontSize = 18.sp
                )
            }
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF1FBF6), Color.White)))
            .padding(18.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            OutlinedButton(
                onClick = onBack,
                shape =
                    RoundedCornerShape(30.dp)
            ) {
                Text("< Sections")
            }

            Text(
                text =
                    "Coins: ${gameData?.coins ?: 0}",
                fontSize = 16.sp
            )
        }

        Spacer(
            Modifier.height(15.dp)
        )

        Text(
            text = section.name,
            fontSize = 30.sp,
            color = Color(0xFF176B45)
        )

        Text(
            text =
                "Question ${index + 1} / 10",
            fontSize = 17.sp,
            color = Color(0xFF557A68)
        )

        Spacer(
            Modifier.height(12.dp)
        )

        LinearProgressIndicator(
            progress = {
                (index + 1) / 10f
            },
            modifier =
                Modifier.fillMaxWidth(),
            color = Color(0xFF258A5A)
        )

        Spacer(
            Modifier.height(20.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(24.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),
            elevation =
                CardDefaults.cardElevation(5.dp)
        ) {

            Text(
                text = question.question,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                fontSize = 23.sp,
                color = Color(0xFF185A40)
            )
        }

        Spacer(
            Modifier.height(18.dp)
        )

        shuffledOptions.forEach { option ->
            val isSelected = option == selected
            val buttonColor = when {
                !answered -> Color.White
                isSelected && correct -> Color(0xFF20B957)
                isSelected && !correct -> Color(0xFFE94055)
                option == question.answer && answered -> Color(0xFF20B957)
                else -> Color(0xFFE8EBF0)
            }

            Button(
                onClick = {
                    if (!answered) {
                        answered = true
                        selected = option
                        if (option == question.answer) {
                            correct = true
                            score++
                            scope.launch {
                                val rewarded = dataStore.recordArifaQuestionReward(question.question, 10, 10)
                                rewardGiven = rewarded
                                if (rewarded) {
                                    earnedCoins += 10
                                    earnedXp += 10
                                }
                            }
                        } else {
                            correct = false
                        }
                    }
                },
                enabled = !answered,
                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                shape = RoundedCornerShape(17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = when {
                        !answered -> Color(0xFF185A40)
                        isSelected && correct -> Color.White
                        isSelected && !correct -> Color.White
                        option == question.answer -> Color.White
                        else -> Color(0xFF8A93A0)
                    }
                )
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (answered && isSelected && !correct) {
                        Text("✕  ", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                    } else if (answered && option == question.answer) {
                        Text("✓  ", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Text(option, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    if (answered && option == question.answer) {
                        Text("  ✓", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        AnimatedVisibility(visible = answered) {
            Card(
                Modifier.fillMaxWidth().padding(top = 8.dp).scale(if (correct) celebrationScale else 1f),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (correct) Color(0xFFEAF9EF) else Color(0xFFFFE9ED)
                )
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (correct) "🎉 CORRECT!" else "❌ WRONG ANSWER",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (correct) Color(0xFF159447) else Color(0xFFD52E45)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        when {
                            !correct -> "Correct answer: " + question.answer
                            rewardGiven -> "+10 Coins  •  +10 XP"
                            else -> "✓ Correct! Reward already collected today • New coins tomorrow"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (correct) Color(0xFF159447) else Color(0xFFD52E45)
                    )
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (index == section.questions.lastIndex) finished = true
                            else {
                                index++
                                answered = false
                                selected = null
                                correct = false
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF258A5A)
                        )
                    ) {
                        Text(
                            if (index == section.questions.lastIndex) "See Result  ›" else "Next Question  ›",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(
            Modifier.weight(1f)
        )

        Text(
            text = "Score: $score / 10",
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
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF1565C0), Color(0xFF00BFA5), Color(0xFFFFC107)))), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.scale(scale)) {
            Text("🎉", fontSize = 72.sp)
            Text("CORRECT!", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("⭐ +10 XP   🪙 +10 Coins ⭐", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
