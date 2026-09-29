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
    var selectedPet by remember { mutableStateOf("🐼") }
    val ability = when (selectedPet) {
        "🐼" -> if (petLevel >= 3) "ZEN BOOST 🧘" else "Calm Friend"
        "🐱" -> if (petLevel >= 3) "LUCKY PAW 🍀" else "Quick Friend"
        "🐶" -> if (petLevel >= 3) "CHEER BOOST 🎉" else "Happy Friend"
        else -> if (petLevel >= 3) "FOX FOCUS 🦊" else "Clever Friend"
    }
    val abilityMessage = when (selectedPet) {
        "🐼" -> "Panda helps you stay calm and focused!"
        "🐱" -> "Cat's Lucky Paw keeps you motivated!"
        "🐶" -> "Puppy's Cheer Boost keeps you motivated!"
        else -> "Fox Focus helps you take on challenges!"
    }
    var petTapCount by remember { mutableStateOf(0) }
    var petReaction by remember { mutableStateOf("Learn with me! 🐾") }
    var petBounce by remember { mutableStateOf(false) }

    val petReactions = mapOf(
        "🐼" to listOf("Panda says: Great job! 🎉", "Let's do 5 more questions! 📚", "Yes! We can do it! 💪"),
        "🐱" to listOf("Cat says: Meow! Correct answer! 😺", "Let's go to the next question! ✨", "You're doing great! 🌟"),
        "🐶" to listOf("Puppy says: Wow! 🐶", "Let's learn together! 📖", "Good job! ⭐"),
        "🦊" to listOf("Fox says: Smart answer! 🦊", "Take the challenge! 🚀", "Today's learning is awesome! 🔥")
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
        Text("🏆 Anish Reward Gallery", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
        Text("Solve questions • Unlock rewards • Collect them", fontSize = 13.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(10.dp))

        Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFE9F2FF)) {
            Text("📝 $total questions completed", Modifier.padding(horizontal = 14.dp, vertical = 9.dp), fontWeight = FontWeight.Bold, color = Color(0xFF315FBA))
        }

        Spacer(Modifier.height(16.dp))

        RewardCard(
            title = "LEVEL 1 • 2D REWARDS",
            icon = "🎁",
            description = "Collect badges and starter rewards.",
            unlocked = true,
            color = Color(0xFFF1F5FF),
            pulse = pulse,
            spin = spin
        )

        Spacer(Modifier.height(10.dp))

        RewardCard(
            title = "LEVEL 2 • 3D TROPHIES",
            icon = "🏆",
            description = if (trophyUnlocked) "Congratulations! Trophy collection is unlocked." else "Complete 50 questions to unlock 3D-style trophies.",
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
            description = if (petUnlocked) "Your pet companion is unlocked!" else "Complete 150 questions to unlock your pet companion.",
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
                    Text("Your Learning Buddy", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF23754A))
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
                        progress = petXp / 50f,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 5.dp),
                        color = Color(0xFF43A866),
                        trackColor = Color(0xFFD5EBDD)
                    )
                    Text(
                        if (petLevel >= 5) "MAX LEVEL! 🌟" else "${50 - petXp} more questions to reach the next pet level! 🚀",
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
                                if (petLevel >= 3) abilityMessage else "This ability unlocks at Level 3!",
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF60758A)
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(petReaction, fontSize = 13.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color(0xFF315FBA))
                    Text("Tap your pet • $petTapCount interactions 🐾", fontSize = 11.sp, textAlign = TextAlign.Center, color = Color(0xFF60758A))
                    Text("Complete questions to earn Pet XP and reach new levels.", fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF60758A))
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        OutlinedButton(onClick = onBack, Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            Text("‹ Back", fontWeight = FontWeight.Bold)
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
