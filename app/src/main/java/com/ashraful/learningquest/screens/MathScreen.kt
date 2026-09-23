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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.GameDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class MathQuestion(
    val text: String,
    val answer: Int,
    val options: List<Int>
)

private fun makeOptions(
    answer: Int,
    difficulty: Int
): List<Int> {

    val choices = mutableSetOf(answer)

    val range = maxOf(5, difficulty * 3)

    while (choices.size < 4) {

        val wrong =
            answer + Random.nextInt(
                -range,
                range + 1
            )

        if (wrong >= 0 && wrong != answer) {
            choices.add(wrong)
        }
    }

    return choices.toList().shuffled()
}

fun generateMathQuestion(level: Int): MathQuestion {

    val difficulty = level.coerceIn(1, 10)

    val type = Random.nextInt(0, 7)

    var answer: Int
    var text: String

    when (type) {

        // ADDITION
        0 -> {

            val max = 10 * difficulty

            val a = Random.nextInt(1, max + 1)
            val b = Random.nextInt(1, max + 1)

            answer = a + b

            text = "$a + $b = ?"
        }

        // SUBTRACTION
        1 -> {

            val max = 15 * difficulty

            val a = Random.nextInt(5, max + 1)
            val b = Random.nextInt(1, a + 1)

            answer = a - b

            text = "$a - $b = ?"
        }

        // MULTIPLICATION
        2 -> {

            val max = difficulty + 3

            val a = Random.nextInt(2, max + 1)
            val b = Random.nextInt(2, max + 1)

            answer = a * b

            text = "$a × $b = ?"
        }

        // DIVISION
        3 -> {

            val divisor =
                Random.nextInt(
                    2,
                    difficulty + 3
                )

            val quotient =
                Random.nextInt(
                    2,
                    difficulty + 3
                )

            val dividend =
                divisor * quotient

            answer = quotient

            text =
                "$dividend ÷ $divisor = ?"
        }

        // MISSING NUMBER
        4 -> {

            val a =
                Random.nextInt(
                    1,
                    10 * difficulty
                )

            val b =
                Random.nextInt(
                    1,
                    10 * difficulty
                )

            answer = b

            text =
                "$a + ? = ${a + b}"
        }

        // COMPARE NUMBERS
        5 -> {

            val a =
                Random.nextInt(
                    1,
                    20 * difficulty
                )

            val b =
                Random.nextInt(
                    1,
                    20 * difficulty
                )

            answer = when {
                a > b -> 1
                a < b -> 2
                else -> 3
            }

            return MathQuestion(
                text = "$a   ?   $b",
                answer = answer,
                options = listOf(
                    1,
                    2,
                    3,
                    4
                ).shuffled()
            )
        }

        // NUMBER SEQUENCE
        else -> {

            val start =
                Random.nextInt(
                    1,
                    10 * difficulty
                )

            val step =
                Random.nextInt(
                    1,
                    difficulty + 3
                )

            val second = start + step
            val third = second + step
            val fourth = third + step

            answer = fourth + step

            text =
                "$start, $second, $third, $fourth, ?"
        }
    }

    return MathQuestion(
        text = text,
        answer = answer,
        options =
            makeOptions(
                answer,
                difficulty
            )
    )
}

