package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbidNumberWritingScreen(onBack: () -> Unit) {
    var number by remember { mutableIntStateOf(1) }
    var strokes by remember { mutableStateOf(0) }

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
                    .border(3.dp, Color(0xFF9CC8F0), RoundedCornerShape(20.dp))
                    .pointerInput(number) {
                        detectDragGestures(
                            onDragStart = { strokes++ },
                            onDrag = { change, _ -> change.consume() }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$number",
                    fontSize = 190.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                    color = Color(0xFFB9D7F2)
                )
                Text(
                    "Trace over the number with your finger",
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
                    fontSize = 14.sp,
                    color = Color(0xFF60758A)
                )
            }
        }

        Text(
            if (strokes > 0) "✨ Great! Keep tracing!" else "Start writing with your finger",
            fontSize = 17.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = Color(0xFF1769AA)
        )

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = { strokes = 0 },
                modifier = Modifier.weight(1f).height(52.dp)
            ) { Text("Clear", fontSize = 17.sp) }

            Button(
                onClick = {
                    number = if (number == 9) 1 else number + 1
                    strokes = 0
                },
                modifier = Modifier.weight(1f).height(52.dp)
            ) { Text("Next →", fontSize = 17.sp) }
        }
    }
}
