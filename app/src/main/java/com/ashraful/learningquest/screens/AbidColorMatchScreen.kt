package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import kotlin.random.Random

private data class ColorChoice(val name: String, val color: Color)

@Composable
fun AbidColorMatchScreen(onBack: () -> Unit) {
    val choices = listOf(
        ColorChoice("RED", Color(0xFFE53935)), ColorChoice("BLUE", Color(0xFF1E88E5)),
        ColorChoice("GREEN", Color(0xFF43A047)), ColorChoice("YELLOW", Color(0xFFFDD835)),
        ColorChoice("ORANGE", Color(0xFFFB8C00)), ColorChoice("PURPLE", Color(0xFF8E24AA)),
        ColorChoice("PINK", Color(0xFFE91E63)), ColorChoice("BROWN", Color(0xFF795548))
    )
    var round by remember { mutableIntStateOf(0) }
    val targetIndex = remember(round) { Random.nextInt(choices.size) }
    val options = remember(round) { (choices.indices.filter { it != targetIndex }.shuffled().take(3) + targetIndex).shuffled() }
    var answered by remember { mutableStateOf<Int?>(null) }

    Column(Modifier.fillMaxSize().background(Color(0xFFFFF5FA)).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Home", fontSize = 18.sp) }
            Text("🎨 Match Colors", fontSize = 27.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFFB02A7A))
        }
        Spacer(Modifier.height(20.dp))
        Text("Which color is this?", fontSize = 23.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        Card(Modifier.size(190.dp), shape = RoundedCornerShape(35.dp), colors = CardDefaults.cardColors(containerColor = choices[targetIndex].color)) {}
        Spacer(Modifier.height(24.dp))
        options.forEach { option ->
            val chosen = answered == option
            val isCorrect = answered != null && option == targetIndex
            val bg = when {
                answered == null -> Color(0xFF7E57C2)
                chosen && option == targetIndex -> Color(0xFF20B957)
                chosen -> Color(0xFFE94055)
                isCorrect -> Color(0xFF20B957)
                else -> Color(0xFFE9EDF2)
            }
            Button(
                onClick = { answered = option },
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).height(56.dp),
                enabled = answered == null,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = bg,
                    contentColor = if (answered != null && (chosen || isCorrect)) Color.White else Color.White
                )
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        when {
                            chosen && option == targetIndex -> "✓"
                            chosen -> "✕"
                            isCorrect -> "✓"
                            else -> ""
                        },
                        fontSize = 24.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                        modifier = Modifier.width(30.dp)
                    )
                    Text(choices[option].name, fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
            }
        }
        if (answered != null) {
            Spacer(Modifier.height(12.dp))
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = if (answered == targetIndex) Color(0xFFE4F8EA) else Color(0xFFFFE5E8))
            ) {
                Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        if (answered == targetIndex) "🎉 CORRECT!" else "❌ WRONG ANSWER",
                        fontSize = 24.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                        color = if (answered == targetIndex) Color(0xFF138A40) else Color(0xFFD52E45)
                    )
                    if (answered != targetIndex) {
                        Text("Correct color: " + choices[targetIndex].name, fontWeight = FontWeight.Bold, color = Color(0xFFD52E45))
                    } else {
                        Text("Great job! 🌟", fontWeight = FontWeight.Bold, color = Color(0xFF138A40))
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { round++; answered = null }, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text("Next Color →", fontSize = 19.sp) }
        }
    }
}
