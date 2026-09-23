package com.ashraful.learningquest.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.GameDataStore
import com.ashraful.learningquest.data.Question
import com.ashraful.learningquest.data.englishSections
import com.ashraful.learningquest.data.scienceSections
import com.ashraful.learningquest.data.puzzleQuestions
import kotlinx.coroutines.launch

private data class MixedQuestion(val subject: String, val question: Question)

@Composable
fun MixedQuizScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val dataStore = remember { GameDataStore(context) }
    val scope = rememberCoroutineScope()
    val gameData by dataStore.gameData.collectAsState(initial = null)

    val questions = remember {
        val english = englishSections.flatMap { it.questions }.map { MixedQuestion("English", it) }
        val science = scienceSections.flatMap { it.questions }.map { MixedQuestion("Science", it) }
        val puzzles = puzzleQuestions.map { MixedQuestion("Puzzle", it) }
        val math = (1..8).map {
            val q = generateMathQuestion(2)
            MixedQuestion("Math", Question(q.text, q.options.map { it.toString() }, q.answer.toString()))
        }
        (english + science + puzzles + math).shuffled().take(20)
    }

    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<String?>(null) }
    var correct by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }

    if (finished) {
        val scale by rememberInfiniteTransition(label = "finish").animateFloat(
            0.96f, 1.04f,
            infiniteRepeatable(tween(650), RepeatMode.Reverse),
            label = "finishScale"
        )
        Column(
            Modifier.fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFFFF0FA), Color(0xFFEAF7FF))))
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(55.dp))
            Text("🎉 Mixed Quiz Complete!", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF6A1B9A))
            Spacer(Modifier.height(24.dp))
            Card(
                Modifier.fillMaxWidth().scale(scale),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🏆", fontSize = 64.sp)
                    Text("${score} / 20", fontSize = 46.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    Text("Marks", fontSize = 18.sp, color = Color(0xFF71809A))
                    Spacer(Modifier.height(12.dp))
                    Text("⭐ Great effort! Keep learning!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(28.dp))
            Button(
                onClick = {
                    index = 0; score = 0; answered = false; selected = null; correct = false; finished = false
                },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)
            ) { Text("🔄 Try Again", fontSize = 18.sp) }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Text("‹ Home")
            }
        }
        return
    }

    val current = questions[index]
    val options = remember(index) { current.question.options.shuffled() }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFF4F7FF), Color.White)))
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = onBack, shape = RoundedCornerShape(18.dp)) { Text("‹ Home") }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("🌈 Mixed Quiz", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF6A1B9A))
                    Text("20 marks • All subjects", fontSize = 13.sp, color = Color(0xFF71809A))
                }
                Text("🪙 ${gameData?.coins ?: 0}", fontWeight = FontWeight.Bold, color = Color(0xFF8B5B00))
            }

            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Question ${index + 1} / 20", fontWeight = FontWeight.Bold, color = Color(0xFF40516A))
                Text("Score: ${score}", fontWeight = FontWeight.Bold, color = Color(0xFF315FBA))
            }
            Spacer(Modifier.height(7.dp))
            LinearProgressIndicator(
                progress = { (index + 1) / 20f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Color(0xFF7B3FC6), trackColor = Color(0xFFE8DDF5)
            )

            Spacer(Modifier.height(14.dp))
            Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFEDE3FF)) {
                Text(current.subject, Modifier.padding(horizontal = 16.dp, vertical = 7.dp), fontWeight = FontWeight.Bold, color = Color(0xFF6A1B9A))
            }
            Spacer(Modifier.height(12.dp))

            Card(
                Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(current.question.question, Modifier.fillMaxWidth().padding(26.dp),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xFF233B68), textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(14.dp))
            options.forEach { option ->
                val isSelected = selected == option
                Button(
                    onClick = {
                        if (!answered) {
                            answered = true
                            selected = option
                            correct = option == current.question.answer
                            if (correct) {
                                score++
                                scope.launch { dataStore.addReward(5, 5) }
                            }
                        }
                    },
                    enabled = !answered,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(17.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            !answered -> Color.White
                            isSelected && correct -> Color(0xFF20B957)
                            isSelected && !correct -> Color(0xFFE94055)
                            else -> Color.White
                        },
                        contentColor = if (answered && isSelected) Color.White else Color(0xFF233B68)
                    )
                ) {
                    Text(option, Modifier.padding(vertical = 5.dp), fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            if (answered) {
                Spacer(Modifier.height(8.dp))
                Text(
                    if (correct) "🎉 Correct! +5 Coins +5 XP" else "💡 Correct answer: ${current.question.answer}",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    color = if (correct) Color(0xFF159447) else Color(0xFFD52E45)
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (index == questions.lastIndex) finished = true
                        else { index++; answered = false; selected = null; correct = false }
                    },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)
                ) {
                    Text(if (index == questions.lastIndex) "🏆 Finish Test" else "Next Question →", fontSize = 17.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        if (answered && correct) MixedCelebrationOverlay()
    }
}

@Composable
private fun MixedCelebrationOverlay() {
    var visible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(800)
        visible = false
    }
    if (!visible) return

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF6A1B9A), Color(0xFFFF4081), Color(0xFFFFC107)))
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎉", fontSize = 78.sp)
            Text("CORRECT!", fontSize = 44.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("⭐ +5 XP   🪙 +5 Coins ⭐", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
