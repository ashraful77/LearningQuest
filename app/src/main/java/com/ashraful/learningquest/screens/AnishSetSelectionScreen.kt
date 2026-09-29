package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.GameDataStore
import com.ashraful.learningquest.data.anishQuestionsFor

private const val SET_SIZE = 20
private const val MAX_SETS = 3

@Composable
fun AnishSetSelectionScreen(
    subject: String,
    onSetSelected: (Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val answeredIds by store.anishAnsweredQuestionIds().collectAsState(initial = emptySet())
    val gameData by store.gameData.collectAsState(initial = null)
    val questions = remember(subject) {
        anishQuestionsFor(subject).take(MAX_SETS * SET_SIZE)
    }
    val sets = remember(questions) { questions.chunked(SET_SIZE).take(MAX_SETS) }

    Column(
        Modifier.fillMaxSize()
            .background(Color(0xFFF7FAFF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("📚 $subject", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
        Text("20টি প্রশ্নের সেট • শেষ হলে আবার অনুশীলন করা যাবে", fontSize = 14.sp, color = Color(0xFF6B7890))
        Spacer(Modifier.height(14.dp))

        sets.forEachIndexed { index, setQuestions ->
            val setNumber = index + 1
            val answeredCount = setQuestions.count { it.id in answeredIds }
            val completed = answeredCount == setQuestions.size
            val allSet1sCompleted = (gameData?.anishTotalQuestions ?: 0) >= 120
            val unlocked = setNumber == 1 ||
                (setNumber == 2 && allSet1sCompleted) ||
                sets.getOrNull(index - 1)?.all { it.id in answeredIds } == true

            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        completed -> Color(0xFFEAF8EF)
                        unlocked -> Color.White
                        else -> Color(0xFFE9EDF3)
                    }
                ),
                elevation = CardDefaults.cardElevation(1.dp),
                onClick = { if (unlocked) onSetSelected(setNumber) }
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        when {
                            completed -> "✅"
                            unlocked -> "📖"
                            else -> "🔒"
                        },
                        fontSize = 30.sp
                    )
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Set $setNumber", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF263B63))
                        Text(
                            when {
                                completed -> "সম্পূর্ণ • $answeredCount/${setQuestions.size} প্রশ্ন"
                                unlocked -> "$answeredCount/${setQuestions.size} প্রশ্ন সম্পন্ন"
                                else -> "আগের সেট শেষ করলে আনলক হবে"
                            },
                            fontSize = 13.sp,
                            color = Color(0xFF68768A)
                        )
                        Spacer(Modifier.height(7.dp))
                        LinearProgressIndicator(
                            progress = { (answeredCount.toFloat() / setQuestions.size).coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth().height(7.dp),
                            color = if (completed) Color(0xFF2E9B63) else Color(0xFF4A73C7),
                            trackColor = Color(0xFFDDE5F2)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        if (completed) "PRACTICE" else if (unlocked) "START" else "LOCKED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (unlocked) Color(0xFF315FBA) else Color(0xFF8A94A4)
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("← বিষয়গুলিতে ফিরে যাও", fontWeight = FontWeight.Bold)
        }
    }
}
