package com.ashraful.learningquest.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.GameDataStore

@Composable
fun AnishRewardGalleryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val data by store.gameData.collectAsState(initial = null)
    val total = data?.anishTotalQuestions ?: 0

    val trophyUnlocked = total >= 50
    val petUnlocked = total >= 150
    var selectedPet by remember { mutableStateOf("🐼") }

    val transition = rememberInfiniteTransition(label = "reward_gallery")
    val pulse by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(850), RepeatMode.Reverse),
        label = "pulse"
    )
    val spin by transition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(1100), RepeatMode.Reverse),
        label = "spin"
    )

    Column(
        Modifier.fillMaxSize()
            .background(Color(0xFFF7FAFF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("🏆 অনিশের রিওয়ার্ড গ্যালারি", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
        Text("প্রশ্ন সমাধান করো • পুরস্কার আনলক করো • সংগ্রহ করো", fontSize = 13.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(10.dp))

        Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFE9F2FF)) {
            Text("📝 $total প্রশ্ন সম্পন্ন", Modifier.padding(horizontal = 14.dp, vertical = 9.dp), fontWeight = FontWeight.Bold, color = Color(0xFF315FBA))
        }

        Spacer(Modifier.height(16.dp))

        RewardCard(
            title = "LEVEL 1 • 2D REWARDS",
            icon = "🎁",
            description = "শুরুতেই ব্যাজ ও ছোট পুরস্কার সংগ্রহ করো।",
            unlocked = true,
            color = Color(0xFFF1F5FF),
            pulse = pulse,
            spin = spin
        )

        Spacer(Modifier.height(10.dp))

        RewardCard(
            title = "LEVEL 2 • 3D TROPHIES",
            icon = "🏆",
            description = if (trophyUnlocked) "অভিনন্দন! ট্রফি সংগ্রহ এখন আনলক।" else "৫০টি প্রশ্ন সম্পন্ন করলে 3D-style ট্রফি আনলক হবে।",
            unlocked = trophyUnlocked,
            color = Color(0xFFFFF4D8),
            pulse = pulse,
            spin = spin
        )

        if (trophyUnlocked) {
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("🏆" to "Gold", "🥇" to "Champion", "🏅" to "Star").forEach { (icon, name) ->
                    Card(
                        Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(icon, fontSize = 40.sp, modifier = Modifier.graphicsLayer { rotationY = spin * 3f; scaleX = pulse; scaleY = pulse })
                            Text(name, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        RewardCard(
            title = "LEVEL 3 • PET COMPANION",
            icon = "🐾",
            description = if (petUnlocked) "তোমার পেট বন্ধু আনলক হয়েছে!" else "১৫০টি প্রশ্ন সম্পন্ন করলে পেট বন্ধু আনলক হবে।",
            unlocked = petUnlocked,
            color = Color(0xFFEAF8EF),
            pulse = pulse,
            spin = spin
        )

        if (petUnlocked) {
            Spacer(Modifier.height(10.dp))
            Text("🐾 CHOOSE YOUR PET", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF23754A))
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("🐼", "🐱", "🐶", "🦊").forEach { pet ->
                    Card(
                        onClick = { selectedPet = pet },
                        Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedPet == pet) Color(0xFFDFF5E6) else Color.White
                        )
                    ) {
                        Text(
                            pet,
                            Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            fontSize = 38.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(Color(0xFFE8F8EE))
            ) {
                Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(selectedPet, fontSize = 86.sp, modifier = Modifier.graphicsLayer {
                        scaleX = pulse
                        scaleY = pulse
                        rotationY = spin * 4f
                    })
                    Text("তোমার Learning Buddy", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF23754A))
                    Text("এটি তোমার সঙ্গে থাকবে — আরও প্রশ্ন সমাধান করলে নতুন পেট যোগ হবে।", fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF60758A))
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        OutlinedButton(onClick = onBack, Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            Text("‹ ফিরে যাই", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun RewardCard(
    title: String,
    icon: String,
    description: String,
    unlocked: Boolean,
    color: Color,
    pulse: Float,
    spin: Float
) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (unlocked) icon else "🔒",
                fontSize = 48.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = if (unlocked) pulse else 1f
                    scaleY = if (unlocked) pulse else 1f
                    rotationY = if (unlocked) spin * 3f else 0f
                }
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                Text(description, fontSize = 12.sp, color = Color(0xFF60758A))
                Spacer(Modifier.height(5.dp))
                Text(if (unlocked) "✓ UNLOCKED" else "🔒 LOCKED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (unlocked) Color(0xFF23754A) else Color(0xFF8A96A8))
            }
        }
    }
}
