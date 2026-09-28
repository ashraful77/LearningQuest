package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.AbidProgress
import com.ashraful.learningquest.data.AbidProgressStore

private data class AbidBadge(val stars: Int, val emoji: String, val title: String, val message: String)

private val abidBadges = listOf(
    AbidBadge(5, "🌟", "Little Star", "You are learning every day!"),
    AbidBadge(10, "🏅", "Super Learner", "Fantastic learning!"),
    AbidBadge(20, "🏆", "Little Champion", "You worked so hard!"),
    AbidBadge(30, "🚀", "Super Explorer", "Your learning is taking off!")
)

@Composable
fun AbidAchievementsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { AbidProgressStore(context) }
    val progress by store.progress.collectAsState(initial = AbidProgress())
    val scroll = rememberScrollState()

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF7FF), Color.White, Color(0xFFFFF7DF))))
            .verticalScroll(scroll)
            .padding(18.dp)
    ) {
        TextButton(onClick = onBack) { Text("‹ Back", fontWeight = FontWeight.Bold) }
        Text("🏆 My Rewards", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
        Text("Keep learning to collect stars and badges!", fontSize = 15.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(14.dp))

        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("⭐", fontSize = 50.sp)
                Text("${progress.stars}", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
                Text("STARS EARNED", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A8798))
                Spacer(Modifier.height(8.dp))
                Text(
                    when {
                        progress.stars >= 30 -> "🚀 Super Explorer!"
                        progress.stars >= 20 -> "🏆 Little Champion!"
                        progress.stars >= 10 -> "🌟 Super Learner!"
                        progress.stars >= 5 -> "⭐ Little Star!"
                        else -> "🎯 Your first badge is waiting!"
                    },
                    fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF23754A)
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        Text("MY BADGES", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF65738A))

        abidBadges.forEach { badge ->
            val unlocked = progress.stars >= badge.stars
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(if (unlocked) Color.White else Color(0xFFF1F3F6)),
                elevation = CardDefaults.cardElevation(if (unlocked) 3.dp else 1.dp)) {
                Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(if (unlocked) badge.emoji else "🔒", fontSize = 38.sp)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(badge.title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold,
                            color = if (unlocked) Color(0xFF1769AA) else Color(0xFF7A8798))
                        Text(if (unlocked) badge.message else "Unlock at ${badge.stars} stars",
                            fontSize = 12.sp, color = Color(0xFF60758A))
                    }
                    if (unlocked) Text("✓", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF23754A))
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color(0xFFFFF1D6))) {
            Text("🌈 Keep learning, Abid! Every activity brings you closer to your next reward.",
                Modifier.padding(16.dp), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5B00))
        }
        Spacer(Modifier.height(20.dp))
    }
}
