package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.AdaptiveQuestionEngine
import com.ashraful.learningquest.data.BengaliTranslation
import com.ashraful.learningquest.data.BankQuestion
import com.ashraful.learningquest.data.GameDataStore
import com.ashraful.learningquest.data.QuestionBank
import com.ashraful.learningquest.data.QuestionProgressStore
import com.ashraful.learningquest.data.TopicProgress
import kotlinx.coroutines.launch

@Composable
fun ArifaLearningHubTestScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val gameStore = remember { GameDataStore(context) }
    val progressStore = remember { QuestionProgressStore(context) }
    val scope = rememberCoroutineScope()
    val progress by progressStore.progress.collectAsState(initial = emptyMap())

    var questionIds by rememberSaveable { mutableStateOf("") }
    val questions = remember(questionIds) {
        questionIds.split(",").filter { it.isNotBlank() }.mapNotNull(QuestionBank::findById)
    }
    var index by rememberSaveable { mutableIntStateOf(0) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var selected by rememberSaveable { mutableStateOf<Int?>(null) }
    var answered by rememberSaveable { mutableStateOf(false) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var testStarted by rememberSaveable { mutableStateOf(false) }
    var translatedQuestion by rememberSaveable { mutableStateOf<String?>(null) }
    var translating by rememberSaveable { mutableStateOf(false) }
    var translationError by rememberSaveable { mutableStateOf<String?>(null) }

    val topicProgress = remember(progress) {
        QuestionBank.all.groupBy { it.topic }.map { (topic, bankQuestions) ->
            val history = bankQuestions.mapNotNull { progress[it.id] }
            val attempts = history.sumOf { it.attempts }
            val correct = history.sumOf { it.correct }
            TopicProgress(
                topic = topic,
                attempts = attempts,
                correct = correct,
                accuracy = if (attempts == 0) 0 else (correct * 100) / attempts,
                masteredQuestions = history.count { it.isMastered },
                totalQuestions = bankQuestions.size
            )
        }.sortedBy { if (it.attempts == 0) 0 else it.accuracy }
    }

    fun startAgain() {
        val selectedQuestions = AdaptiveQuestionEngine.mixedTest(
            questions = QuestionBank.all,
            progress = progress,
            count = minOf(10, QuestionBank.all.size)
        )
        questionIds = selectedQuestions.joinToString(",") { it.id }
        index = 0
        score = 0
        selected = null
        answered = false
        finished = false
        testStarted = true
        translatedQuestion = null
        translating = false
        translationError = null
    }

    Box(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFEAF2FF), Color.White))),
        contentAlignment = Alignment.TopCenter
    ) {
    Column(
        Modifier.fillMaxWidth().widthIn(max = 760.dp)
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onBack, shape = RoundedCornerShape(16.dp)) {
                Text("‹ Home")
            }
            Spacer(Modifier.width(10.dp))
            Text(
                "🧠 Learning Hub",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF315FBA)
            )
        }

        Spacer(Modifier.height(16.dp))

        if (finished) {
            Spacer(Modifier.height(40.dp))
            Text("🏆", fontSize = 70.sp)
            Text("Test Complete!", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(12.dp))
            Text("$score / ${questions.size}", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
            Text("Your question history has been updated.", fontSize = 15.sp)
            Spacer(Modifier.height(18.dp))
            Text("📊 Topic Progress", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            topicProgress.take(4).forEach { topic ->
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    tonalElevation = 1.dp
                ) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(topic.topic, fontWeight = FontWeight.Bold)
                            Text(
                                if (topic.attempts == 0) "Not started" else "${topic.accuracy}% accuracy • ${topic.masteredQuestions}/${topic.totalQuestions} mastered",
                                fontSize = 12.sp,
                                color = Color(0xFF68778C)
                            )
                        }
                        Text(
                            when {
                                topic.attempts == 0 -> "NEW"
                                topic.isWeak -> "PRACTICE"
                                topic.isStrong -> "STRONG"
                                else -> "BUILDING"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF315FBA)
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(onClick = ::startAgain, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Text("🔄 New Adaptive Test", fontSize = 17.sp)
            }
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(18.dp)) {
                Text("‹ Home")
            }
        } else if (!testStarted) {
            val attemptedQuestions = QuestionBank.all.count { progress[it.id]?.attempts ?: 0 > 0 }
            val masteredQuestions = QuestionBank.all.count { progress[it.id]?.isMastered == true }
            val totalAttempts = progress.values.sumOf { it.attempts }
            val totalCorrect = progress.values.sumOf { it.correct }
            val overallAccuracy = if (totalAttempts == 0) 0 else (totalCorrect * 100) / totalAttempts

            Spacer(Modifier.height(18.dp))
            Text("Your Learning Dashboard", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text("Adaptive practice built around what you know and what needs more practice.", fontSize = 14.sp, color = Color(0xFF68778C), textAlign = TextAlign.Center)
            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DashboardStat("📚", "Bank", QuestionBank.all.size.toString(), Modifier.weight(1f))
                DashboardStat("🎯", "Attempted", attemptedQuestions.toString(), Modifier.weight(1f))
                DashboardStat("🏆", "Mastered", masteredQuestions.toString(), Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), color = Color.White, tonalElevation = 2.dp) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Overall accuracy", fontWeight = FontWeight.Bold)
                        Text("$overallAccuracy%", fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(progress = { overallAccuracy / 100f }, modifier = Modifier.fillMaxWidth().height(8.dp))
                    Text("$totalAttempts answers recorded", fontSize = 12.sp, color = Color(0xFF68778C), modifier = Modifier.padding(top = 6.dp))
                }
            }
            Spacer(Modifier.height(18.dp))
            Text("🎯 Focus Areas", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(7.dp))
            topicProgress.take(4).forEach { topic ->
                Surface(Modifier.fillMaxWidth().padding(vertical = 3.dp), shape = RoundedCornerShape(14.dp), color = Color.White, tonalElevation = 1.dp) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(topic.topic, fontWeight = FontWeight.Bold)
                            Text(if (topic.attempts == 0) "Not started • ${topic.totalQuestions} questions" else "${topic.accuracy}% accuracy • ${topic.masteredQuestions}/${topic.totalQuestions} mastered", fontSize = 12.sp, color = Color(0xFF68778C))
                        }
                        Text(if (topic.attempts == 0) "START" else "PRACTICE", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Button(onClick = {
                questionIds = AdaptiveQuestionEngine.mixedTest(questions = QuestionBank.all, progress = progress, count = minOf(10, QuestionBank.all.size)).joinToString(",") { it.id }
                index = 0
                score = 0
                selected = null
                answered = false
                finished = false
                testStarted = true
                translatedQuestion = null
                translating = false
                translationError = null
            }, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(18.dp)) {
                Text("🚀 Start Adaptive Test", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Text("10 questions • weak areas get more practice • mastered questions return less often", fontSize = 12.sp, color = Color(0xFF68778C), textAlign = TextAlign.Center)
        } else if (questions.isEmpty()) {
            Spacer(Modifier.height(60.dp))
            CircularProgressIndicator()
            Text("Preparing your adaptive test…", Modifier.padding(top = 14.dp))
        } else {
            val q = questions[index]
            val optionOrder = remember(index, q.id) { q.options.indices.shuffled() }

            Text(
                "Adaptive Test • Question ${index + 1} / ${questions.size}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF68778C)
            )
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (index + 1) / questions.size.toFloat() },
                modifier = Modifier.fillMaxWidth().height(8.dp)
            )
            Spacer(Modifier.height(14.dp))

            Surface(shape = RoundedCornerShape(20.dp), color = Color.White, tonalElevation = 2.dp) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Text(
                        "${q.subject} • ${q.topic}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF315FBA)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        q.prompt,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!Regex("[\\u0980-\\u09FF]").containsMatchIn(q.prompt)) {
                        Spacer(Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                translating = true
                                translationError = null
                                BengaliTranslation.translate(
                                    text = q.prompt,
                                    onSuccess = {
                                        translatedQuestion = it
                                        translating = false
                                    },
                                    onError = {
                                        translationError = it
                                        translating = false
                                    }
                                )
                            },
                            enabled = !translating,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            if (translating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Translating…")
                            } else {
                                Text("বাংলা অনুবাদ  •  Translate to Bengali")
                            }
                        }

                        translatedQuestion?.let { translated ->
                            Spacer(Modifier.height(10.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF1F8E9)
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(
                                        "বাংলা",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF388E3C)
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        translated,
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        translationError?.let { error ->
                            Text(
                                error,
                                modifier = Modifier.padding(top = 6.dp),
                                fontSize = 12.sp,
                                color = Color(0xFFD32F2F),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            optionOrder.forEach { optionIndex ->
                val option = q.options[optionIndex]
                val isSelected = selected == optionIndex
                val isCorrect = optionIndex == q.correctIndex

                Button(
                    onClick = {
                        if (!answered) {
                            answered = true
                            selected = optionIndex
                            val correct = isCorrect
                            scope.launch {
                                progressStore.recordAnswer(q.id, correct)
                                gameStore.recordAnswer(correct)
                                if (correct) {
                                    score++
                                    gameStore.addReward(5, 5)
                                }
                            }
                        }
                    },
                    enabled = !answered,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(17.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            !answered -> Color.White
                            isCorrect -> Color(0xFF20B957)
                            isSelected -> Color(0xFFE94055)
                            else -> Color.White
                        },
                        contentColor = when {
                            answered && (isCorrect || isSelected) -> Color.White
                            else -> Color(0xFF26354A)
                        }
                    )
                ) {
                    Text(option, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            if (answered) {
                Spacer(Modifier.height(8.dp))
                Text(
                    if (selected == q.correctIndex) "🎉 Correct! +5 XP +5 Coins"
                    else "💡 Correct answer: ${q.correctAnswer}",
                    fontWeight = FontWeight.Bold
                )

                if (q.explanation.isNotBlank()) {
                    Text(
                        q.explanation,
                        modifier = Modifier.padding(top = 4.dp),
                        fontSize = 13.sp,
                        color = Color(0xFF65738A),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(7.dp))
                Button(
                    onClick = {
                        if (index == questions.lastIndex) {
                            finished = true
                        } else {
                            index++
                            selected = null
                            answered = false
                            translatedQuestion = null
                            translating = false
                            translationError = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(if (index == questions.lastIndex) "🏆 Finish" else "Next →")
                }
            }

            Spacer(Modifier.weight(1f))
            Text("Score: $score", fontWeight = FontWeight.Bold, color = Color(0xFF68778C))
        }
    }
    }
}

@Composable
private fun DashboardStat(icon: String, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier, shape = RoundedCornerShape(16.dp), color = Color.White, tonalElevation = 2.dp) {
        Column(Modifier.padding(vertical = 12.dp, horizontal = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 20.sp)
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
            Text(label, fontSize = 11.sp, color = Color(0xFF68778C), fontWeight = FontWeight.SemiBold)
        }
    }
}
