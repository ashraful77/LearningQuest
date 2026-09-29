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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.anishQuestionsFor

@Composable
fun AnishSubjectQuizScreen(subject: String, onBack: () -> Unit) {
    val questions = remember(subject) { anishQuestionsFor(subject) }
    var currentIndex by rememberSaveable(subject) { mutableIntStateOf(0) }
    var selected by rememberSaveable(subject) { mutableIntStateOf(-1) }
    var score by rememberSaveable(subject) { mutableIntStateOf(0) }

    if (questions.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("কোনও প্রশ্ন পাওয়া যায়নি।", fontSize = 20.sp)
        }
        return
    }

    val finished = currentIndex >= questions.size

    Column(
        Modifier.fillMaxSize()
            .background(Color(0xFFF7FAFF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "📚 অনিশ • \${subject}",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF315FBA)
        )
        Spacer(Modifier.height(5.dp))

        if (finished) {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉 পরীক্ষা শেষ!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "\${score} / \${questions.size}",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF315FBA)
                    )
                    Text("সঠিক উত্তর: \${score}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(18.dp))
                    Button(onClick = {
                        currentIndex = 0
                        selected = -1
                        score = 0
                    }) { Text("আবার দিই") }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = onBack) { Text("বিষয়গুলিতে ফিরে যাই") }
                }
            }
            return@Column
        }

        val question = questions[currentIndex]

        Text(
            "প্রশ্ন \${currentIndex + 1} / \${questions.size}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF60758A)
        )
        Spacer(Modifier.height(10.dp))

        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text(
                    "ID: \${question.id}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8A96A8)
                )
                Text(
                    "বিষয়: \${question.topic}",
                    fontSize = 12.sp,
                    color = Color(0xFF7043A8)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    question.question,
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF26354A)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        question.options.forEachIndexed { index, option ->
            val isSelected = selected == index
            val isCorrect = index == question.correctAnswer
            val container = when {
                selected >= 0 && isCorrect -> Color(0xFFDFF5E6)
                isSelected && !isCorrect -> Color(0xFFFFE3E3)
                else -> Color.White
            }

            Card(
                onClick = {
                    if (selected == -1) {
                        selected = index
                        if (index == question.correctAnswer) score++
                    }
                },
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = container),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "\${('A'.code + index).toChar()}.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF315FBA)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        option,
                        Modifier.weight(1f),
                        fontSize = 16.sp,
                        fontWeight = if (isSelected || (selected >= 0 && isCorrect)) FontWeight.Bold else FontWeight.Normal
                    )
                    if (selected >= 0 && isCorrect) {
                        Text("✓", fontSize = 22.sp, color = Color(0xFF23754A))
                    } else if (isSelected) {
                        Text("✗", fontSize = 22.sp, color = Color(0xFFC62828))
                    }
                }
            }
        }

        if (selected >= 0) {
            Spacer(Modifier.height(10.dp))
            Text(
                if (selected == question.correctAnswer) "🎉 সঠিক উত্তর!"
                else "💡 সঠিক উত্তর: \${question.options[question.correctAnswer]}",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected == question.correctAnswer) Color(0xFF23754A) else Color(0xFFC62828)
            )
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    currentIndex++
                    selected = -1
                },
                Modifier.fillMaxWidth()
            ) {
                Text(if (currentIndex == questions.lastIndex) "ফলাফল দেখুন" else "পরের প্রশ্ন →")
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(
            "প্রশ্ন ID: \${question.id}",
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 11.sp,
            color = Color(0xFF8A96A8)
        )
    }
}
