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

    val letter = bengaliTraceLetters[index]

    fun check() {
        if (points.size < 20) { score = 0; result = false; return }
        val cx = 200f
        val cy = 230f
        val scale = 1.0f
        val matches = points.count { p ->
            val dx = (p.x - cx) / scale
            val dy = (p.y - cy) / scale
            hypot(dx.toDouble(), dy.toDouble()) < 180.0
        }
        score = (matches * 100 / points.size).coerceIn(0,100)
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
                    points.forEach { drawCircle(Color(0xFFB05A00), 7f, it) }
                }
            }

            Spacer(Modifier.height(8.dp))
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
