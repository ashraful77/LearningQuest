package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileSelectionScreen(onSelected: (String) -> Unit) {
    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                listOf(Color(0xFFFFEAF4), Color(0xFFEAF4FF), Color(0xFFFFF5D9))
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🌈 LearningQuest", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF5636A8))
            Spacer(Modifier.height(12.dp))
            Text("Who are you?", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Color(0xFF26354A))
            Spacer(Modifier.height(30.dp))
            ProfileButton("👦", "Abid", Color(0xFFE8F4FF), Color(0xFF1769AA)) { onSelected("abid") }
            Spacer(Modifier.height(14.dp))
            ProfileButton("👧", "Arifa", Color(0xFFFFEAF4), Color(0xFFB12A73)) { onSelected("arifa") }
        }
    }
}

@Composable
private fun ProfileButton(
    emoji: String,
    name: String,
    background: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(82.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = background,
            contentColor = textColor
        )
    ) {
        Text("$emoji  $name", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
    }
}
