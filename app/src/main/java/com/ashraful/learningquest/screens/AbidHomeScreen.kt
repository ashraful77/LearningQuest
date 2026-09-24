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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbidHomeScreen(onNavigate: (String) -> Unit) {
    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF7FF), Color.White, Color(0xFFFFF7DF))))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Text("👦 Hi Abid!", fontSize = 34.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF1769AA))
        Text("Let's learn and play! 🚀", fontSize = 17.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(28.dp))

        LearningCard("🔤", "Letters", "Learn A, B, C and more!", Color(0xFFE8F4FF), Color(0xFF1769AA)) {
            onNavigate("abid_letters")
        }
        LearningCard("🔢", "Numbers", "Learn numbers and counting!", Color(0xFFFFF1D6), Color(0xFF9A5A00)) {
            onNavigate("abid_numbers")
        }
        LearningCard("🔗", "Match Letters", "Match small letters with CAPITAL letters!", Color(0xFFF3ECFF), Color(0xFF7043A8)) {
            onNavigate("abid_match_letters")
        }
    }
}

@Composable
private fun LearningCard(
    icon: String,
    title: String,
    subtitle: String,
    background: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 42.sp)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 25.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = textColor)
                Text(subtitle, fontSize = 14.sp, color = Color(0xFF60758A))
            }
            Text("›", fontSize = 34.sp, color = textColor)
        }
    }
}
