package com.ashraful.learningquest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun AbidMatchNumbersTestScreen(onBack: () -> Unit) {
    var round by remember { mutableIntStateOf(0) }
    var matches by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    var dragStart by remember { mutableIntStateOf(-1) }
    var dragPoint by remember { mutableStateOf<Offset?>(null) }
    var message by remember { mutableStateOf("") }
    var completedRounds by remember { mutableIntStateOf(0) }

    val numbers = remember(round) { (1..10).shuffled().take(5) }
    val objects = remember(round) { numbers.shuffled() }
    val density = LocalDensity.current
    val emoji = listOf("🍎", "⭐", "🔵", "🌸", "🎈")

    fun newRound() {
        round++
        matches = emptyMap()
        dragStart = -1
        dragPoint = null
        message = ""
    }

    Column(
        Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF4FF), Color.White, Color(0xFFFFF5DE)))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Home", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.weight(1f))
            Text("Round \${round + 1}", fontWeight = FontWeight.Bold, color = Color(0xFF2457A6))
        }
        Text("🔗 Match the Numbers", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF2457A6))
        Text("Count the objects. Drag a line from each number to the matching group.", textAlign = TextAlign.Center, fontSize = 15.sp, color = Color(0xFF536579))
        Spacer(Modifier.height(6.dp))
        Card(Modifier.fillMaxWidth().weight(1f), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(Color.White)) {
            BoxWithConstraints(Modifier.fillMaxSize().padding(8.dp)) {
                val rowHeight = maxHeight / 5f
                val leftX = 62.dp
                val rightX = maxWidth - 62.dp
                val leftCenters = numbers.indices.map { Offset(leftX.value, (rowHeight * it + rowHeight / 2).value) }
                val rightCenters = objects.indices.map { Offset(rightX.value, (rowHeight * it + rowHeight / 2).value) }

                Canvas(
                    Modifier.fillMaxSize().pointerInput(numbers, objects, matches) {
                        detectDragGestures(
                            onDragStart = { point ->
                                val leftCenters = leftCentersDp.map { Offset(with(density) { it.x.dp.toPx() }, with(density) { it.y.dp.toPx() }) }
                                val nearest = leftCenters.indices.minByOrNull { abs(leftCenters[it].y - point.y) }
                                if (nearest != null && abs(leftCenters[nearest].y - point.y) < 70f && !matches.containsKey(numbers[nearest])) {
                                    dragStart = nearest
                                    dragPoint = point
                                }
                            },
                            onDrag = { change, _ -> if (dragStart >= 0) { change.consume(); dragPoint = change.position } },
                            onDragEnd = {
                                if (dragStart >= 0) {
                                    val end = dragPoint
                                    val rightCenters = rightCentersDp.map { Offset(with(density) { it.x.dp.toPx() }, with(density) { it.y.dp.toPx() }) }
                                    val nearestRight = if (end != null) rightCenters.indices.minByOrNull { abs(rightCenters[it].y - end.y) } else null
                                    if (nearestRight != null && abs(rightCenters[nearestRight].y - end!!.y) < 70f) {
                                        val number = numbers[dragStart]
                                        val target = objects[nearestRight]
                                        if (number == target) {
                                            matches = matches + (number to target)
                                            message = "✓ Correct! Keep going!"
                                        } else {
                                            message = "Try again — count the objects carefully."
                                        }
                                    }
                                }
                                dragStart = -1
                                dragPoint = null
                            },
                            onDragCancel = { dragStart = -1; dragPoint = null }
                        )
                    }
                ) {
                    val leftCenters = leftCentersDp.map { Offset(with(density) { it.x.dp.toPx() }, with(density) { it.y.dp.toPx() }) }
                    val rightCenters = rightCentersDp.map { Offset(with(density) { it.x.dp.toPx() }, with(density) { it.y.dp.toPx() }) }
                    matches.forEach { (number, target) ->
                        val li = numbers.indexOf(number)
                        val ri = objects.indexOf(target)
                        if (li >= 0 && ri >= 0) drawLine(Color(0xFF20A65A), leftCenters[li], rightCenters[ri], strokeWidth = 9f)
                    }
                    if (dragStart >= 0 && dragPoint != null) drawLine(Color(0xFF4F83D1), leftCenters[dragStart], dragPoint!!, strokeWidth = 7f)
                }

                numbers.forEachIndexed { i, n ->
                    Box(Modifier.offset(x = leftX - 38.dp, y = rowHeight * i + rowHeight / 2 - 27.dp).size(76.dp, 54.dp)
                        .background(if (matches.containsKey(n)) Color(0xFFDDF7E7) else Color(0xFFEAF2FF), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center) {
                        Text(n.toString(), fontSize = 29.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF173C8C))
                    }
                }

                objects.forEachIndexed { i, n ->
                    Column(Modifier.offset(x = rightX - 72.dp, y = rowHeight * i + rowHeight / 2 - 38.dp).width(144.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text((0 until n).joinToString(" ") { emoji[i % emoji.size] }, fontSize = 23.sp, textAlign = TextAlign.Center, maxLines = 2)
                    }
                }
            }
        }
        Spacer(Modifier.height(7.dp))
        Text(message.ifBlank { "5 matches • Numbers are from 1 to 10" }, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (message.startsWith("✓")) Color(0xFF159447) else Color(0xFF60758A), textAlign = TextAlign.Center)
        if (matches.size == 5) {
            Spacer(Modifier.height(4.dp))
            Text("🎉 Excellent! All 5 matched!", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF159447))
            Button(onClick = { completedRounds++; newRound() }, Modifier.fillMaxWidth().height(50.dp)) { Text("Next Round →", fontSize = 17.sp) }
        } else {
            Text("\${5 - matches.size} matches remaining", fontSize = 12.sp, color = Color(0xFF8A96A8))
        }
    }
}