package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.GameDataStore
import com.ashraful.learningquest.data.anishQuestionBank
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val MODEL_TEST_SIZE = 20
private const val MODEL_TEST_SECONDS = 15 * 60

@Composable
fun AnishModelTestScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val scope = rememberCoroutineScope()
    val data by store.gameData.collectAsState(initial = null)
    val questions = remember { anishQuestionBank.shuffled().take(MODEL_TEST_SIZE) }

    var index by rememberSaveable { mutableIntStateOf(0) }
    var selected by rememberSaveable { mutableIntStateOf(-1) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var secondsLeft by rememberSaveable { mutableIntStateOf(MODEL_TEST_SECONDS) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var recorded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(finished) {
        if (!finished) {
            while (secondsLeft > 0 && !finished) {
                delay(1000)
                if (!finished) secondsLeft--
            }
            if (secondsLeft <= 0) finished = true
        }
    }

    LaunchedEffect(finished) {
        if (finished && !recorded) {
            recorded = true
            store.recordRealTest("Anish Model Test", score, MODEL_TEST_SIZE, (MODEL_TEST_SECONDS - secondsLeft).toLong())
        }
    }

    fun answer(choice: Int) {
        if (selected != -1 || finished) return
        selected = choice
        val correct = choice == questions[index].correctAnswer
        if (correct) score++
        scope.launch {
            store.recordAnishQuestionAttempt(
                questions[index].id,
                correct
            )
        }
    }

    fun next() {
        if (selected == -1) return
        if (index == questions.lastIndex) finished = true
        else {
            index++
            selected = -1
        }
    }

    Column(
        Modifier.fillMaxSize()
            .background(Color(0xFFF7FAFF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (finished) {
            ModelTestResult(
                score, MODEL_TEST_SIZE, data?.diamonds ?: 0,
                onBack,
                onRetry = {
                    index = 0
                    selected = -1
                    score = 0
                    secondsLeft = MODEL_TEST_SECONDS
                    finished = false
                    recorded = false
                }
            )
            return@Column
        }

        Text("📝 Anish Model Test", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
        Text("Class 5 • Mixed Subjects • 20 Questions", fontSize = 13.sp, color = Color(0xFF60758A))

        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFE9F2FF)) {
                Text("💎 ${data?.diamonds ?: 0}", Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
            }
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (secondsLeft <= 60) Color(0xFFFFE4E4) else Color(0xFFFFF3D4)
            ) {
                Text(
                    "⏱️ ${secondsLeft / 60}:${(secondsLeft % 60).toString().padStart(2, '0')}",
                    Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    fontWeight = FontWeight.ExtraBold,
                    color = if (secondsLeft <= 60) Color(0xFFC62828) else Color(0xFF8B6500)
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { ((index + 1).toFloat() / MODEL_TEST_SIZE).coerceIn(0f, 1f) },
            Modifier.fillMaxWidth().height(8.dp),
            color = Color(0xFF315FBA),
            trackColor = Color(0xFFDCE7F7)
        )
        Spacer(Modifier.height(8.dp))
        Text("Question ${index + 1} / $MODEL_TEST_SIZE    •    Score: ${score}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))

        Spacer(Modifier.height(12.dp))
        val question = questions[index]

        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(18.dp)) {
                Text("${question.subject} • ${question.topic}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7043A8))
                Spacer(Modifier.height(10.dp))
                Text(question.question, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))
                Spacer(Modifier.height(16.dp))

                question.options.forEachIndexed { optionIndex, option ->
                    val isSelected = selected == optionIndex
                    val isCorrect = optionIndex == question.correctAnswer
                    val optionColor = when {
                        selected == -1 -> Color(0xFFF3F7FF)
                        isCorrect -> Color(0xFFDDF6E7)
                        isSelected -> Color(0xFFFFE0E0)
                        else -> Color(0xFFF3F5F8)
                    }
                    val textColor = when {
                        selected != -1 && isCorrect -> Color(0xFF23754A)
                        selected != -1 && isSelected -> Color(0xFFC62828)
                        else -> Color(0xFF26354A)
                    }

                    Card(
                        onClick = { answer(optionIndex) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = optionColor),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("${('A'.code + optionIndex).toChar()}.", fontWeight = FontWeight.ExtraBold, color = textColor, fontSize = 16.sp)
                            Spacer(Modifier.width(10.dp))
                            Text(option, fontSize = 16.sp, fontWeight = if (isSelected || (selected != -1 && isCorrect)) FontWeight.Bold else FontWeight.Normal, color = textColor)
                        }
                    }
                }
            }
        }

        if (selected != -1) {
            Spacer(Modifier.height(10.dp))
            Text(
                if (selected == question.correctAnswer) "🎉 Correct! +5 💎" else "📚 Keep learning! +5 💎",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (selected == question.correctAnswer) Color(0xFF23754A) else Color(0xFFC62828)
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { next() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(if (index == questions.lastIndex) "Finish Test 🏆" else "Next Question →", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
private fun ModelTestResult(
    score: Int,
    total: Int,
    diamonds: Int,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    val percent = (score * 100) / total
    val message = when {
        percent >= 90 -> "🏆 Outstanding!"
        percent >= 75 -> "🌟 Excellent work!"
        percent >= 60 -> "👏 Good job!"
        percent >= 40 -> "💪 Keep practising!"
        else -> "📚 Let's learn and try again!"
    }

    Column(Modifier.fillMaxWidth().padding(top = 28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🏆", fontSize = 72.sp)
        Text("Model Test Complete!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA), textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(message, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7043A8))
        Spacer(Modifier.height(18.dp))

        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${score} / ${total}", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                Text("${percent}% Score", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
                Spacer(Modifier.height(14.dp))
                Text("💎 +${total * 5} diamonds earned", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                Text("Current balance: ${diamonds} 💎", fontSize = 13.sp, color = Color(0xFF60758A))
            }
        }

        Spacer(Modifier.height(18.dp))
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) {
            Text("🔄 Take Another Test", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) {
            Text("← Back to Anish Home", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}