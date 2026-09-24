package com.ashraful.learningquest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun AbidNumberMatchScreen(onBack: () -> Unit) {
    var round by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var correct by remember { mutableStateOf<Boolean?>(null) }

    val target = remember(round) { Random.nextInt(1, 11) }
    val options = remember(round) {
        val wrong = (1..10).filter { it != target }.shuffled().take(3)
        (wrong + target).shuffled()
    }

    val emojis = listOf("🍎", "⭐", "🍓", "🦋", "🌸", "🐟", "🍪", "🎈", "🐰", "🌈")

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF5DE), Color.White, Color(0xFFEAF7FF))))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth().padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("‹ Home") }
            Text(
                "🔢 Count & Match",
                fontSize = 25.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                color = Color(0xFF9A5A00)
            )
        }

        Text("How many objects are there?", fontSize = 18.sp, color = Color(0xFF536579))
        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                Modifier.fillMaxSize().padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val columns = 3
                    val rows = (target + columns - 1) / columns
                    val cellW = size.width / columns
                    val cellH = size.height / rows.coerceAtLeast(1)
                    repeat(target) { index ->
                        val col = index % columns
                        val row = index / columns
                        val x = cellW * (col + 0.5f)
                        val y = cellH * (row + 0.5f)
                        drawCircle(
                            color = listOf(
                                Color(0xFFFF6B6B), Color(0xFFFFC857),
                                Color(0xFF62C370), Color(0xFF5DADE2),
                                Color(0xFFB084CC)
                            )[index % 5],
                            radius = 30f,
                            center = Offset(x, y)
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(emojis[(target - 1) % emojis.size], fontSize = 46.sp)
                    Text("Count carefully! 👀", fontSize = 16.sp, color = Color(0xFF71809A))
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Text("Choose the correct number", fontSize = 17.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color(0xFF40516A))
        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            options.forEach { number ->
                val bg = when {
                    selected == number && correct == true -> Color(0xFFB9F6C7)
                    selected == number && correct == false -> Color(0xFFFFC1C1)
                    else -> Color(0xFFFFF1D6)
                }
                Card(
                    onClick = {
                        if (correct != true) {
                            selected = number
                            correct = number == target
                        }
                    },
                    modifier = Modifier.weight(1f).height(76.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = bg),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(number.toString(), fontSize = 30.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF8A5200))
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        when (correct) {
            true -> {
                Text("🎉 Great counting!", fontSize = 21.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF159447))
                Button(
                    onClick = { round++; selected = -1; correct = null },
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) { Text("🔄 Next", fontSize = 18.sp) }
            }
            false -> Text("Try again! 😊", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color(0xFFD52E45))
            null -> Spacer(Modifier.height(52.dp))
        }

        Spacer(Modifier.height(10.dp))
    }
}
