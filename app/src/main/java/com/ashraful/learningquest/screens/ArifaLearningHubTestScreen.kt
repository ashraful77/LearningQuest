package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ashraful.learningquest.data.GameDataStore
import com.ashraful.learningquest.data.QuestionBank
import com.ashraful.learningquest.data.QuestionProgress
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
    var showBengali by rememberSaveable { mutableStateOf(false) }
    var hubTab by rememberSaveable { mutableIntStateOf(0) }

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
        showBengali = false
    }

    Box(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFEAF2FF), Color.White))),
        contentAlignment = Alignment.TopCenter
    ) {
    Column(
        Modifier.fillMaxWidth().widthIn(max = 760.dp)
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "🧠 Learning Hub",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF315FBA)
            )
            OutlinedButton(onClick = onBack, shape = RoundedCornerShape(16.dp)) {
                Text("‹ Home")
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(18.dp)).padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(onClick = { hubTab = 0 }, modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = if (hubTab == 0) Color(0xFF315FBA) else Color.Transparent, contentColor = if (hubTab == 0) Color.White else Color(0xFF315FBA))) { Text("📚 LEARNING HUB", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold) }
            Button(onClick = { hubTab = 1 }, modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = if (hubTab == 1) Color(0xFF315FBA) else Color.Transparent, contentColor = if (hubTab == 1) Color.White else Color(0xFF315FBA))) { Text("📋 REAL TEST", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold) }
        }

        Spacer(Modifier.height(16.dp))

        if (hubTab == 1) {
            RealTestScreen(progress = progress, onBackToHub = { hubTab = 0 })
        } else if (finished) {
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
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                questionIds = AdaptiveQuestionEngine.mixedTest(questions = QuestionBank.all, progress = progress, count = minOf(10, QuestionBank.all.size)).joinToString(",") { it.id }
                index = 0
                score = 0
                selected = null
                answered = false
                finished = false
                testStarted = true
                showBengali = false
            }, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(18.dp)) {
                Text("🚀 Start Adaptive Test", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Text("10 questions • weak areas get more practice • mastered questions return less often", fontSize = 12.sp, color = Color(0xFF68778C), textAlign = TextAlign.Center)
            Spacer(Modifier.height(18.dp))
            Text("📚 Subject Progress", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(7.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Math", "English", "Science").forEach { subject ->
                    val subjectQuestions = QuestionBank.bySubject(subject)
                    val subjectHistory = subjectQuestions.mapNotNull { progress[it.id] }
                    val attempts = subjectHistory.sumOf { it.attempts }
                    val correct = subjectHistory.sumOf { it.correct }
                    val accuracy = if (attempts == 0) 0 else (correct * 100) / attempts
                    val mastered = subjectHistory.count { it.isMastered }
                    SubjectProgressCard(
                        subject = subject,
                        accuracy = accuracy,
                        mastered = mastered,
                        total = subjectQuestions.size,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
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
                    if (q.hasBengali) {
                        OutlinedButton(
                            onClick = { showBengali = !showBengali },
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(if (showBengali) "English দেখুন" else "বাংলা দেখুন")
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    Text(
                        if (showBengali) q.bengaliPrompt.orEmpty() else q.prompt,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            optionOrder.forEach { optionIndex ->
                val option = if (showBengali) {
                    q.bengaliOptions?.getOrNull(optionIndex) ?: q.options[optionIndex]
                } else {
                    q.options[optionIndex]
                }
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
                    enabled = true,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(58.dp),
                    shape = RoundedCornerShape(17.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            !answered -> Color(0xFFF4F6FA)
                            isSelected && isCorrect -> Color(0xFF20B957)
                            isSelected -> Color(0xFFE53935)
                            isCorrect -> Color(0xFF20B957)
                            else -> Color(0xFFE8EBF0)
                        },
                        contentColor = when {
                            !answered -> Color(0xFF26354A)
                            isSelected || isCorrect -> Color.White
                            else -> Color(0xFF8A93A0)
                        }
                    )
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (answered && isSelected && !isCorrect) {
                            Text("✕  ", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
                        } else if (answered && isCorrect) {
                            Text("✓  ", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
                        }
                        Text(option, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        if (answered && isCorrect) {
                            Text("  ✓", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }

            if (answered) {
                Spacer(Modifier.height(10.dp))
                Surface(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if (selected == q.correctIndex) Color(0xFFE8F8EE) else Color(0xFFFFECEC)
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            if (selected == q.correctIndex) "🎉 CORRECT! +5 XP +5 Coins" else "❌ WRONG ANSWER",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (selected == q.correctIndex) Color(0xFF138A43) else Color(0xFFC62828)
                        )
                        if (selected != q.correctIndex) {
                            Text(
                                "✓ Correct answer: \${q.correctAnswer}",
                                modifier = Modifier.padding(top = 5.dp),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF138A43)
                            )
                        }
                        if (q.explanation.isNotBlank()) {
                            Text(
                                q.explanation,
                                modifier = Modifier.padding(top = 4.dp),
                                fontSize = 13.sp,
                                color = Color(0xFF65738A),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
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
                            showBengali = false
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
private fun RealTestScreen(
    progress: Map<String, QuestionProgress>,
    onBackToHub: () -> Unit
) {
    val context = LocalContext.current
    val gameStore = remember { GameDataStore(context) }
    val scope = rememberCoroutineScope()
    var count by rememberSaveable { mutableIntStateOf(10) }
    var subject by rememberSaveable { mutableStateOf("Mixed") }
    var ids by rememberSaveable { mutableStateOf("") }
    val questions = remember(ids) { ids.split(",").filter { it.isNotBlank() }.mapNotNull(QuestionBank::findById) }
    var index by rememberSaveable { mutableIntStateOf(0) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var startedAt by rememberSaveable { mutableLongStateOf(0L) }
    var elapsed by rememberSaveable { mutableLongStateOf(0L) }
    var showSubmit by rememberSaveable { mutableStateOf(false) }
    var review by rememberSaveable { mutableStateOf(false) }
    val answers = remember { mutableStateMapOf<Int, Int>() }
    val testStore = remember { GameDataStore(context) }
    val testHistory by testStore.realTestHistory.collectAsState(initial = emptyList())

    LaunchedEffect(startedAt, finished) {
        if (startedAt > 0L && !finished) {
            while (true) {
                elapsed = ((System.currentTimeMillis() - startedAt) / 1000L).coerceAtLeast(0L)
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    fun timeText(value: Long): String {
        val m = value / 60
        val s = value % 60
        return "%02d:%02d".format(m, s)
    }

    fun startTest() {
        val pool = if (subject == "Mixed") QuestionBank.all else QuestionBank.bySubject(subject)
        val chosen = pool.shuffled().take(minOf(count, pool.size))
        ids = chosen.joinToString(",") { it.id }
        answers.clear()
        index = 0
        finished = false
        review = false
        showSubmit = false
        elapsed = 0L
        startedAt = System.currentTimeMillis()
    }

    fun submitTest() {
        elapsed = ((System.currentTimeMillis() - startedAt) / 1000L).coerceAtLeast(0L)
        val finalScore = questions.indices.count { answers[it] == questions[it].correctIndex }
        scope.launch { testStore.recordRealTest(subject, finalScore, questions.size, elapsed) }
        finished = true
        startedAt = 0L
        showSubmit = false
    }

    if (finished) {
        val correct = questions.indices.count { answers[it] == questions[it].correctIndex }
        val unanswered = questions.indices.count { !answers.containsKey(it) }
        val wrong = questions.size - correct - unanswered
        val percent = if (questions.isEmpty()) 0 else correct * 100 / questions.size
        if (!review) {
            Text("🏆 TEST COMPLETE", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(12.dp))
            Text(correct.toString() + " / " + questions.size, fontSize = 50.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
            Text(percent.toString() + "%", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DashboardStat("✅", "Correct", correct.toString(), Modifier.weight(1f))
                DashboardStat("❌", "Wrong", wrong.toString(), Modifier.weight(1f))
                DashboardStat("⭕", "Unanswered", unanswered.toString(), Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), color = Color.White, tonalElevation = 2.dp) {
                Column(Modifier.padding(16.dp)) {
                    Text("Test Summary", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Subject: " + subject, fontWeight = FontWeight.Bold)
                    Text("Questions: " + questions.size)
                    Text("Time taken: " + timeText(elapsed))
                }
            }
            Spacer(Modifier.height(14.dp))
            Button(onClick = { review = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) { Text("📝 REVIEW ANSWERS") }
            OutlinedButton(onClick = { startTest() }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(18.dp)) { Text("🔄 NEW TEST") }
            OutlinedButton(onClick = onBackToHub, modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(18.dp)) { Text("‹ Learning Hub") }
        } else {
            Text("📝 REVIEW ANSWERS", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            Text(correct.toString() + "/" + questions.size + " correct • " + timeText(elapsed), fontSize = 13.sp, color = Color(0xFF68778C))
            Spacer(Modifier.height(10.dp))
            questions.forEachIndexed { i, q ->
                val a = answers[i]
                Surface(Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(16.dp), color = if (a == q.correctIndex) Color(0xFFE8F8EE) else Color(0xFFFFECEC)) {
                    Column(Modifier.padding(14.dp)) {
                        Text("Q" + (i + 1) + ". " + q.prompt, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(if (a == null) "⭕ Not answered" else "Your answer: " + q.options[a] + if (a == q.correctIndex) " ✓" else " ✕", fontSize = 14.sp, modifier = Modifier.padding(top = 6.dp))
                        if (a != q.correctIndex) {
                            Text("Correct answer: " + q.correctAnswer + " ✓", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF138A43), modifier = Modifier.padding(top = 4.dp))
                            if (q.explanation.isNotBlank()) Text(q.explanation, fontSize = 12.sp, color = Color(0xFF65738A), modifier = Modifier.padding(top = 3.dp))
                        }
                    }
                }
            }
            Button(onClick = onBackToHub, modifier = Modifier.fillMaxWidth().padding(top = 10.dp), shape = RoundedCornerShape(18.dp)) { Text("‹ Back to Learning Hub") }
        }
    } else if (questions.isEmpty()) {
        Text("📋 REAL TEST", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text("Choose the test size and subject, then start.", fontSize = 14.sp, color = Color(0xFF68778C), textAlign = TextAlign.Center)
        if (testHistory.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            Text("📊 Recent Tests", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.fillMaxWidth())
            testHistory.take(5).forEach { entry ->
                val parts = entry.split("|")
                if (parts.size >= 5) {
                    val seconds = parts[3].toLongOrNull() ?: 0L
                    Surface(Modifier.fillMaxWidth().padding(vertical = 3.dp), shape = RoundedCornerShape(14.dp), color = Color.White, tonalElevation = 1.dp) {
                        Text(parts[0] + " • " + parts[1] + "/" + parts[2] + " • " + timeText(seconds), Modifier.padding(12.dp), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(Modifier.height(18.dp))
        Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = Color.White, tonalElevation = 2.dp) {
            Column(Modifier.padding(18.dp)) {
                Text("Number of Questions", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(10, 20, 30).forEach { n -> FilterChip(selected = count == n, onClick = { count = n }, label = { Text(n.toString(), fontWeight = FontWeight.Bold) }, modifier = Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(16.dp))
                Text("Subject", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(8.dp))
                listOf("Mixed", "Math", "English", "Science").chunked(2).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { s -> FilterChip(selected = subject == s, onClick = { subject = s }, label = { Text(if (s == "Mixed") "🔀 Mixed" else s, fontWeight = FontWeight.Bold) }, modifier = Modifier.weight(1f)) }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
        val available = if (subject == "Mixed") QuestionBank.all.size else QuestionBank.bySubject(subject).size
        Spacer(Modifier.height(12.dp))
        Text("Available: " + available + " questions • fresh random selection each test", fontSize = 12.sp, color = Color(0xFF68778C), textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Button(onClick = ::startTest, enabled = available >= count, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) { Text("🚀 START REAL TEST", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold) }
        Spacer(Modifier.height(8.dp))
        Text("Answers, marks and explanations stay hidden until submission.", fontSize = 12.sp, color = Color(0xFF68778C), textAlign = TextAlign.Center)
    } else {
        val q = questions[index]
        val order = remember(index, q.id) { q.options.indices.shuffled() }
        val current = answers[index]
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("REAL TEST", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
            Text("⏱ " + timeText(elapsed), fontWeight = FontWeight.Bold, color = Color(0xFF68778C))
        }
        Text("Question " + (index + 1) + " / " + questions.size, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(7.dp))
        LinearProgressIndicator(progress = { (index + 1) / questions.size.toFloat() }, Modifier.fillMaxWidth().height(8.dp))
        Spacer(Modifier.height(14.dp))
        Surface(shape = RoundedCornerShape(20.dp), color = Color.White, tonalElevation = 2.dp) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Text(q.subject + " • " + q.topic, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF315FBA))
                Spacer(Modifier.height(10.dp))
                Text(q.prompt, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
        Spacer(Modifier.height(12.dp))
        order.forEach { optionIndex ->
            Button(onClick = { answers[index] = optionIndex }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(58.dp), shape = RoundedCornerShape(17.dp), colors = ButtonDefaults.buttonColors(containerColor = if (current == optionIndex) Color(0xFFDCE9FF) else Color(0xFFF4F6FA), contentColor = Color(0xFF26354A))) { Text(q.options[optionIndex], fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { if (index > 0) index-- }, enabled = index > 0, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) { Text("← Previous") }
            Button(onClick = { if (index < questions.lastIndex) index++ else showSubmit = true }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) { Text(if (index == questions.lastIndex) "SUBMIT TEST" else "Next →") }
        }
        Spacer(Modifier.height(8.dp))
        Text(answers.size.toString() + " answered • " + (questions.size - answers.size) + " unanswered", fontSize = 12.sp, color = Color(0xFF68778C))
    }

    if (showSubmit) {
        AlertDialog(onDismissRequest = { showSubmit = false }, title = { Text("Submit Real Test?") }, text = {
            val unanswered = questions.indices.count { !answers.containsKey(it) }
            Text(if (unanswered == 0) "You have answered all questions. Submit now?" else unanswered.toString() + " question(s) are unanswered. Submit anyway?")
        }, confirmButton = { Button(onClick = ::submitTest) { Text("SUBMIT") } }, dismissButton = { OutlinedButton(onClick = { showSubmit = false }) { Text("CONTINUE") } })
    }
}

@Composable
private fun SubjectProgressCard(
    subject: String,
    accuracy: Int,
    mastered: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 1.dp
    ) {
        Column(
            Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(subject, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(5.dp))
            Text(
                "$accuracy%",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF315FBA)
            )
            Text(
                "$mastered/$total mastered",
                fontSize = 10.sp,
                color = Color(0xFF68778C),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { accuracy / 100f },
                modifier = Modifier.fillMaxWidth().height(5.dp)
            )
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