@Composable
fun MathScreen(onBack: () -> Unit) {

    val context = LocalContext.current

    val dataStore = remember {
        GameDataStore(context)
    }

    val scope = rememberCoroutineScope()

    val gameData by dataStore.gameData.collectAsState(
        initial = null
    )

    var question by remember {
        mutableStateOf(
            generateMathQuestion(1)
        )
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var questionNumber by remember {
        mutableIntStateOf(1)
    }

    var answered by remember {
        mutableStateOf(false)
    }

    var selectedAnswer by remember {
        mutableStateOf<Int?>(null)
    }

    var correct by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    var shake by remember {
        mutableStateOf(false)
    }

    val shakeAnimation by animateFloatAsState(
        targetValue =
            if (shake) 1f else 0f,
        animationSpec = keyframes {

            durationMillis = 400

            0f at 0
            -10f at 50
            10f at 100
            -8f at 150
            8f at 200
            -5f at 250
            0f at 400
        },
        label = "shake"
    )

    LaunchedEffect(shake) {

        if (shake) {

            delay(450)

            shake = false
        }
    }

    val infinite =
        rememberInfiniteTransition(
            label = "celebration"
        )

    val celebrationScale by
    infinite.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(650),
                repeatMode =
                    RepeatMode.Reverse
            ),
        label = "celebrationScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFEAF7FF)
            )
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

                Text("< Home")
            }

            Surface(
                shape =
                    RoundedCornerShape(30.dp),
                color =
                    Color(0xFF315DA8)
            ) {

                Text(
                    text =
                        "Coins: ${gameData?.coins ?: 0}",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier =
                        Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 10.dp
                        )
                )
            }
        }

        Spacer(
            Modifier.height(18.dp)
        )

        Text(
            text = "Quick Math",
            fontSize = 32.sp,
            color = Color(0xFF173C8C)
        )

        Text(
            text =
                "Question $questionNumber",
            fontSize = 17.sp,
            color = Color(0xFF315DA8)
        )

        Spacer(
            Modifier.height(6.dp)
        )

        Text(
            text =
                "Difficulty ${gameData?.mathDifficulty ?: 1}",
            fontSize = 14.sp,
            color = Color(0xFF55708F)
        )

        Spacer(
            Modifier.height(18.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationX =
                        shakeAnimation
                },
            shape =
                RoundedCornerShape(28.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),
            elevation =
                CardDefaults.cardElevation(6.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 28.dp
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = question.text,
                    fontSize = 34.sp,
                    color =
                        Color(0xFF142D78)
                )
            }
        }

        Spacer(
            Modifier.height(20.dp)
        )

        question.options.forEach { option ->

            val selected =
                option == selectedAnswer

            val displayText =
                if (
                    question.text.contains("?") &&
                    question.text.contains("   ?   ")
                ) {

                    when (option) {
                        1 -> ">"
                        2 -> "<"
                        3 -> "="
                        else -> "?"
                    }

                } else {
                    option.toString()
                }

            Button(
                onClick = {

                    if (!answered) {

                        answered = true

                        selectedAnswer =
                            option

                        val actualAnswer =
                            if (
                                question.text.contains(
                                    "   ?   "
                                )
                            ) {
                                when (question.answer) {
                                    1 -> ">"
                                    2 -> "<"
                                    else -> "="
                                }
                            } else {
                                question.answer.toString()
                            }

                        if (
                            option ==
                            question.answer
                        ) {

                            correct = true

                            score++

                            message =
                                "Correct!  +10 Coins  +10 XP"

                            scope.launch {

                                dataStore.addReward(
                                    coins = 10,
                                    xp = 10
                                )

                                dataStore.recordMathAnswer(
                                    correct = true
                                )
                            }

                        } else {

                            correct = false

                            message =
                                "Not quite! Correct answer: $actualAnswer"

                            scope.launch {

                                dataStore.recordMathAnswer(
                                    correct = false
                                )
                            }

                            shake = true
                        }
                    }
                },
                enabled = !answered,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 4.dp
                    ),
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            when {

                                !answered ->
                                    Color.White

                                selected &&
                                        correct ->
                                    Color(0xFF20B957)

                                selected &&
                                        !correct ->
                                    Color(0xFFE94055)

                                else ->
                                    Color.White
                            },
                        contentColor =
                            if (
                                answered &&
                                selected
                            ) {
                                Color.White
                            } else {
                                Color(0xFF142D78)
                            }
                    )
            ) {

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 4.dp
                            ),
                    horizontalArrangement =
                        Arrangement.Center,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = displayText,
                        fontSize = 22.sp
                    )

                    if (
                        answered &&
                        selected
                    ) {

                        Spacer(
                            Modifier.width(12.dp)
                        )

                        Text(
                            text =
                                if (correct)
                                    "✓"
                                else
                                    "X",
                            fontSize = 25.sp
                        )
                    }
                }
            }
        }

        Spacer(
            Modifier.height(12.dp)
        )

        AnimatedVisibility(
            visible = answered
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(
                        if (correct)
                            celebrationScale
                        else
                            1f
                    ),
                shape =
                    RoundedCornerShape(24.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            if (correct)
                                Color(0xFFFFF8D8)
                            else
                                Color(0xFFFFE6EA)
                    )
            ) {

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text =
                            if (correct)
                                "Correct!"
                            else
                                "Not quite!",
                        fontSize = 28.sp,
                        color =
                            if (correct)
                                Color(0xFF159447)
                            else
                                Color(0xFFD52E45)
                    )

                    Spacer(
                        Modifier.height(6.dp)
                    )

                    Text(
                        text = message,
                        fontSize = 17.sp
                    )

                    if (correct) {

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "+10 COINS    +10 XP",
                            fontSize = 17.sp,
                            color =
                                Color(0xFFB77900)
                        )
                    }

                    Spacer(
                        Modifier.height(12.dp)
                    )

                    Button(
                        onClick = {

                            question =
                                generateMathQuestion(
                                    gameData
                                        ?.mathDifficulty
                                        ?: 1
                                )

                            questionNumber++

                            answered = false
                            selectedAnswer = null
                            message = ""
                            correct = false
                        },
                        shape =
                            RoundedCornerShape(18.dp),
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        if (correct)
                                            Color(0xFF19B957)
                                        else
                                            Color(0xFFE94055)
                                )
                    ) {

                        Text(
                            text =
                                "Next Question  >",
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }

        Spacer(
            Modifier.weight(1f)
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(35.dp)
        ) {

            Text(
                text =
                    "Score: $score",
                fontSize = 17.sp
            )

            Text(
                text =
                    "XP: ${gameData?.xp ?: 0}",
                fontSize = 17.sp
            )
        }
    }
}