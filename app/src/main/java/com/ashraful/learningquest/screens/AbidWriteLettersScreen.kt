package com.ashraful.learningquest.screens

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.hypot

private fun makeGuideBitmap(letter: Char): Bitmap {
    val bitmap = Bitmap.createBitmap(400, 500, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.BLACK
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        textSize = 360f
        textAlign = Paint.Align.CENTER
    }
    val metrics = paint.fontMetrics
    val baseline = 250f - (metrics.ascent + metrics.descent) / 2f
    canvas.drawText(letter.toString(), 200f, baseline, paint)
    return bitmap
}

private fun guidePoints(bitmap: Bitmap, step: Int = 5): List<Offset> {
    val points = mutableListOf<Offset>()
    var y = 2
    while (y < bitmap.height - 2) {
        var x = 2
        while (x < bitmap.width - 2) {
            val p = bitmap.getPixel(x, y)
            if (android.graphics.Color.alpha(p) > 80 &&
                android.graphics.Color.red(p) < 180
            ) points += Offset(x.toFloat(), y.toFloat())
            x += step
        }
        y += step
    }
    return points
}

@Composable
fun AbidWriteLettersScreen(onBack: () -> Unit) {
    var started by remember { mutableStateOf(false) }
    var startIndex by remember { mutableIntStateOf(0) }
    var index by remember { mutableIntStateOf(0) }
    var userPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var strokes by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
    var result by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var brushSizeDp by remember { mutableFloatStateOf(7f) }

    val letter = ('A'.code + index).toChar()
    val guide = remember(letter) { makeGuideBitmap(letter) }
    val expectedPoints = remember(letter) { guidePoints(guide) }

    fun clearWriting() {
        userPoints = emptyList()
        strokes = emptyList()
        result = null
        score = 0
    }

    fun checkLetter(width: Float, height: Float) {
        if (userPoints.size < 20 || width <= 0f || height <= 0f) {
            score = 0
            result = false
            return
        }
        val radius = minOf(width, height) * 0.045f + brushSizeDp * 1.2f
        val guideCoverage = expectedPoints.count { point ->
            val target = Offset(point.x / guide.width * width, point.y / guide.height * height)
            userPoints.any {
                hypot((it.x - target.x).toDouble(), (it.y - target.y).toDouble()) <= radius
            }
        } * 100 / expectedPoints.size.coerceAtLeast(1)
        val userOnGuide = userPoints.count { user ->
            expectedPoints.any { point ->
                val target = Offset(point.x / guide.width * width, point.y / guide.height * height)
                hypot((user.x - target.x).toDouble(), (user.y - target.y).toDouble()) <= radius
            }
        } * 100 / userPoints.size.coerceAtLeast(1)
        score = ((guideCoverage * 0.70f) + (userOnGuide * 0.30f)).toInt().coerceIn(0, 100)
        result = score >= 55
    }

    Box(
        Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF7FF), Color.White, Color(0xFFFFF1D6))))
    ) {
        if (!started) {
            Column(
                Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(onClick = onBack, Modifier.align(Alignment.Start)) {
                    Text("‹ Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                Text("✍️ Write & Trace", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
                Text("Choose a letter to start", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
                Spacer(Modifier.height(22.dp))
                Text("Choose Letter", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
                Spacer(Modifier.height(14.dp))
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    ('A'..'Z').chunked(6).forEach { row ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { ch ->
                                Button(
                                    onClick = {
                                        startIndex = ch.code - 'A'.code
                                        index = startIndex
                                        started = true
                                        clearWriting()
                                    },
                                    modifier = Modifier.weight(1f).height(54.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(ch.toString(), fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                            repeat(6 - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
                Text(
                    "After choosing a letter, use Previous and Next to practice every letter in order. No letters can be skipped.",
                    fontSize = 14.sp, color = Color(0xFF60758A), textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { started = false; clearWriting() }) {
                        Text("‹ Choose Letter", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        "Letter ${index - startIndex + 1} of ${26 - startIndex}",
                        fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A)
                    )
                }
                Text("✍️ Write & Trace", fontSize = 29.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
                Text("Trace the letter with your finger", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
                Text(letter.toString(), fontSize = 58.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))

                Spacer(Modifier.height(8.dp))
                Card(
                    Modifier.fillMaxWidth().height(420.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    Canvas(
                        Modifier.fillMaxSize().padding(10.dp)
                            .onSizeChanged { canvasSize = it }
                            .pointerInput(letter, brushSizeDp) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        userPoints = userPoints + offset
                                        strokes = strokes + listOf(listOf(offset))
                                        result = null
                                    },
                                    onDrag = { change, _ ->
                                        userPoints = userPoints + change.position
                                        strokes = strokes.dropLast(1) + listOf(strokes.lastOrNull().orEmpty() + change.position)
                                        result = null
                                    }
                                )
                            }
                    ) {
                        val guidePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = android.graphics.Color.rgb(224, 234, 242)
                            typeface = Typeface.create("sans-serif", Typeface.BOLD)
                            textSize = 360f * minOf(size.width / 400f, size.height / 500f)
                            textAlign = Paint.Align.CENTER
                        }
                        val metrics = guidePaint.fontMetrics
                        val baseline = size.height / 2f - (metrics.ascent + metrics.descent) / 2f
                        drawContext.canvas.nativeCanvas.drawText(letter.toString(), size.width / 2f, baseline, guidePaint)
                        val brushPx = brushSizeDp.dp.toPx()
                        strokes.forEach { stroke ->
                            stroke.zipWithNext().forEach { (a, b) ->
                                drawLine(Color(0xFF1769AA), a, b, brushPx, cap = StrokeCap.Round)
                            }
                        }
                        userPoints.forEach { drawCircle(Color(0xFF1769AA), brushPx / 2f, it) }
                    }
                }

                Spacer(Modifier.height(5.dp))
                Text("🖌️ Brush Size", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Thin", fontSize = 12.sp, color = Color(0xFF60758A))
                    Slider(
                        value = brushSizeDp,
                        onValueChange = { brushSizeDp = it },
                        valueRange = 3f..12f,
                        steps = 8,
                        modifier = Modifier.weight(1f).padding(horizontal = 6.dp)
                    )
                    Text("Thick", fontSize = 12.sp, color = Color(0xFF60758A))
                }

                when (result) {
                    true -> Text("🎉 Great tracing! $score%", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF159447))
                    false -> Text("😊 Almost there! $score%", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD52E45))
                    null -> Text("Follow the light letter from top to bottom", fontSize = 14.sp, color = Color(0xFF65738A))
                }

                Spacer(Modifier.height(5.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { clearWriting() }, modifier = Modifier.weight(1f).height(52.dp)) {
                        Text("🧹 Clear", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { checkLetter(canvasSize.width.toFloat(), canvasSize.height.toFloat()) },
                        modifier = Modifier.weight(1f).height(52.dp)
                    ) { Text("✓ Check", fontSize = 17.sp, fontWeight = FontWeight.Bold) }
                }

                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { if (index > startIndex) { index--; clearWriting() } },
                        enabled = index > startIndex,
                        modifier = Modifier.weight(1f).height(50.dp)
                    ) { Text("← Previous", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
                    Button(
                        onClick = {
                            if (index < 25) {
                                index++
                                clearWriting()
                            } else {
                                started = false
                                clearWriting()
                            }
                        },
                        modifier = Modifier.weight(1f).height(50.dp)
                    ) {
                        Text(if (index == 25) "✓ Complete" else "Next →", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(5.dp))
            }
        }

        AnimatedVisibility(visible = started && result == true, modifier = Modifier.align(Alignment.Center)) {
            val scale by animateFloatAsState(if (result == true) 1f else 0.8f, label = "successScale")
            Card(
                Modifier.fillMaxWidth(0.86f).graphicsLayer(scaleX = scale, scaleY = scale),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉", fontSize = 54.sp)
                    Text("Great Job!", fontSize = 31.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF159447))
                    Text("You wrote $letter!", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1769AA))
                    Text("$score% match", fontSize = 17.sp, color = Color(0xFF60758A))
                }
            }
        }
    }
}
