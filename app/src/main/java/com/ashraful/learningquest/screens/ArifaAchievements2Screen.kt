package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.GameDataStore

@Composable
fun ArifaAchievements2Screen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val data by store.gameData.collectAsState(initial = null)

    val d = data
    val achievements = if (d == null) emptyList() else listOf(
        "🌟 First 50 XP" to (d.xp >= 50),
        "🔥 3-Day Streak" to (d.streak >= 3),
        "🪙 Coin Collector" to (d.coins >= 100),
        "➗ Maths Star" to (d.mathScore >= 10),
        "🔤 English Star" to (d.englishScore >= 10),
        "🔬 Science Star" to (d.scienceScore >= 10),
        "🧩 Puzzle Star" to (d.puzzleScore >= 10),
        "🚀 Level 5" to (d.level >= 5),
        "📚 100 Questions" to (d.totalQuestions >= 100),
        "🎯 90% Accuracy" to (d.totalQuestions >= 20 && d.correctAnswers * 100 >= d.totalQuestions * 90)
    )

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF4F7FF), Color.White, Color(0xFFFFF8E8))))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onBack, shape = RoundedCornerShape(14.dp)) { Text("‹ Back") }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("🏆 ACHIEVEMENTS", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                Text("${d?.achievementCount ?: 0} / ${achievements.size} unlocked", fontSize = 12.sp, color = Color(0xFF71809A))
            }
        }
        Spacer(Modifier.height(14.dp))

        achievements.forEach { (title, unlocked) ->
            Card(
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(if (unlocked) Color(0xFFEAF8EF) else Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(if (unlocked) "🏆" else "🔒", fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                        Text(if (unlocked) "Unlocked! Keep learning." else "Keep learning to unlock this.", fontSize = 11.sp, color = Color(0xFF71809A))
                    }
                    Text(if (unlocked) "✓" else "•", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold,
                        color = if (unlocked) Color(0xFF20A65A) else Color(0xFF9AA6B5))
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("💡 Achievements are earned automatically from learning progress.", fontSize = 12.sp, color = Color(0xFF71809A))
    }
}
