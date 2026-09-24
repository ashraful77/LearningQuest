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
import kotlin.random.Random

private val fruits = listOf("🍎","🍌","🍊","🍇","🍓","🍉","🍐","🍒")

@Composable
fun AbidMemoryPairsScreen(onBack: () -> Unit) {
    var round by remember { mutableIntStateOf(0) }
    val target = remember(round) { fruits[Random.nextInt(fruits.size)] }
    val cards = remember(round) { (fruits.filter { it != target }.shuffled().take(3) + target).shuffled() }
    var selected by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize().background(Color(0xFFF4F0FF)).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Home", fontSize = 18.sp) }
            Text("🧠 Memory Match", fontSize = 27.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF7043A8))
        }
        Spacer(Modifier.height(18.dp))
        Text("Find the same fruit!", fontSize = 23.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Remember: $target", fontSize = 20.sp, color = Color(0xFF7043A8))
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            cards.take(2).forEach { card(it, selected, target, Modifier.weight(1f)) { if (selected == null) selected = it } }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            cards.drop(2).forEach { card(it, selected, target, Modifier.weight(1f)) { if (selected == null) selected = it } }
        }
        Spacer(Modifier.height(18.dp))
        if (selected != null) {
            Text(if (selected == target) "🎉 Excellent memory!" else "😊 Keep practicing!", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Button(onClick = { round++; selected = null }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Text("Next Memory →", fontSize = 19.sp)
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("⭐ Score: $score", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}

@Composable
private fun card(value: String, selected: String?, target: String, modifier: Modifier, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = modifier.padding(vertical = 6.dp).height(130.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = if (selected == value && value == target) Color(0xFFDDF5E5) else Color.White)) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(value, fontSize = 52.sp) }
    }
}
