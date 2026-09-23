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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.GameDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class MathQuestion(val text: String, val answer: Int, val options: List<Int>)

private fun makeOptions(answer: Int, difficulty: Int): List<Int> {
    val choices = mutableSetOf(answer)
    val range = maxOf(5, difficulty * 3)
    while (choices.size < 4) {
        val wrong = answer + Random.nextInt(-range, range + 1)
        if (wrong >= 0 && wrong != answer) choices.add(wrong)
    }
    return choices.toList().shuffled()
}

fun generateMathQuestion(level: Int): MathQuestion {
    val difficulty = level.coerceIn(1, 10)
    return when (Random.nextInt(7)) {
        0 -> {
            val max = 10 * difficulty
            val a = Random.nextInt(1, max + 1)
            val b = Random.nextInt(1, max + 1)
            MathQuestion("$a + $b = ?", a + b, makeOptions(a + b, difficulty))
        }
        1 -> {
            val max = 15 * difficulty
            val a = Random.nextInt(5, max + 1)
            val b = Random.nextInt(1, a + 1)
            MathQuestion("$a - $b = ?", a - b, makeOptions(a - b, difficulty))
        }
        2 -> {
            val max = difficulty + 3
            val a = Random.nextInt(2, max + 1)
            val b = Random.nextInt(2, max + 1)
            MathQuestion("$a × $b = ?", a * b, makeOptions(a * b, difficulty))
        }
        3 -> {
            val divisor = Random.nextInt(2, difficulty + 3)
            val quotient = Random.nextInt(2, difficulty + 3)
            MathQuestion("${divisor * quotient} ÷ $divisor = ?", quotient, makeOptions(quotient, difficulty))
        }
        4 -> {
            val a = Random.nextInt(1, 10 * difficulty)
            val b = Random.nextInt(1, 10 * difficulty)
            MathQuestion("$a + ? = ${a + b}", b, makeOptions(b, difficulty))
        }
        5 -> {
            val a = Random.nextInt(1, 20 * difficulty)
            val b = Random.nextInt(1, 20 * difficulty)
            val answer = when { a > b -> 1; a < b -> 2; else -> 3 }
            MathQuestion("$a   ?   $b", answer, listOf(1, 2, 3, 4).shuffled())
        }
        else -> {
            val start = Random.nextInt(1, 10 * difficulty)
            val step = Random.nextInt(1, difficulty + 3)
            val a = start + step
            val b = a + step
            val c = b + step
            val answer = c + step
            MathQuestion("$start, $a, $b, $c, ?", answer, makeOptions(answer, difficulty))
        }
    }
}

@Composable
fun MathScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val dataStore = remember { GameDataStore(context) }
    val scope = rememberCoroutineScope()
    val gameData by dataStore.gameData.collectAsState(initial = null)

    var question by remember { mutableStateOf(generateMathQuestion(1)) }
    var score by remember { mutableIntStateOf(0) }
    var questionNumber by remember { mutableIntStateOf(1) }
    var answered by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var correct by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var shake by remember { mutableStateOf(false) }

    val shakeX by animateFloatAsState(
        targetValue = if (shake) 1f else 0f,
        animationSpec = keyframes {
            durationMillis = 400
            0f at 0; -10f at 50; 10f at 100; -8f at 150
            8f at 200; -5f at 250; 0f at 400
        },
        label = "shake"
    )

    LaunchedEffect(shake) {
        if (shake) {
            delay(450)
            shake = false
        }
    }

    val celebration = rememberInfiniteTransition(label = "celebration")
    val celebrationScale by celebration.animateFloat(
        0.97f, 1.03f,
        infiniteRepeatable(tween(650), RepeatMode.Reverse),
        label = "celebrationScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF2FF), Color.White)))
            .padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onBack, shape = RoundedCornerShape(20.dp)) {
                Text("‹ Home")
            }
            Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFFFF3D4)) {
                Text(
                    "🪙 ${gameData?.coins ?: 0}",
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B5B00)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Quick Math", fontSize = 29.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF173C8C))
                Text("Question $questionNumber", fontSize = 14.sp, color = Color(0xFF55708F))
            }
            Surface(shape = CircleShape, color = Color(0xFFDCEAFF)) {
                Text("➗", modifier = Modifier.padding(11.dp), fontSize = 22.sp)
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Difficulty ${gameData?.mathDifficulty ?: 1}", fontSize = 13.sp, color = Color(0xFF65778B))
            Text("Score $score", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2457A6))
        }

        Spacer(Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { (questionNumber % 10).let { if (it == 0) 1f else it / 10f } },
            modifier = Modifier.fillMaxWidth().height(7.dp),
            color = Color(0xFF315FBA),
            trackColor = Color(0xFFDCE7F8)
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().graphicsLayer { translationX = shakeX },
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                Modifier.fillMaxWidth().padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SOLVE THIS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A96A8))
                Spacer(Modifier.height(8.dp))
                Text(
                    question.text,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF142D78),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        question.options.forEach { option ->
            val selected = option == selectedAnswer
            val display = if (question.text.contains("   ?   ")) {
                when (option) { 1 -> ">"; 2 -> "<"; 3 -> "="; else -> "?" }
            } else option.toString()

            Button(
                onClick = {
                    if (!answered) {
                        answered = true
                        selectedAnswer = option
                        if (option == question.answer) {
                            correct = true
                            score++
                            message = "Correct! +10 Coins +10 XP"
                            scope.launch {
                                dataStore.addReward(10, 10)
                                dataStore.recordMathAnswer(true)
                            }
                        } else {
                            correct = false
                            val actual = if (question.text.contains("   ?   ")) {
                                when (question.answer) { 1 -> ">"; 2 -> "<"; else -> "=" }
                            } else question.answer.toString()
                            message = "Correct answer: $actual"
                            scope.launch { dataStore.recordMathAnswer(false) }
                            shake = true
                        }
                    }
                },
                enabled = !answered,
                modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                shape = RoundedCornerShape(17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        !answered -> Color.White
                        selected && correct -> Color(0xFF20B957)
                        selected && !correct -> Color(0xFFE94055)
                        else -> Color.White
                    },
                    contentColor = if (answered && selected) Color.White else Color(0xFF142D78)
                )
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(display, fontSize = 21.sp, fontWeight = FontWeight.SemiBold)
                    if (answered && selected) {
                        Spacer(Modifier.width(10.dp))
                        Text(if (correct) "✓" else "✕", fontSize = 22.sp)
                    }
                }
            }
        }

        AnimatedVisibility(answered) {
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
                        if (correct) "🎉 Great job!" else "💡 Keep trying!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (correct) Color(0xFF159447) else Color(0xFFD52E45)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(message, fontSize = 14.sp)
                    Spacer(Modifier.height(9.dp))
                    Button(
                        onClick = {
                            question = generateMathQuestion(gameData?.mathDifficulty ?: 1)
                            questionNumber++
                            answered = false
                            selectedAnswer = null
                            message = ""
                            correct = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (correct) Color(0xFF19B957) else Color(0xFF315FBA)
                        )
                    ) {
                        Text("Next Question  ›", fontSize = 16.sp)
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Text("Keep learning • Keep growing 🌟", fontSize = 12.sp, color = Color(0xFF8A96A8), modifier = Modifier.padding(bottom = 7.dp))
    }
}
