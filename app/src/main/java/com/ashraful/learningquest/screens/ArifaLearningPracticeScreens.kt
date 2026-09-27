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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.AdaptiveQuestionEngine
import com.ashraful.learningquest.data.BankQuestion
import com.ashraful.learningquest.data.GameDataStore
import com.ashraful.learningquest.data.QuestionBank
import com.ashraful.learningquest.data.QuestionProgressStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ArifaReviewMistakesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val progressStore = remember { QuestionProgressStore(context) }
    val gameStore = remember { GameDataStore(context) }
    val progress by progressStore.progress.collectAsState(initial = emptyMap())
    val scope = rememberCoroutineScope()
    val mistakes = remember(progress) {
        QuestionBank.all.filter { (progress[it.id]?.wrong ?: 0) > 0 }
            .sortedWith(compareByDescending<BankQuestion> { progress[it.id]?.wrong ?: 0 }
                .thenBy { progress[it.id]?.accuracy ?: 0 })
    }
    ReviewQuiz("🔁 Review Mistakes",
        if (mistakes.isEmpty()) "No mistakes to review yet. Keep learning!" else "Questions you missed come back for focused practice.",
        mistakes.take(10), progressStore, gameStore, scope, onBack)
}

@Composable
fun ArifaTopicPracticeScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val progressStore = remember { QuestionProgressStore(context) }
    val gameStore = remember { GameDataStore(context) }
    val progress by progressStore.progress.collectAsState(initial = emptyMap())
    val scope = rememberCoroutineScope()
    var topic by rememberSaveable { mutableStateOf<String?>(null) }
    val topics = QuestionBank.all.map { it.topic }.distinct().sorted()
    val topicQuestions = remember(progress, topic) {
        topic?.let { AdaptiveQuestionEngine.select(QuestionBank.all, progress, 10, topic = it) } ?: emptyList()
    }
    if (topic == null) {
        TopicPicker(topics, progress, onBack) { topic = it }
    } else {
        ReviewQuiz("🎯 $$topic Practice", "Adaptive practice focused on one topic.",
            topicQuestions, progressStore, gameStore, scope) { topic = null }
    }
}

@Composable
private fun TopicPicker(
    topics: List<String>,
    progress: Map<String, com.ashraful.learningquest.data.QuestionProgress>,
    onBack: () -> Unit,
    onTopic: (String) -> Unit
) {
    Column(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFEAF2FF), Color.White)))
            .verticalScroll(rememberScrollState()).padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onBack, shape = RoundedCornerShape(16.dp)) { Text("‹ Home") }
            Spacer(Modifier.width(10.dp))
            Text("🎯 Topic Practice", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
        }
        Spacer(Modifier.height(18.dp))
        Text("Choose what you want to practise", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
        Text("Each topic uses your history to choose suitable questions.", fontSize = 13.sp, color = Color(0xFF68778C), textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        topics.forEach { topic ->
            val qs = QuestionBank.all.filter { it.topic == topic }
            val attempted = qs.count { progress[it.id]?.attempts ?: 0 > 0 }
            val totalAttempts = qs.mapNotNull { progress[it.id] }.sumOf { it.attempts }
            val totalCorrect = qs.mapNotNull { progress[it.id] }.sumOf { it.correct }
            val accuracy = if (totalAttempts == 0) 0 else totalCorrect * 100 / totalAttempts
            Card(onClick = { onTopic(topic) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(1.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(topic, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text("$attempted/${qs.size} attempted • $accuracy% accuracy", fontSize = 12.sp, color = Color(0xFF68778C))
                    }
                    Text("Practice →", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF315FBA))
                }
            }
        }
    }
}

@Composable
private fun ReviewQuiz(
    title: String,
    subtitle: String,
    questions: List<BankQuestion>,
    progressStore: QuestionProgressStore,
    gameStore: GameDataStore,
    scope: CoroutineScope,
    onBack: () -> Unit
) {
    var index by rememberSaveable { mutableIntStateOf(0) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var selected by rememberSaveable { mutableStateOf<Int?>(null) }
    var answered by rememberSaveable { mutableStateOf(false) }
    var showBengali by rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFEAF2FF), Color.White)))
            .verticalScroll(rememberScrollState()).padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onBack, shape = RoundedCornerShape(16.dp)) { Text("‹ Back") }
            Spacer(Modifier.width(10.dp))
            Text(title, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
        }
        Spacer(Modifier.height(8.dp))
        Text(subtitle, fontSize = 13.sp, color = Color(0xFF68778C), textAlign = TextAlign.Center)

        if (questions.isEmpty()) {
            Spacer(Modifier.height(60.dp))
            Text("🎉", fontSize = 64.sp)
            Text("Nothing to review!", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            Text("Keep answering questions and this area will update automatically.", Modifier.padding(10.dp), textAlign = TextAlign.Center, color = Color(0xFF68778C))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) { Text("Back") }
            return@Column
        }

        val q = questions[index]
        val order = remember(index, q.id) { q.options.indices.shuffled() }
        Spacer(Modifier.height(14.dp))
        LinearProgressIndicator(progress = { (index + 1) / questions.size.toFloat() }, Modifier.fillMaxWidth().height(8.dp))
        Text("Question ${index + 1} / ${questions.size}", Modifier.padding(top = 7.dp), fontWeight = FontWeight.Bold, color = Color(0xFF68778C))
        Spacer(Modifier.height(12.dp))
        Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = Color.White, tonalElevation = 2.dp) {
            Column(Modifier.padding(20.dp)) {
                Text("${q.subject} • ${q.topic}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF315FBA))
                Spacer(Modifier.height(8.dp))
                Text(if (showBengali && q.hasBengali) q.bengaliPrompt.orEmpty() else q.prompt, fontSize = 21.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                if (q.hasBengali) {
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(onClick = { showBengali = !showBengali }, shape = RoundedCornerShape(12.dp)) {
                        Text(if (showBengali) "English দেখুন" else "বাংলা দেখুন")
                    }
                }
            }
        }
        order.forEach { optionIndex ->
            val correct = optionIndex == q.correctIndex
            val chosen = selected == optionIndex
            Button(onClick = {
                if (!answered) {
                    selected = optionIndex
                    answered = true
                    scope.launch {
                        progressStore.recordAnswer(q.id, correct)
                        gameStore.recordAnswer(correct)
                        if (correct) { score++; gameStore.addReward(5, 5) }
                    }
                }
            }, enabled = !answered, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when { !answered -> Color.White; correct -> Color(0xFF20B957); chosen -> Color(0xFFE94055); else -> Color.White },
                    contentColor = if (answered && (correct || chosen)) Color.White else Color(0xFF26354A)
                )
            ) {
                Text(if (showBengali && q.hasBengali) q.bengaliOptions?.getOrNull(optionIndex) ?: q.options[optionIndex] else q.options[optionIndex], fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        if (answered) {
            Spacer(Modifier.height(8.dp))
            Text(if (selected == q.correctIndex) "🎉 Correct! +5 XP +5 Coins" else "💡 Correct answer: " + if (showBengali && q.hasBengali) q.bengaliCorrectAnswer.orEmpty() else q.correctAnswer, fontWeight = FontWeight.Bold)
            if (q.explanation.isNotBlank()) Text(q.explanation, Modifier.padding(top = 5.dp), fontSize = 13.sp, color = Color(0xFF65738A), textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                if (index == questions.lastIndex) onBack() else { index++; selected = null; answered = false }
            }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Text(if (index == questions.lastIndex) "Finish" else "Next →")
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Score: $score", fontWeight = FontWeight.Bold, color = Color(0xFF68778C))
    }
}
