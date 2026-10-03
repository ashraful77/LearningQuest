package com.ashraful.learningquest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbidNumberWritingScreen(onBack: () -> Unit) {
    var number by remember { mutableIntStateOf(1) }
    var strokes by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }

    Column(
        Modifier.fillMaxSize().background(Color(0xFFEAF4FF))
            .padding(16.dp).navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Home", fontSize = 18.sp) }
            Spacer(Modifier.weight(1f))
            Text("✍️ Number Writing", fontSize = 23.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                color = Color(0xFF1769AA))
            Spacer(Modifier.weight(1f))
        }

        Text("Trace the number", fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Card(
            Modifier.fillMaxWidth().weight(1f).padding(vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Box(
                Modifier.fillMaxSize().padding(18.dp)
                    .border(3.dp, Color(0xFF9CC8F0), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    Modifier.fillMaxSize()
                        .pointerInput(number) {
                            detectDragGestures(
                                onDragStart = { point ->
                                    strokes = strokes + listOf(listOf(point))
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    val current = strokes.lastOrNull().orEmpty()
                                    strokes = if (strokes.isEmpty()) {
                                        listOf(listOf(change.position))
                                    } else {
                                        strokes.dropLast(1) + listOf(current + change.position)
                                    }
                                }
                            )
                        }
                ) {
                    drawContext.canvas.nativeCanvas.let { canvas ->
                        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                            color = android.graphics.Color.rgb(185, 215, 242)
                            typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD)
                            textSize = 190.dp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                        val metrics = paint.fontMetrics
                        val baseline = size.height / 2f - (metrics.ascent + metrics.descent) / 2f
                        canvas.drawText(number.toString(), size.width / 2f, baseline, paint)
                    }

                    strokes.forEach { stroke ->
                        if (stroke.size == 1) {
                            drawCircle(Color(0xFF1769AA), 8.dp.toPx(), stroke[0])
                        } else {
                            stroke.zipWithNext().forEach { (a, b) ->
                                drawLine(Color(0xFF1769AA), a, b, 12.dp.toPx(), cap = StrokeCap.Round)
                            }
                        }
                    }

                    drawContext.canvas.nativeCanvas.let { canvas ->
                        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                            color = android.graphics.Color.rgb(96, 117, 138)
                            textSize = 14.dp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                        canvas.drawText("Trace over the number with your finger", size.width / 2f, size.height - 20.dp.toPx(), paint)
                    }
                }
            }
        }

        Text(
            if (strokes.isNotEmpty()) "✨ Great! Keep tracing!" else "Start writing with your finger",
            fontSize = 17.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = Color(0xFF1769AA)
        )

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = { strokes = emptyList() },
                modifier = Modifier.weight(1f).height(52.dp)
            ) { Text("Clear", fontSize = 17.sp) }

            Button(
                onClick = {
                    number = if (number == 9) 1 else number + 1
                    strokes = emptyList()
                },
                modifier = Modifier.weight(1f).height(52.dp)
            ) { Text("Next →", fontSize = 17.sp) }
        }
    }
}
