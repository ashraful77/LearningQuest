package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.GameDataStore
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.*

@Composable
fun AnishHomeScreen(
    scrollState: ScrollState,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val data by store.gameData.collectAsState(initial = null)
    val ownedItems by store.anishOwnedItemIds().collectAsState(initial = emptySet())
    val totalQuestions = data?.anishTotalQuestions ?: 0
    val rewardLevel = when {
        totalQuestions >= 150 -> 3
        totalQuestions >= 50 -> 2
        else -> 1
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF4FF), Color.White, Color(0xFFF3ECFF))))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "📚 Anish Learning Zone",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF315FBA),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text("Class 5 • Bengali Medium", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7043A8))
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { onNavigate("switch_to_arifa") }, modifier = Modifier.weight(1f)) {
                    Text("👧 Arifa", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
                VerticalDivider(modifier = Modifier.height(28.dp), color = Color(0xFFD9E0EA))
                TextButton(onClick = { onNavigate("switch_to_abid") }, modifier = Modifier.weight(1f)) {
                    Text("👦 Abid", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(Color(0xFFFFF4D8)),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🎯", fontSize = 22.sp)
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text("Daily Goal", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9A5A00))
                    Text("Complete 3 questions today", fontSize = 10.sp, color = Color(0xFF60758A))
                }
                Text("3 Q", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("📚 My Learning", modifier = Modifier.fillMaxWidth(), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))
        Text("Choose a subject to learn today", modifier = Modifier.fillMaxWidth(), fontSize = 11.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(6.dp))

        AnishSubjectCard("Bengali", "Lessons, grammar, vocabulary and practice", "📖", Color(0xFFFFEAF4), Color(0xFFB12A73)) { onNavigate("anish_bengali") }
        AnishSubjectCard("Maths", "Arithmetic, fractions, geometry and problem solving", "➗", Color(0xFFEAF2FF), Color(0xFF2457A6)) { onNavigate("anish_math") }
        AnishSubjectCard("Science", "Life, matter, energy, environment and experiments", "🔬", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("anish_science") }
        AnishSubjectCard("History & Geography", "India, the world, maps and important events", "🌍", Color(0xFFFFF1DE), Color(0xFF9A5A00)) { onNavigate("anish_history") }
        AnishSubjectCard("English", "Vocabulary, grammar, reading and practice", "🔤", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("anish_english") }
        AnishSubjectCard("General Knowledge", "Country, state, science and everyday knowledge", "🧠", Color(0xFFFFF8E8), Color(0xFF8B6500)) { onNavigate("anish_gk") }

        Spacer(Modifier.height(6.dp))
        Card(
            onClick = { onNavigate("anish_model_test") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color(0xFFE8F0FF)),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("📝", fontSize = 25.sp)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Model Tests", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    Text("20 mixed questions • 15 minutes", fontSize = 10.sp, color = Color(0xFF60758A))
                }
                Text("›", fontSize = 24.sp, color = Color(0xFF315FBA))
            }
        }

        Spacer(Modifier.height(10.dp))
        Card(
            onClick = { onNavigate("anish_store") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("💎", fontSize = 24.sp)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Progress & Rewards", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    Text("$"+"{data?.diamonds ?: 0} 💎  •  $"+"{ownedItems.size}/5 rewards  •  Level $rewardLevel", fontSize = 10.sp, color = Color(0xFF60758A))
                }
                Text("›", fontSize = 24.sp, color = Color(0xFF315FBA))
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("🌟 Keep learning, Anish!", modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), fontSize = 11.sp, textAlign = TextAlign.Center, color = Color(0xFF7A8798))
    }
}
@Composable
private fun AnishRewardStat(icon: String, value: String, label: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(14.dp), color = Color(0xFFF3F7FF)) {
        Column(Modifier.padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 20.sp)
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))
            Text(label, fontSize = 10.sp, color = Color(0xFF60758A))
        }
    }
}

@Composable
private fun AnishSubjectCard(
    title: String,
    subtitle: String,
    icon: String,
    background: Color,
    accent: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 30.sp)
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = accent)
                Text(subtitle, fontSize = 12.sp, color = Color(0xFF60758A))
            }
        }
    }
}
