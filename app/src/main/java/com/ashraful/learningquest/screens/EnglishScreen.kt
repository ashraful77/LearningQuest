package com.ashraful.learningquest.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.GameDataStore
import com.ashraful.learningquest.data.QuizSection
import com.ashraful.learningquest.data.englishSections
import kotlinx.coroutines.launch

@Composable
fun EnglishScreen(onBack: () -> Unit) {

    var selectedSection by remember {
        mutableStateOf<QuizSection?>(null)
    }

    val section = selectedSection

    if (section == null) {

        EnglishSectionScreen(
            onBack = onBack,
            onSectionSelected = {
                selectedSection = it
            }
        )

    } else {

        EnglishQuizScreen(
            section = section,
            onBack = {
                selectedSection = null
            }
        )
    }
}

@Composable
private fun EnglishSectionScreen(
    onBack: () -> Unit,
    onSectionSelected: (QuizSection) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedButton(
            onClick = onBack,
            shape = RoundedCornerShape(30.dp)
        ) {
            Text("< Home")
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "English",
            fontSize = 32.sp,
            color = Color(0xFF173C8C)
        )

        Text(
            text = "Choose a Challenge",
            fontSize = 18.sp,
            color = Color(0xFF55708F)
        )

        Spacer(Modifier.height(24.dp))

        englishSections.forEach { section ->

            Button(
                onClick = {
                    onSectionSelected(section)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(18.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(
                        vertical = 5.dp
                    )
                ) {

                    Text(
                        text = section.name,
                        fontSize = 20.sp
                    )

                    Text(
                        text = "10 Questions",
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "Complete a challenge to earn rewards!",
            fontSize = 15.sp,
            color = Color(0xFF55708F)
        )
    }
}

@Composable
private fun EnglishQuizScreen(
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

    var earnedXp by remember {
        mutableIntStateOf(0)
    }

    val question = section.questions[index]

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "celebration"
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
        label = "celebrationScale"
    )

    if (finished) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F9FF))
                .padding(24.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                Modifier.height(60.dp)
            )

            Text(
                text = "Challenge Complete!",
                fontSize = 30.sp,
                color = Color(0xFF173C8C)
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
                            Color(0xFFFFF8D8)
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
                        color = Color(0xFF173C8C)
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
                    RoundedCornerShape(18.dp)
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
            .background(Color(0xFFF5F9FF))
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
            color = Color(0xFF173C8C)
        )

        Text(
            text =
                "Question ${index + 1} / 10",
            fontSize = 17.sp,
            color = Color(0xFF55708F)
        )

        Spacer(
            Modifier.height(12.dp)
        )

        LinearProgressIndicator(
            progress = {
                (index + 1) / 10f
            },
            modifier =
                Modifier.fillMaxWidth()
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
                color = Color(0xFF142D78)
            )
        }

        Spacer(
            Modifier.height(18.dp)
        )

        question.options.forEach { option ->

            val isSelected =
                option == selected

            val buttonColor = when {

                !answered ->
                    Color.White

                isSelected && correct ->
                    Color(0xFF20B957)

                isSelected && !correct ->
                    Color(0xFFE94055)

                option == question.answer &&
                        answered ->
                    Color(0xFF20B957)

                else ->
                    Color.White
            }

            Button(
                onClick = {

                    if (!answered) {

                        answered = true
                        selected = option

                        if (
                            option ==
                            question.answer
                        ) {

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
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 4.dp
                        ),
                shape =
                    RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            buttonColor,
                        contentColor =
                            if (
                                answered &&
                                (
                                        isSelected ||
                                                option ==
                                                question.answer
                                        )
                            ) {
                                Color.White
                            } else {
                                Color(0xFF142D78)
                            }
                    )
            ) {

                Text(
                    text = option,
                    fontSize = 19.sp,
                    modifier =
                        Modifier.padding(
                            vertical = 4.dp
                        )
                )
            }
        }

        Spacer(
            Modifier.height(15.dp)
        )

        AnimatedVisibility(
            visible = answered
        ) {

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
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

                Spacer(
                    Modifier.height(12.dp)
                )

                Button(
                    onClick = {

                        if (
                            index ==
                            section.questions.lastIndex
                        ) {

                            finished = true

                        } else {

                            index++
                            answered = false
                            selected = null
                            correct = false
                        }
                    },
                    shape =
                        RoundedCornerShape(18.dp)
                ) {

                    Text(
                        text =
                            if (
                                index ==
                                section.questions.lastIndex
                            ) {
                                "See Result"
                            } else {
                                "Next Question >"
                            },
                        fontSize = 17.sp
                    )
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