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
    val petLevel = if (petUnlocked) ((total - 150) / 50 + 1).coerceAtMost(5) else 0
    val petXp = if (petUnlocked) ((total - 150) % 50) else 0
    val petScale = when (petLevel) { 1 -> 0.92f; 2 -> 0.98f; 3 -> 1.04f; 4 -> 1.10f; else -> 1.16f }
    val petRotation = when (petLevel) { 1 -> 2f; 2 -> 4f; 3 -> 7f; 4 -> 10f; else -> 14f }
    val petAura = when (petLevel) { 1 -> "🌱"; 2 -> "✨"; 3 -> "💫"; 4 -> "🔥"; else -> "👑" }
    val ability = when (selectedPet) { "🐼" -> if (petLevel >= 3) "ZEN BOOST 🧘" else "Calm Friend"; "🐱" -> if (petLevel >= 3) "LUCKY PAW 🍀" else "Quick Friend"; "🐶" -> if (petLevel >= 3) "CHEER BOOST 🎉" else "Happy Friend"; else -> if (petLevel >= 3) "FOX FOCUS 🦊" else "Clever Friend" }
    val abilityMessage = when (selectedPet) { "🐼" -> "পান্ডা তোমাকে শান্তভাবে ফোকাস করতে সাহায্য করে!"; "🐱" -> "বিড়ালের Lucky Paw তোমাকে উৎসাহ দেয়!"; "🐶" -> "কুকুরছানার Cheer Boost তোমাকে মোটিভেট করে!"; else -> "শিয়ালের Focus Mode তোমাকে চ্যালেঞ্জ নিতে সাহায্য করে!" }
    var selectedPet by remember { mutableStateOf("🐼") }    val abilityMessage = when (selectedPet) { "🐼" -> "পান্ডা তোমাকে শান্তভাবে ফোকাস করতে সাহায্য করে!"; "🐱" -> "বিড়ালের Lucky Paw তোমাকে উৎসাহ দেয়!"; "🐶" -> "কুকুরছানার Cheer Boost তোমাকে মোটিভেট করে!"; else -> "শিয়ালের Focus Mode তোমাকে চ্যালেঞ্জ নিতে সাহায্য করে!" }
    var selectedPet by remember { mutableStateOf("🐼") }
    var petTapCount by remember { mutableStateOf(0) }
    var petReaction by remember { mutableStateOf("আমার সঙ্গে শেখো! 🐾") }
    var petBounce by remember { mutableStateOf(false) }

    val petReactions = mapOf(
        "🐼" to listOf("পান্ডা বলছে: দারুণ করেছ! 🎉", "আরও ৫টি প্রশ্ন করি! 📚", "ইয়েস! আমরা পারব! 💪"),
        "🐱" to listOf("বিড়াল বলছে: মিউ! সঠিক উত্তর! 😺", "চলো পরের প্রশ্নে যাই! ✨", "তুমি খুব ভালো করছ! 🌟"),
        "🐶" to listOf("কুকুরছানা বলছে: ওয়াও! 🐶", "চলো একসঙ্গে শিখি! 📖", "গুড জব! ⭐"),
        "🦊" to listOf("শিয়াল বলছে: স্মার্ট উত্তর! 🦊", "চ্যালেঞ্জ নাও! 🚀", "আজকের শেখা অসাধারণ! 🔥")
    )
    val bounce by animateFloatAsState(
        targetValue = if (petBounce) 1.14f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "pet_bounce"
    )

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
                    Surface(
                        onClick = {
                            petTapCount++
                            petBounce = true
                            petReaction = petReactions[selectedPet]?.get(petTapCount % 3) ?: "চলো শিখি! 🐾"
                        },
                        shape = RoundedCornerShape(32.dp),
                        color = Color.White
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(petAura, fontSize = 30.sp)
                            Text(
                                selectedPet,
                                fontSize = (86 * petScale).sp,
                                modifier = Modifier.padding(horizontal = 26.dp, vertical = 4.dp).graphicsLayer {
                                    scaleX = pulse * bounce
                                    scaleY = pulse * bounce
                                    rotationY = spin * petRotation
                                }
                            )
                        }
                    }
                    LaunchedEffect(petBounce) {
                        if (petBounce) {
                            kotlinx.coroutines.delay(280)
                            petBounce = false
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("তোমার Learning Buddy", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF23754A))
                    Text("$petAura PET LEVEL $petLevel • $petXp / 50 XP", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    Text(
                        when (petLevel) {
                            1 -> "BABY BUDDY 🌱"
                            2 -> "HAPPY BUDDY ✨"
                            3 -> "SUPER BUDDY 💫"
                            4 -> "HERO BUDDY 🔥"
                            else -> "LEGENDARY BUDDY 👑"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF23754A)
                    )
                    LinearProgressIndicator(
                        progress = { petXp / 50f },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 5.dp),
                        color = Color(0xFF43A866),
                        trackColor = Color(0xFFD5EBDD)
                    )
                    Text(
                        if (petLevel >= 5) "MAX LEVEL! 🌟" else "আর ${50 - petXp}টি প্রশ্নে পেটের পরের লেভেল! 🚀",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF23754A)
                    )
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFF3F8FF)
                    ) {
                        Column(Modifier.padding(horizontal = 14.dp, vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚡ $ability", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                            Text(
                                if (petLevel >= 3) abilityMessage else "Level 3-এ এই ability unlock হবে!",
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF60758A)
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(petReaction, fontSize = 13.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color(0xFF315FBA))
                    Text("পেটকে ট্যাপ করো • $petTapCount বার খেলেছো 🐾", fontSize = 11.sp, textAlign = TextAlign.Center, color = Color(0xFF60758A))
                    Text("প্রশ্ন সমাধান করলে পেট XP পাবে এবং নতুন লেভেলে উঠবে।", fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF60758A))
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
