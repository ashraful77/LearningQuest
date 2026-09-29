package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
fun AnishHomeScreen(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val data by store.gameData.collectAsState(initial = null)
    val ownedItems by store.anishOwnedItemIds().collectAsState(initial = emptySet())

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFEAF4FF), Color.White, Color(0xFFF3ECFF))
                )
            )
            .padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "📚 Anish Learning Zone",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF315FBA),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(5.dp))
        Text(
            "Class 5 • Bengali Medium",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7043A8)
        )
        Text(
            "Learn • Practise • Test • Grow 🚀",
            fontSize = 13.sp,
            color = Color(0xFF60758A),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(14.dp))

        OutlinedButton(
            onClick = { onNavigate("switch_to_arifa") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("👧 Switch to Arifa", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onNavigate("switch_to_abid") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("👦 Switch to Abid", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(12.dp))

        Card(onClick = { onNavigate("anish_store") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F2FF))) {
            Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("💎", fontSize = 30.sp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Diamond Store", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    Text("Solve questions, earn diamonds and buy rewards", fontSize = 12.sp, color = Color(0xFF60758A))
                }
                Text("›", fontSize = 28.sp, color = Color(0xFF315FBA))
            }
        }

        Card(
            onClick = { onNavigate("anish_store") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("🏆", fontSize = 30.sp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("My Rewards", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                        Text("Diamonds • Collection • Milestones", fontSize = 12.sp, color = Color(0xFF60758A))
                    }
                    Text("›", fontSize = 28.sp, color = Color(0xFF315FBA))
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AnishRewardStat("💎", (data?.diamonds ?: 0).toString(), "Diamonds", Modifier.weight(1f))
                    AnishRewardStat("🏆", ownedItems.size.toString() + "/5", "Collection", Modifier.weight(1f))
                    AnishRewardStat("⭐", if ((data?.diamonds ?: 0) >= 250) "MAX" else "চলছে", "Reward", Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        val totalQuestions = data?.anishTotalQuestions ?: 0
        val rewardLevel = when {
            totalQuestions >= 150 -> 3
            totalQuestions >= 50 -> 2
            else -> 1
        }
        val levelTitle = when (rewardLevel) {
            3 -> "LEVEL 3 • PET COMPANION"
            2 -> "LEVEL 2 • TROPHY COLLECTION"
            else -> "LEVEL 1 • REWARD COLLECTION"
        }
        val levelIcon = when (rewardLevel) {
            3 -> "🐾"
            2 -> "🏆"
            else -> "🎁"
        }
        val levelDescription = when (rewardLevel) {
            3 -> "150+ questions • Pet companion unlocked!"
            2 -> "৫০+ প্রশ্ন • 3D-style ট্রফি Collection করো!"
            else -> "৫০ প্রশ্নের আগে • ব্যাজ ও ছোট পুরস্কার Collection করো"
        }

        Card(
            onClick = { onNavigate("anish_rewards") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (rewardLevel) {
                    3 -> Color(0xFFEAF8EF)
                    2 -> Color(0xFFFFF4D8)
                    else -> Color(0xFFF1F5FF)
                }
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            val transition = rememberInfiniteTransition(label = "anish_reward")
            val pulse by transition.animateFloat(
                initialValue = 0.94f,
                targetValue = 1.06f,
                animationSpec = infiniteRepeatable(tween(850), RepeatMode.Reverse),
                label = "rewardPulse"
            )
            val rotation by transition.animateFloat(
                initialValue = -6f,
                targetValue = 6f,
                animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                label = "rewardRotation"
            )
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(levelIcon, fontSize = 48.sp, modifier = Modifier.graphicsLayer {
                    scaleX = pulse
                    scaleY = pulse
                    rotationY = if (rewardLevel >= 2) rotation * 3f else rotation
                })
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(levelTitle, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    Text(levelDescription, fontSize = 12.sp, color = Color(0xFF60758A))
                    Spacer(Modifier.height(5.dp))
                    Text("Progress: $totalQuestions / ${if (rewardLevel == 1) 50 else 150} questions", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
                    if (rewardLevel < 3) {
                        val next = if (rewardLevel == 1) 50 else 150
                        LinearProgressIndicator(progress = { (totalQuestions.toFloat() / next).coerceIn(0f, 1f) }, Modifier.fillMaxWidth().padding(top = 6.dp).height(6.dp), color = Color(0xFF315FBA), trackColor = Color.White.copy(alpha = 0.7f))
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    "🏆 My Milestones",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF7043A8)
                )
                Text(
                    "Complete more questions to unlock new badges",
                    fontSize = 12.sp,
                    color = Color(0xFF60758A)
                )
                Spacer(Modifier.height(10.dp))
                val milestones = listOf(
                    5 to "🥉 ব্রোঞ্জ",
                    20 to "🥈 সিলভার",
                    50 to "🥇 গোল্ড",
                    100 to "🏆 মাস্টার"
                )
                milestones.forEach { (target, title) ->
                    val unlocked = target in (data?.anishAchievements ?: emptySet())
                    val current = data?.anishTotalQuestions ?: 0
                    val progress = minOf(current, target)
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            if (unlocked) "✅" else "🔒",
                            fontSize = 20.sp
                        )
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                "$progress / $target প্রশ্ন",
                                fontSize = 11.sp,
                                color = Color(0xFF60758A)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        Text(
            "📖 My Subjects",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF26354A)
        )

        Spacer(Modifier.height(8.dp))

        AnishSubjectCard("Bengali", "Lessons, grammar, vocabulary and practice", "📖", Color(0xFFFFEAF4), Color(0xFFB12A73)) { onNavigate("anish_bengali") }
        AnishSubjectCard("Maths", "Arithmetic, fractions, geometry and problem solving", "➗", Color(0xFFEAF2FF), Color(0xFF2457A6)) { onNavigate("anish_math") }
        AnishSubjectCard("Science", "Life, matter, energy, environment and experiments", "🔬", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("anish_science") }
        AnishSubjectCard("History & Geography", "India, the world, maps and important events", "🌍", Color(0xFFFFF1DE), Color(0xFF9A5A00)) { onNavigate("anish_history") }
        AnishSubjectCard("English", "Vocabulary, grammar, reading and practice", "🔤", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("anish_english") }
        AnishSubjectCard("General Knowledge", "Country, state, science and everyday knowledge", "🧠", Color(0xFFFFF8E8), Color(0xFF8B6500)) { onNavigate("anish_gk") }

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FF)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(17.dp)) {
                Text(
                    "📝 Model Tests",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF315FBA)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Subject-wise and mixed tests will be added here.",
                    fontSize = 13.sp,
                    color = Color(0xFF60758A)
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        Text(
            "More learning materials for Anish will be added step by step. 🌟",
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF7A8798)
        )
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
