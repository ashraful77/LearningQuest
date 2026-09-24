package com.ashraful.learningquest.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbidNumbersScreen(onBack: () -> Unit) {
    var number by remember { mutableIntStateOf(1) }
    Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFFFF7DF), Color.White))).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Home") }
            Text("🔢 Numbers", fontSize = 28.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF9A5A00))
        }
        Spacer(Modifier.height(35.dp))
        Text("Let's count!", fontSize = 20.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(20.dp))
        Card(Modifier.fillMaxWidth().height(300.dp), shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1D6))) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("🔢", fontSize = 52.sp)
                Text(number.toString(), fontSize = 110.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF9A5A00))
                Text("⭐".repeat(number.coerceAtMost(10)), fontSize = 24.sp)
            }
        }
        Spacer(Modifier.height(28.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = { number = (number - 1).coerceAtLeast(1) }, modifier = Modifier.weight(1f).height(58.dp), enabled = number > 1) {
                Text("← Previous", fontSize = 17.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
            Button(onClick = { number = if (number == 20) 1 else number + 1 }, modifier = Modifier.weight(1f).height(58.dp)) {
                Text(if (number == 20) "🔄 Start Again" else "Next →", fontSize = 19.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }
    }
}
