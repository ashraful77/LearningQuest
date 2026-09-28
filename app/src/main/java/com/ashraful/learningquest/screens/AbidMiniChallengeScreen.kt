package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.AbidProgressStore
import kotlinx.coroutines.launch

private data class AbidMiniQuestion(
    val question: String,
    val options: List<String>,
    val answer: Int,
    val emoji: String
)

private val abidMiniQuestions = listOf(
    AbidMiniQuestion("Which letter comes first?", listOf("A", "B", "C", "D"), 0, "🔤"),
    AbidMiniQuestion("What comes after 4?", listOf("3", "5", "6", "8"), 1, "🔢"),
    AbidMiniQuestion("Which one is RED?", listOf("🔵", "🔴", "🟢", "🟡"), 1, "🎨"),
    AbidMiniQuestion("Which shape has 3 sides?", listOf("○", "□", "△", "▭"), 2, "🔷"),
    AbidMiniQuestion("How many stars? ⭐⭐⭐", listOf("2", "3", "4", "5"), 1, "⭐")
)

@Composable
fun AbidMiniChallengeScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { AbidProgressStore(context) }
    val scope = rememberCoroutineScope()
    var index by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var score by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    var saved by remember { mutableStateOf(false) }

    val question = abidMiniQuestions[index]

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFEAF7FF), Color.White, Color(0xFFFFF7DF)))
        )
    ) {
        Column(
            Modifier.fillMaxSize().padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) { Text("‹ Back", fontWeight = FontWeight.Bold) }
                Text(
                    "⭐ Little Challenge",
                    Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1769AA)
                )
                Spacer(Modifier.width(62.dp))
            }

            if (!finished) {
                Text("Question ${index + 1} of ${abidMiniQuestions.size}", fontSize = 14.sp, color = Color(0xFF60758A))
                Spacer(Modifier.height(14.dp))

                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(question.emoji, fontSize = 46.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            question.question,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF24364B)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                question.options.forEachIndexed { optionIndex, option ->
                    val bg = when {
                        selected == optionIndex && optionIndex == question.answer -> Color(0xFFC8F7D2)
                        selected == optionIndex -> Color(0xFFFFD0D0)
                        else -> Color.White
                    }
                    Button(
                        onClick = {
                            if (selected == -1) {
                                selected = optionIndex
                                if (optionIndex == question.answer) score++
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = bg,
                            contentColor = Color(0xFF24364B)
                        )
                    ) {
                        Text(
                            option + if (selected != -1 && optionIndex == question.answer) "  ✓" else if (selected == optionIndex) "  ✕" else "",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (selected != -1) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        if (selected == question.answer) "🎉 Great job! +1 ⭐" else "💡 Let's learn and try the next one!",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selected == question.answer) Color(0xFF23754A) else Color(0xFF9A5A00)
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (index == abidMiniQuestions.lastIndex) finished = true
                            else {
                                index++
                                selected = -1
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(if (index == abidMiniQuestions.lastIndex) "FINISH 🏆" else "NEXT →", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            } else {
                LaunchedEffect(Unit) {
                    if (!saved) {
                        scope.launch {
                            store.completeActivity("games", stars = score.coerceAtLeast(1), xp = 5)
                            saved = true
                        }
                    }
                }

                Spacer(Modifier.height(25.dp))
                Text("🏆", fontSize = 70.sp)
                Text("Amazing, Abid!", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
                Spacer(Modifier.height(10.dp))
                Text("$score / ${abidMiniQuestions.size} correct", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text(
                    if (score == abidMiniQuestions.size) "🌟 Super Learner!" else "👏 Good try! Keep learning!",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF23754A)
                )
                Spacer(Modifier.height(18.dp))
                Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFFFF1D6)) {
                    Text(
                        "+${score.coerceAtLeast(1)} ⭐   +5 XP",
                        Modifier.padding(horizontal = 24.dp, vertical = 14.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF9A5A00)
                    )
                }
                Spacer(Modifier.height(22.dp))
                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("BACK TO MY LEARNING", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}
