package com.ashraful.learningquest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.AbidProgressStore
import kotlinx.coroutines.launch
import kotlin.math.hypot

private val bengaliTraceLetters = listOf("অ","আ","ই","ঈ","উ","এ","ও","ক","খ","গ","ঘ","চ","ছ","জ","ট","ড","ত","দ","ন","প","ফ","ব","ভ","ম","য","র","ল","শ","স","হ")

@Composable
fun AbidBengaliTracingScreen(onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val store = remember(context) { AbidProgressStore(context) }
    val scope = rememberCoroutineScope()
    var index by rememberSaveable { mutableIntStateOf(0) }
    var points by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var strokes by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
    var result by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var rewarded by rememberSaveable { mutableStateOf(false) }
    var brushSizeDp by rememberSaveable { mutableFloatStateOf(8f) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val letter = bengaliTraceLetters[index]

    fun check() {
        if (points.size < 20) { score = 0; result = false; return }
        val width = canvasSize.width.toFloat()
        val height = canvasSize.height.toFloat()
        if (width <= 0f || height <= 0f) { score = 0; result = false; return }

        // Score the actual Bengali glyph instead of a circular area.
        val bitmap = android.graphics.Bitmap.createBitmap(500, 500, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.BLACK
            typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD)
            textSize = 330f
            textAlign = android.graphics.Paint.Align.CENTER
        }
        val metrics = paint.fontMetrics
        val baseline = 250f - (metrics.ascent + metrics.descent) / 2f
        canvas.drawText(letter, 250f, baseline, paint)

        val expected = mutableListOf<Offset>()
        var y = 2
        while (y < 498) {
            var x = 2
            while (x < 498) {
                val pixel = bitmap.getPixel(x, y)
                if (android.graphics.Color.alpha(pixel) > 80 && android.graphics.Color.red(pixel) < 180) {
                    expected += Offset(x.toFloat(), y.toFloat())
                }
                x += 6
            }
            y += 6
        }

        val radius = minOf(width, height) * 0.045f
        val guideCoverage = expected.count { target ->
            val scaled = Offset(target.x / 500f * width, target.y / 500f * height)
            points.any { user ->
                hypot((user.x - scaled.x).toDouble(), (user.y - scaled.y).toDouble()) <= radius
            }
        } * 100 / expected.size.coerceAtLeast(1)

        val userOnGuide = points.count { user ->
            expected.any { target ->
                val scaled = Offset(target.x / 500f * width, target.y / 500f * height)
                hypot((user.x - scaled.x).toDouble(), (user.y - scaled.y).toDouble()) <= radius
            }
        } * 100 / points.size.coerceAtLeast(1)

        score = ((guideCoverage * 0.70f) + (userOnGuide * 0.30f)).toInt().coerceIn(0, 100)
        result = score >= 55
    }

    Box(Modifier.fillMaxSize().background(Color(0xFFFFF8E8)).padding(16.dp)) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) { Text("‹ Home", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.weight(1f))
                Text("✍️ বাংলা লেখা", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
            }
            Text("বর্ণটি অনুসরণ করে লিখি", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("${index + 1} / ${bengaliTraceLetters.size}", color = Color(0xFF60758A))
            Spacer(Modifier.height(8.dp))

            Card(Modifier.fillMaxWidth().weight(1f), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(Color.White)) {
                Canvas(
                    Modifier.fillMaxSize().padding(10.dp)
                        .onSizeChanged { canvasSize = it }
                        .pointerInput(letter) {
                            detectDragGestures(
                                onDragStart = { p ->
                                    points = points + p
                                    strokes = strokes + listOf(listOf(p))
                                    result = null
                                },
                                onDrag = { change, _ ->
                                    points = points + change.position
                                    strokes = strokes.dropLast(1) + listOf(strokes.lastOrNull().orEmpty() + change.position)
                                    result = null
                                }
                            )
                        }
                ) {
                    val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.rgb(225,230,238)
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                        textSize = minOf(size.width, size.height) * .72f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                    val metrics = paint.fontMetrics
                    val baseline = size.height / 2f - (metrics.ascent + metrics.descent) / 2f
                    drawContext.canvas.nativeCanvas.drawText(letter, size.width/2f, baseline, paint)
                    strokes.forEach { stroke ->
                        stroke.zipWithNext().forEach { (a,b) ->
                            drawLine(Color(0xFFB05A00), a, b, 16f, cap = StrokeCap.Round)
                        }
                    }
                    points.forEach { drawCircle(Color(0xFFB05A00), brushSizeDp.dp.toPx() / 2f, it) }
                }
            }

            Spacer(Modifier.height(3.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("পাতলা", fontSize = 11.sp, color = Color(0xFF60758A))
                Slider(
                    value = brushSizeDp,
                    onValueChange = { brushSizeDp = it },
                    valueRange = 3f..12f,
                    steps = 8,
                    modifier = Modifier.weight(1f).padding(horizontal = 5.dp)
                )
                Text("মোটা", fontSize = 11.sp, color = Color(0xFF60758A))
            }
            Spacer(Modifier.height(3.dp))
            Text(
                when (result) {
                    true -> "🎉 দারুণ! ${score}%"
                    false -> "😊 আবার চেষ্টা করি — ${score}%"
                    null -> "হালকা বর্ণটি অনুসরণ করো"
                },
                fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = if (result == true) Color(0xFF237A4B) else Color(0xFF60758A)
            )
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = { points=emptyList(); strokes=emptyList(); result=null; score=0 }, Modifier.weight(1f).height(54.dp)) {
                    Text("মুছে ফেলি", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        if (result == true) {
                            if (index == bengaliTraceLetters.lastIndex) {
                                scope.launch { store.completeActivity("letters", 3, 30) }
                                rewarded = true
                            } else {
                                index++; points=emptyList(); strokes=emptyList(); result=null; score=0
                            }
                        } else check()
                    },
                    Modifier.weight(1f).height(54.dp)
                ) {
                    Text(if (result == true) if (index == bengaliTraceLetters.lastIndex) "🏆 শেষ" else "পরের বর্ণ →" else "✓ পরীক্ষা", fontWeight = FontWeight.Bold)
                }
            }
            if (rewarded) {
                Spacer(Modifier.height(6.dp))
                Text("🏆 বাংলা লেখা অনুশীলন সম্পূর্ণ!", fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
            }
        }
    }
}
