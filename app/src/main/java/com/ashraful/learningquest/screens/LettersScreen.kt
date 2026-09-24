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
fun AbidLettersScreen(onBack: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }
    val letters = ('A'..'Z').toList()
    Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFEAF7FF), Color.White))).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Home") }
            Text("🔤 Letters", fontSize = 28.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF1769AA))
        }
        Spacer(Modifier.height(35.dp))
        Text("Letter ${index + 1} of 26", fontSize = 16.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(20.dp))
        Card(Modifier.fillMaxWidth().height(300.dp), shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F4FF))) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("🔤", fontSize = 52.sp)
                Text(letters[index].toString(), fontSize = 110.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF1769AA))
                Text(letters[index].toString().lowercase(), fontSize = 32.sp, color = Color(0xFF60758A))
            }
        }
        Spacer(Modifier.height(28.dp))
        Button(onClick = { index = (index + 1) % letters.size }, modifier = Modifier.fillMaxWidth().height(58.dp)) { Text(if (index == 25) "🔄 Start Again" else "Next Letter →", fontSize = 19.sp) }
    }
}
