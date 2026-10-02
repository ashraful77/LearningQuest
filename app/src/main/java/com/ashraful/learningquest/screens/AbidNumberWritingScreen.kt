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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.ashraful.learningquest.data.AbidProgressStore
import kotlinx.coroutines.launch
import kotlin.math.hypot

private fun makeNumberGuide(number: Int): Bitmap {
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
    canvas.drawText(number.toString(), 200f, baseline, paint)
    return bitmap
}

private fun numberGuidePoints(bitmap: Bitmap, step: Int = 5): List<Offset> {
    val points = mutableListOf<Offset>()
    var y = 2
    while (y < bitmap.height - 2) {
        var x = 2
        while (x < bitmap.width - 2) {
            val p = bitmap.getPixel(x, y)
            if (android.graphics.Color.alpha(p) > 80 && android.graphics.Color.red(p) < 180) {
                points += Offset(x.toFloat(), y.toFloat())
            }
            x += step
        }
        y += step
    }
    return points
}

@Composable
fun AbidNumberWritingScreen(onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val store = remember(context) { AbidProgressStore(context) }
    val scope = rememberCoroutineScope()

    var started by rememberSaveable { mutableStateOf(false) }
    var startNumber by rememberSaveable { mutableIntStateOf(0) }
    var number by rememberSaveable { mutableIntStateOf(0) }
    var points by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var strokes by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
    var result by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var brushSizeDp by rememberSaveable { mutableFloatStateOf(7f) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var completed by rememberSaveable { mutableStateOf(false) }

    val guide = remember(number) { makeNumberGuide(number) }
    val expected = remember(number) { numberGuidePoints(guide) }

    fun clearWriting() {
        points = emptyList()
        strokes = emptyList()
        result = null
        score = 0
    }

    fun checkWriting() {
        val width = canvasSize.width.toFloat()
        val height = canvasSize.height.toFloat()
        if (points.size < 20 || width <= 0f || height <= 0f) {
            score = 0
            result = false
            return
        }
        val radius = minOf(width, height) * 0.045f + brushSizeDp * 1.2f
        val coverage = expected.count { p ->
            val target = Offset(p.x / guide.width * width, p.y / guide.height * height)
            points.any { u -> hypot((u.x-target.x).toDouble(), (u.y-target.y).toDouble()) <= radius }
        } * 100 / expected.size.coerceAtLeast(1)
        val onGuide = points.count { u ->
            expected.any { p ->
                val target = Offset(p.x / guide.width * width, p.y / guide.height * height)
                hypot((u.x-target.x).toDouble(), (u.y-target.y).toDouble()) <= radius
            }
        } * 100 / points.size.coerceAtLeast(1)
        score = ((coverage * 0.70f) + (onGuide * 0.30f)).toInt().coerceIn(0, 100)
        result = score >= 55
    }

    Box(
        Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF7FF), Color.White, Color(0xFFFFF1D6))))
    ) {
        if (!started) {
            Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                TextButton(onClick = onBack, Modifier.align(Alignment.Start)) {
                    Text("‹ Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text("🔢 Number Writing", fontSize = 29.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
                Text("Choose a number to start", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
                Spacer(Modifier.height(22.dp))
                Text("Choose Number", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
                Spacer(Modifier.height(14.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (0..9).forEach { n ->
                        Button(
                            onClick = { startNumber=n; number=n; started=true; completed=false; clearWriting() },
                            modifier = Modifier.weight(1f).height(58.dp),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) { Text(n.toString(), fontSize=23.sp, fontWeight=FontWeight.ExtraBold) }
                    }
                }
                Spacer(Modifier.height(18.dp))
                Text("After choosing a number, practice the following numbers in order. No numbers can be skipped.", fontSize=14.sp, color=Color(0xFF60758A), textAlign=TextAlign.Center)
            }
        } else {
            Column(Modifier.fillMaxSize().padding(horizontal=16.dp), horizontalAlignment=Alignment.CenterHorizontally) {
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically) {
                    TextButton(onClick={started=false; clearWriting()}) { Text("‹ Choose Number", fontSize=16.sp, fontWeight=FontWeight.Bold) }
                    Spacer(Modifier.weight(1f))
                    Text("Number ${number-startNumber+1} of ${10-startNumber}", fontSize=14.sp, fontWeight=FontWeight.Bold, color=Color(0xFF60758A))
                }
                Text("🔢 Number Writing", fontSize=27.sp, fontWeight=FontWeight.ExtraBold, color=Color(0xFF1769AA))
                Text("Trace the number with your finger", fontSize=17.sp, fontWeight=FontWeight.Bold, color=Color(0xFF60758A))
                Text(number.toString(), fontSize=58.sp, fontWeight=FontWeight.ExtraBold, color=Color(0xFF1769AA))
                Spacer(Modifier.height(8.dp))
                Card(Modifier.fillMaxWidth().height(420.dp), shape=RoundedCornerShape(28.dp), colors=CardDefaults.cardColors(Color.White), elevation=CardDefaults.cardElevation(5.dp)) {
                    Canvas(Modifier.fillMaxSize().padding(10.dp).onSizeChanged{canvasSize=it}.pointerInput(number,brushSizeDp){
                        detectDragGestures(
                            onDragStart={p->points=points+p; strokes=strokes+listOf(listOf(p)); result=null},
                            onDrag={change,_->points=points+change.position; strokes=strokes.dropLast(1)+listOf(strokes.lastOrNull().orEmpty()+change.position); result=null}
                        )
                    }){
                        val paint=Paint(Paint.ANTI_ALIAS_FLAG).apply{color=android.graphics.Color.rgb(224,234,242);typeface=Typeface.create("sans-serif",Typeface.BOLD);textSize=360f*minOf(size.width/400f,size.height/500f);textAlign=Paint.Align.CENTER}
                        val metrics=paint.fontMetrics
                        val baseline=size.height/2f-(metrics.ascent+metrics.descent)/2f
                        drawContext.canvas.nativeCanvas.drawText(number.toString(),size.width/2f,baseline,paint)
                        val brushPx=brushSizeDp.dp.toPx()
                        strokes.forEach{stroke->stroke.zipWithNext().forEach{(a,b)->drawLine(Color(0xFF1769AA),a,b,brushPx,cap=StrokeCap.Round)}}
                        points.forEach{drawCircle(Color(0xFF1769AA),brushPx/2f,it)}
                    }
                }
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){
                    Text("Thin",fontSize=11.sp,color=Color(0xFF60758A))
                    Slider(value=brushSizeDp,onValueChange={brushSizeDp=it},valueRange=3f..12f,steps=8,modifier=Modifier.weight(1f).padding(horizontal=6.dp))
                    Text("Thick",fontSize=11.sp,color=Color(0xFF60758A))
                }
                Text(when(result){true->"🎉 Great! $score%";false->"😊 Try again — $score%";null->"Follow the light number"},fontSize=18.sp,fontWeight=FontWeight.Bold,color=if(result==true)Color(0xFF159447)else Color(0xFF60758A))
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
                    OutlinedButton(onClick={clearWriting()},modifier=Modifier.weight(1f).height(52.dp)){Text("🧹 Clear",fontWeight=FontWeight.Bold)}
                    Button(onClick={if(result==true){if(number==9){scope.launch{store.completeActivity("numbers",3,30)};completed=true}else{number++;clearWriting()}}else checkWriting()},modifier=Modifier.weight(1f).height(52.dp)){Text(if(result==true)if(number==9)"🏆 Finish" else "Next →" else "✓ Check",fontWeight=FontWeight.Bold)}
                }
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){
                    OutlinedButton(onClick={if(number>startNumber){number--;clearWriting()}},enabled=number>startNumber,modifier=Modifier.weight(1f).height(48.dp)){Text("← Previous",fontSize=15.sp,fontWeight=FontWeight.Bold)}
                    Button(onClick={if(number<9){number++;clearWriting()}else{started=false;clearWriting()}},modifier=Modifier.weight(1f).height(48.dp)){Text(if(number==9)"✓ Complete" else "Next →",fontSize=15.sp,fontWeight=FontWeight.Bold)}
                }
            }
        }
        AnimatedVisibility(visible=started&&result==true,modifier=Modifier.align(Alignment.Center)){
            val scale by animateFloatAsState(if(result==true)1f else .8f,label="numberSuccess")
            Card(Modifier.fillMaxWidth(.86f).graphicsLayer(scaleX=scale,scaleY=scale),shape=RoundedCornerShape(28.dp),colors=CardDefaults.cardColors(Color.White),elevation=CardDefaults.cardElevation(12.dp)){
                Column(Modifier.padding(24.dp),horizontalAlignment=Alignment.CenterHorizontally){
                    Text("🎉",fontSize=54.sp);Text("Great Job!",fontSize=30.sp,fontWeight=FontWeight.ExtraBold,color=Color(0xFF159447));Text("You wrote $number!",fontSize=21.sp,fontWeight=FontWeight.Bold,color=Color(0xFF1769AA));Text("$score% match",fontSize=17.sp,color=Color(0xFF60758A))
                }
            }
        }
    }
}
