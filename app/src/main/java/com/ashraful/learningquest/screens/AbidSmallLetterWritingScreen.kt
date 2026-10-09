package com.ashraful.learningquest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbidSmallLetterWritingScreen(onBack: () -> Unit) {
    val letters = ('a'..'z').map { it.toString() }
    var index by remember { mutableIntStateOf(0) }
    val strokes = remember { mutableStateListOf<List<Offset>>() }
    var currentStroke by remember { mutableStateOf<List<Offset>>(emptyList()) }

    fun clearDrawing() {
        strokes.clear()
        currentStroke = emptyList()
    }

    Column(
        Modifier.fillMaxSize().background(Color(0xFFF0F7FF)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Back") }
            Text("✍️ Small Letter Writing", modifier = Modifier.weight(1f), fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
        }
        Text("Trace the small letter with your finger", fontSize = 16.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(8.dp))
        Text("Letter ${index + 1} of ${letters.size}", fontWeight = FontWeight.Bold, color = Color(0xFF1769AA))
        Spacer(Modifier.height(8.dp))
        Card(Modifier.fillMaxWidth().weight(1f), shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Canvas(
                Modifier.fillMaxSize().padding(8.dp).pointerInput(index) {
                    detectDragGestures(
                        onDragStart = { start -> currentStroke = listOf(start) },
                        onDrag = { change, _ ->
                            change.consume()
                            currentStroke = currentStroke + change.position
                        },
                        onDragEnd = {
                            if (currentStroke.isNotEmpty()) strokes.add(currentStroke)
                            currentStroke = emptyList()
                        },
                        onDragCancel = { currentStroke = emptyList() }
                    )
                }
            ) {
                val baselineY = size.height * 0.72f
                drawLine(Color(0xFFB9D7F2), Offset(0f, baselineY), Offset(size.width, baselineY), 3.dp.toPx())
                drawLine(Color(0xFFE0EAF4), Offset(0f, size.height * 0.25f), Offset(size.width, size.height * 0.25f), 2.dp.toPx())
                drawContext.canvas.nativeCanvas.let { canvas ->
                    val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.rgb(215, 226, 238)
                        typeface = android.graphics.Typeface.create("sans-serif-rounded", android.graphics.Typeface.BOLD)
                        textSize = size.height * 0.62f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    val metrics = paint.fontMetrics
                    val baseline = size.height * 0.58f - (metrics.ascent + metrics.descent) / 2f
                    canvas.drawText(letters[index], size.width / 2f, baseline, paint)
                }
                (strokes.toList() + listOfNotNull(currentStroke.takeIf { it.isNotEmpty() })).forEach { points ->
                    if (points.size > 1) {
                        val path = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            points.drop(1).forEach { lineTo(it.x, it.y) }
                        }
                        drawPath(path, Color(0xFF1769AA), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round))
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = ::clearDrawing, modifier = Modifier.weight(1f).height(54.dp)) {
                Text("🧹 Clear", fontSize = 17.sp)
            }
            Button(onClick = {
                if (index < letters.lastIndex) index++ else index = 0
                clearDrawing()
            }, modifier = Modifier.weight(1f).height(54.dp)) {
                Text(if (index == letters.lastIndex) "Start Again →" else "Next Letter →", fontSize = 16.sp)
            }
        }
        Spacer(Modifier.height(6.dp))
        Text("a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z",
            fontSize = 14.sp, color = Color(0xFF60758A))
    }
}
