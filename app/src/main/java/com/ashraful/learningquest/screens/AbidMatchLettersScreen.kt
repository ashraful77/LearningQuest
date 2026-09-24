package com.ashraful.learningquest.screens

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbidMatchLettersScreen(onBack: () -> Unit) {
    var round by remember { mutableIntStateOf(0) }
    val pairs = remember(round) { ('A'..'Z').shuffled().take(5) }
    val leftLetters = pairs.map { it.lowercaseChar() }
    val rightLetters = remember(round) { pairs.shuffled() }
    var matched by remember { mutableStateOf(setOf<Char>()) }
    var dragging by remember { mutableStateOf<Char?>(null) }
    var dragStart by remember { mutableStateOf(Offset.Unspecified) }
    var dragCurrent by remember { mutableStateOf(Offset.Unspecified) }
    var wrong by remember { mutableStateOf(false) }

    fun pointFor(index: Int, width: Float, height: Float, right: Boolean): Offset {
        val x = if (right) width * 0.75f else width * 0.25f
        return Offset(x, height * (index + 1).toFloat() / 6f)
    }

    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFEAF7FF), Color.White, Color(0xFFFFF1D6))))) {
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth().padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) { Text("‹ Home", fontSize = 19.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                Text("🔗 Alphabet Match", fontSize = 31.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF1769AA))
            }
            Text("Drag lowercase → matching CAPITAL", fontSize = 19.sp, color = Color(0xFF60758A), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text("Round ${round + 1}  •  Match all 5!", fontSize = 16.sp, color = Color(0xFF7B3FC6), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            Canvas(
                Modifier.fillMaxWidth().weight(1f).pointerInput(round, matched, rightLetters) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val nearest = leftLetters.mapIndexed { i, letter -> letter to pointFor(i, size.width.toFloat(), size.height.toFloat(), false) }
                                .minByOrNull { (_, p) -> distanceSquared(p, offset) }
                            if (nearest != null && distanceSquared(nearest.second, offset) < 14000f && nearest.first.uppercaseChar() !in matched) {
                                dragging = nearest.first
                                dragStart = nearest.second
                                dragCurrent = offset
                                wrong = false
                            }
                        },
                        onDrag = { change, _ ->
                            if (dragging != null) {
                                dragCurrent = change.position
                            }
                        },
                        onDragEnd = {
                            val source = dragging
                            if (source != null) {
                                val target = rightLetters.mapIndexed { i, letter -> letter to pointFor(i, size.width.toFloat(), size.height.toFloat(), true) }
                                    .minByOrNull { (_, p) -> distanceSquared(p, dragCurrent) }
                                if (target != null && distanceSquared(target.second, dragCurrent) < 7000f && target.first == source.uppercaseChar()) {
                                    matched = matched + target.first
                                    wrong = false
                                } else {
                                    wrong = true
                                }
                            }
                            dragging = null
                            dragStart = Offset.Unspecified
                            dragCurrent = Offset.Unspecified
                        }
                    )
                }
            ) {
                val leftX = size.width * 0.25f
                val rightX = size.width * 0.75f

                matched.forEach { upper ->
                    val li = leftLetters.indexOf(upper.lowercaseChar())
                    val ri = rightLetters.indexOf(upper)
                    if (li >= 0 && ri >= 0) drawLine(Color(0xFF20B957), pointFor(li, size.width, size.height, false), pointFor(ri, size.width, size.height, true), strokeWidth = 18f, cap = StrokeCap.Round)
                }

                if (dragging != null && dragStart != Offset.Unspecified && dragCurrent != Offset.Unspecified)
                    drawLine(Color(0xFF7B3FC6), dragStart, dragCurrent, strokeWidth = 16f, cap = StrokeCap.Round)

                leftLetters.forEachIndexed { i, letter ->
                    val p = pointFor(i, size.width, size.height, false)
                    drawCircle(Color(0xFFE8F4FF), 108f, p)
                    drawCircle(Color(0xFF1769AA), 108f, p, style = Stroke(9f))
                    drawContext.canvas.nativeCanvas.drawText(letter.toString(), leftX, p.y + 14f, Paint().apply {
                        textSize = 112f; textAlign = Paint.Align.CENTER
                        color = android.graphics.Color.rgb(23,105,170); typeface = Typeface.create("sans-serif-rounded", Typeface.BOLD)
                    })
                }
                rightLetters.forEachIndexed { i, letter ->
                    val p = pointFor(i, size.width, size.height, true)
                    drawCircle(Color(0xFFFFF1D6), 82f, p)
                    drawCircle(Color(0xFF9A5A00), 82f, p, style = Stroke(8f))
                    drawContext.canvas.nativeCanvas.drawText(letter.toString(), rightX, p.y + 14f, Paint().apply {
                        textSize = 94f; textAlign = Paint.Align.CENTER
                        color = android.graphics.Color.rgb(154,90,0); typeface = Typeface.DEFAULT_BOLD
                    })
                }
            }

            if (matched.size == 5) {
                Text("🎉 All 5 matched!", fontSize = 25.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF159447))
                Spacer(Modifier.height(6.dp))
                Button(onClick = { round++; matched = emptySet(); wrong = false }, modifier = Modifier.fillMaxWidth().height(54.dp)) { Text("🔄 Next Round", fontSize = 20.sp) }
            } else {
                Text("Matched: ${matched.size} / 5", fontSize = 21.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color(0xFF40516A))
                if (wrong) Text("Try again! 😊", color = Color(0xFFD52E45), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

private fun distanceSquared(a: Offset, b: Offset): Float {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return dx * dx + dy * dy
}
