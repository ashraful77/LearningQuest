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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
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
    var y = 0
    while (y < bitmap.height) {
        var x = 0
        while (x < bitmap.width) {
            val pixel = bitmap.getPixel(x, y)
            if (android.graphics.Color.alpha(pixel) > 80 &&
                android.graphics.Color.red(pixel) < 180) {
                points += Offset(x.toFloat(), y.toFloat())
            }
            x += step
        }
        y += step
    }
    return points
}

@Composable
fun AbidWriteLettersScreen(onBack: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }
    var userPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var strokes by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
    var result by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableIntStateOf(0) }

    val letter = ('A'.code + index).toChar()
    val guide = remember(letter) { makeGuideBitmap(letter) }
    val expectedPoints = remember(letter) { guidePoints(guide) }

    fun checkLetter(width: Float, height: Float) {
        if (userPoints.size < 15) {
            score = 0
            result = false
            return
        }

        val radius = minOf(width, height) * 0.055f
        val covered = expectedPoints.count { point ->
            val target = Offset(
                point.x / guide.width * width,
                point.y / guide.height * height
            )
            userPoints.any { user ->
                hypot(
                    (user.x - target.x).toDouble(),
                    (user.y - target.y).toDouble()
                ) <= radius
            }
        }

        score = (covered * 100 / expectedPoints.size.coerceAtLeast(1)).coerceIn(0, 100)
        result = score >= 80
    }

    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFEAF7FF), Color.White, Color(0xFFFFF1D6))
                )
            )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth().padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text("‹ Home", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    "✍️ Write Letters",
                    fontSize = 29.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1769AA)
                )
            }

            Text(
                "Write this letter",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF60758A)
            )
            Text(
                letter.toString(),
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1769AA)
            )
            Text(
                "Trace the light guide with your finger",
                fontSize = 15.sp,
                color = Color(0xFF7B8797)
            )

            Spacer(Modifier.height(8.dp))

            Card(
                Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Canvas(
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .pointerInput(letter) {
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
                        color = android.graphics.Color.rgb(220, 229, 238)
                        typeface = Typeface.create("sans-serif", Typeface.BOLD)
                        textSize = 360f * minOf(size.width / 400f, size.height / 500f)
                        textAlign = Paint.Align.CENTER
                    }
                    val metrics = guidePaint.fontMetrics
                    val baseline = size.height / 2f - (metrics.ascent + metrics.descent) / 2f
                    drawContext.canvas.nativeCanvas.drawText(
                        letter.toString(),
                        size.width / 2f,
                        baseline,
                        guidePaint
                    )

                    strokes.forEach { stroke ->
                        stroke.zipWithNext().forEach { (a, b) ->
                            drawLine(
                                Color(0xFF1769AA),
                                a,
                                b,
                                strokeWidth = 15f,
                                cap = StrokeCap.Round
                            )
                        }
                    }

                    userPoints.forEach {
                        drawCircle(Color(0xFF1769AA), 7f, it)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            when (result) {
                true -> Text(
                    "🎉 Excellent! $letter • $score%",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF159447)
                )
                false -> Text(
                    "😊 Try again! $score%",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD52E45)
                )
                null -> Text(
                    "Cover about 80% of the letter to pass",
                    fontSize = 15.sp,
                    color = Color(0xFF65738A)
                )
            }

            Spacer(Modifier.height(6.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        userPoints = emptyList()
                        strokes = emptyList()
                        result = null
                        score = 0
                    },
                    modifier = Modifier.weight(1f).height(54.dp)
                ) {
                    Text("Clear", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                if (result == true) {
                    Button(
                        onClick = {
                            index = (index + 1) % 26
                            userPoints = emptyList()
                            strokes = emptyList()
                            result = null
                            score = 0
                        },
                        modifier = Modifier.weight(1f).height(54.dp)
                    ) {
                        Text(
                            if (index == 25) "🔄 Again" else "Next →",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            checkLetter(guide.width.toFloat(), guide.height.toFloat())
                        },
                        modifier = Modifier.weight(1f).height(54.dp)
                    ) {
                        Text("✓ Check", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        AnimatedVisibility(
            visible = result == true,
            modifier = Modifier.align(Alignment.Center)
        ) {
            val scale by animateFloatAsState(if (result == true) 1f else 0.8f, label = "successScale")
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .graphicsLayer(scaleX = scale, scaleY = scale),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉", fontSize = 58.sp)
                    Text(
                        "Great Job!",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF159447)
                    )
                    Text(
                        "You wrote $letter!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1769AA)
                    )
                    Text(
                        "$score% match",
                        fontSize = 18.sp,
                        color = Color(0xFF60758A)
                    )
                }
            }
        }
    }
}
