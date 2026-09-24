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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class ColorItem(val name: String, val emoji: String, val color: Color)

@Composable
fun AbidColorsScreen(onBack: () -> Unit) {
    val colors = listOf(
        ColorItem("Red", "❤️", Color(0xFFE53935)),
        ColorItem("Blue", "💙", Color(0xFF1E88E5)),
        ColorItem("Green", "💚", Color(0xFF43A047)),
        ColorItem("Yellow", "💛", Color(0xFFFDD835)),
        ColorItem("Orange", "🧡", Color(0xFFFB8C00)),
        ColorItem("Purple", "💜", Color(0xFF8E24AA)),
        ColorItem("Pink", "🩷", Color(0xFFE91E63)),
        ColorItem("Brown", "🤎", Color(0xFF795548))
    )
    var index by remember { mutableIntStateOf(0) }
    val current = colors[index]
    Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFFFF0F7), Color.White))).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Home", fontSize = 18.sp) }
            Text("🎨 Colors", fontSize = 28.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFFB02A7A))
        }
        Spacer(Modifier.height(25.dp))
        Text("Color ${index + 1} of ${colors.size}", fontSize = 17.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(18.dp))
        Card(Modifier.fillMaxWidth().height(330.dp), shape = RoundedCornerShape(32.dp), colors = CardDefaults.cardColors(containerColor = current.color.copy(alpha = 0.12f))) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(current.emoji, fontSize = 70.sp)
                Text(current.name.uppercase(), fontSize = 44.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = current.color)
                Text("This is ${current.name.lowercase()}!", fontSize = 20.sp, color = Color(0xFF60758A))
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = { index = (index - 1).coerceAtLeast(0) }, modifier = Modifier.weight(1f).height(58.dp), enabled = index > 0) { Text("← Previous", fontSize = 17.sp) }
            Button(onClick = { index = (index + 1) % colors.size }, modifier = Modifier.weight(1f).height(58.dp)) { Text(if (index == colors.lastIndex) "🔄 Again" else "Next →", fontSize = 18.sp) }
        }
    }
}
