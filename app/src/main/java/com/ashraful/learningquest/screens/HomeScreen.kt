package com.ashraful.learningquest.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.GameDataStore
import com.ashraful.learningquest.data.giftCatalog
import kotlinx.coroutines.delay

@Composable
fun HomeScreen() {
    var profile by rememberSaveable { mutableStateOf<String?>(null) }
    var screen by rememberSaveable { mutableStateOf("home") }
    var backStack by rememberSaveable { mutableStateOf("") }

    fun navigate(route: String) {
        if (route == "switch_to_abid") {
            profile = "abid"
            screen = "home"
            backStack = ""
        } else if (route == "switch_to_arifa") {
            profile = "arifa"
            screen = "home"
            backStack = ""
        } else if (route == "switch_to_anish") {
            profile = "anish"
            screen = "home"
            backStack = ""
        } else if (route != screen) {
            backStack = listOf(backStack, screen).filter { it.isNotBlank() }.joinToString(",")
            screen = route
        }
    }

    fun goBack() {
        val stack = backStack.split(",").filter { it.isNotBlank() }
        if (stack.isNotEmpty()) {
            screen = stack.last()
            backStack = stack.dropLast(1).joinToString(",")
        } else {
            screen = "home"
        }
    }

    BackHandler(enabled = profile != null && screen != "home") {
        goBack()
    }

    if (profile == null) {
        ProfileSelectionScreen { selected ->
            profile = selected
            screen = "home"
        }
        return
    }

    if (screen == "home") {
        when (profile) {
            "arifa" -> HomeContent(onNavigate = { route -> navigate(route) })
            "anish" -> AnishHomeScreen { route -> navigate(route) }
            else -> AbidHomeScreen { route -> navigate(route) }
        }
        return
    }

    // One persistent in-app Home/Back button for every learning screen.
    Box(Modifier.fillMaxSize()) {
        when (screen) {
            "math" -> MathScreen { goBack() }
            "english" -> EnglishScreen { goBack() }
            "science" -> ScienceScreen { goBack() }
            "puzzle" -> PuzzleScreen { goBack() }
            "mixed" -> MixedQuizScreen { goBack() }
            "arifa_learning_hub" -> ArifaLearningHubTestScreen { goBack() }
            "arifa_review_mistakes" -> ArifaReviewMistakesScreen { goBack() }
            "arifa_topic_practice" -> ArifaTopicPracticeScreen { goBack() }
            "arifa_brain" -> ArifaBrainGamesScreen { goBack() }
            "arifa_reading" -> ArifaReadingAdventureScreen { goBack() }
            "arifa_writing" -> ArifaWritingPracticeScreen { goBack() }
            "arifa_advanced_math" -> ArifaAdvancedMathScreen { goBack() }
            "arifa_world" -> ArifaWorldExplorerScreen { goBack() }
            "arifa_visual" -> ArifaVisualPuzzlesScreen { goBack() }
            "arifa_speaking" -> ArifaEnglishSpeakingScreen { goBack() }
            "arifa_daily" -> ArifaDailyChallengeScreen { goBack() }
            "arifa_achievements2" -> ArifaAchievements2Screen { goBack() }
            "arifa_path" -> ArifaLearningPathScreen { goBack() }
            "arifa_gift_store" -> ArifaGiftStoreScreen { goBack() }
            "abid_letters" -> AbidLettersScreen { goBack() }
            "abid_write_letters" -> AbidWriteLettersScreen { goBack() }
            "abid_bengali_letters" -> AbidBengaliLettersScreen { goBack() }
            "abid_bengali_practice" -> AbidBengaliPracticeScreen { goBack() }
            "abid_bengali_picture_match" -> AbidBengaliPictureMatchScreen { goBack() }
            "abid_bengali_tracing" -> AbidBengaliTracingScreen { goBack() }
            "abid_numbers" -> AbidNumbersScreen { goBack() }
            "abid_shapes" -> AbidShapesScreen { goBack() }
            "abid_colors" -> AbidColorsScreen { goBack() }
            "abid_color_match" -> AbidColorMatchScreen { goBack() }
            "abid_memory" -> AbidMemoryPairsScreen { goBack() }
            "abid_letter_sounds" -> AbidLetterSoundsScreen { goBack() }
            "abid_picture_match" -> AbidLetterPictureMatchScreen { goBack() }
            "abid_simple_math" -> AbidSimpleMathScreen { goBack() }
            "abid_shape_match" -> AbidShapeMatchScreen { goBack() }
            "abid_animals" -> AbidAnimalSoundsScreen { goBack() }
            "abid_fruits" -> AbidFruitsScreen { goBack() }
            "abid_memory2" -> AbidMemoryGameScreen { goBack() }
            "abid_achievements" -> AbidAchievementsScreen { goBack() }
            "abid_match_letters" -> AbidMatchLettersScreen { goBack() }
            "abid_number_match" -> AbidNumberMatchScreen { goBack() }
            "abid_challenge" -> AbidMiniChallengeScreen { goBack() }
            "anish_bengali" -> AnishSubjectQuizScreen("বাংলা") { goBack() }
            "anish_math" -> AnishSubjectQuizScreen("গণিত") { goBack() }
            "anish_science" -> AnishSubjectQuizScreen("বিজ্ঞান") { goBack() }
            "anish_history" -> AnishSubjectQuizScreen("ইতিহাস ও ভূগোল") { goBack() }
            "anish_english" -> AnishSubjectQuizScreen("ইংরেজি") { goBack() }
            "anish_gk" -> AnishSubjectQuizScreen("সাধারণ জ্ঞান") { goBack() }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White.copy(alpha = 0.96f),
            shadowElevation = 5.dp
        ) {
            TextButton(
                onClick = { goBack() },
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text("‹  Back", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun HomeContent(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val data by store.gameData.collectAsState(initial = null)
    val equippedGiftId by store.equippedGiftId.collectAsState(initial = null)
    val equippedGift = giftCatalog.firstOrNull { it.id == equippedGiftId }
    val currentCoins = data?.coins ?: 0
    var lastKnownCoins by rememberSaveable { mutableIntStateOf(-1) }
    var rewardDelta by rememberSaveable { mutableIntStateOf(0) }
    var showReward by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(currentCoins) {
        if (lastKnownCoins >= 0 && currentCoins > lastKnownCoins) {
            rewardDelta = currentCoins - lastKnownCoins
            showReward = true
        }
        lastKnownCoins = currentCoins
    }
    LaunchedEffect(showReward) {
        if (showReward) {
            delay(1400)
            showReward = false
        }
    }

    val giftAnimation = rememberInfiniteTransition(label = "gift_reward")
    val giftPulse by giftAnimation.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(420, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gift_pulse"
    )
    val giftRotation by giftAnimation.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(520, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gift_rotation"
    )
    var showMore by rememberSaveable { mutableStateOf(false) }

    val scores = listOf(
        "math" to (data?.mathScore ?: 0),
        "english" to (data?.englishScore ?: 0),
        "science" to (data?.scienceScore ?: 0),
        "puzzle" to (data?.puzzleScore ?: 0)
    )
    val weakest = scores.minByOrNull { it.second }?.first ?: "math"
    val continueInfo = when (weakest) {
        "english" -> Triple("🔤", "English", "Words, grammar & language")
        "science" -> Triple("🔬", "Science", "Explore the world around you")
        "puzzle" -> Triple("🧩", "Puzzles", "Think, solve & discover")
        else -> Triple("➗", "Quick Math", "Numbers & problem solving")
    }
    val todayProgress = data?.todayProgress ?: 0
    val goalProgress = (todayProgress / 3f).coerceIn(0f, 1f)

    Box(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFF4F7FF), Color.White))),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            Modifier.fillMaxWidth().widthIn(max = 920.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onNavigate("switch_to_abid") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("👦 Switch to Abid", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onNavigate("switch_to_anish") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("🧑 Switch to Anish", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(10.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Hi, Arifa! 👋", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF172B5C))
                    Text("What shall we learn today?", fontSize = 14.sp, color = Color(0xFF71809A))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFFF3D4)) {
                        Text("🪙 ${data?.coins ?: 0}", Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5B00))
                    }
                    Surface(
                        onClick = { onNavigate("arifa_gift_store") },
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFEAF2FF)
                    ) {
                        Text("🎁", Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 18.sp)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (equippedGift != null) {
                Card(
                    onClick = { onNavigate("arifa_gift_store") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3ECFF)),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.85f)) {
                            Text(equippedGift.emoji, Modifier.padding(10.dp), fontSize = 28.sp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("MY REWARD", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
                            Text(equippedGift.name, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4A2B73))
                            Text(equippedGift.description, fontSize = 11.sp, color = Color(0xFF78658A))
                        }
                        Text("🎁", fontSize = 20.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(Color.Transparent)) {
                Box(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF315FBA), Color(0xFF5636A8)))).padding(20.dp)) {
                    Column {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("LEVEL ${data?.level ?: 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDDE7FF))
                                Text("${data?.xp ?: 0} XP", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("🔥 ${data?.streak ?: 0}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                Text("day streak", fontSize = 11.sp, color = Color(0xFFE6ECFF))
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        LinearProgressIndicator(progress = { data?.xpProgress ?: 0f }, Modifier.fillMaxWidth().height(8.dp), color = Color.White, trackColor = Color.White.copy(alpha = .22f))
                        Spacer(Modifier.height(6.dp))
                        Text("${data?.xpInCurrentLevel ?: 0} / 100 XP to next level", fontSize = 12.sp, color = Color(0xFFE6ECFF))
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(Color(0xFFFFF8E8))) {
                Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🎯", fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("TODAY'S GOAL", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9A6800), modifier = Modifier.weight(1f))
                            Text("$todayProgress / 3", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B6500))
                        }
                        Text("Complete 3 learning challenges", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5F4700))
                        LinearProgressIndicator(progress = { goalProgress }, modifier = Modifier.fillMaxWidth().padding(top = 6.dp).height(6.dp), color = Color(0xFFE0A400), trackColor = Color(0xFFFFE9A8))
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
            Text("START LEARNING", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF65738A))
            Spacer(Modifier.height(6.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CompactSubject("➗", "Maths", Color(0xFFEAF2FF), Color(0xFF2457A6), Modifier.weight(1f)) { onNavigate("math") }
                CompactSubject("🔢", "Advanced Maths", Color(0xFFE8F0FF), Color(0xFF315FBA), Modifier.weight(1f)) { onNavigate("arifa_advanced_math") }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CompactSubject("🔤", "English", Color(0xFFF3ECFF), Color(0xFF7043A8), Modifier.weight(1f)) { onNavigate("english") }
                CompactSubject("🔬", "Science", Color(0xFFE8F8EF), Color(0xFF23754A), Modifier.weight(1f)) { onNavigate("science") }
            }
            Row(Modifier.fillMaxWidth()) {
                CompactSubject("🧩", "Puzzles", Color(0xFFFFF1DE), Color(0xFF9A5A00), Modifier.fillMaxWidth()) { onNavigate("puzzle") }
            }

            Spacer(Modifier.height(10.dp))
            SubjectCard("🧠", "Learning Hub", "Adaptive practice • 206 questions • remembers progress", Color(0xFFEAF2FF), Color(0xFF315FBA)) { onNavigate("arifa_learning_hub") }

            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CompactSubject("🌈", "Mixed Quiz", Color(0xFFFFE8F5), Color(0xFF6A1B9A), Modifier.weight(1f)) { onNavigate("mixed") }
                CompactSubject("🎯", "Topics", Color(0xFFE8F8EF), Color(0xFF23754A), Modifier.weight(1f)) { onNavigate("arifa_topic_practice") }
            }

            Spacer(Modifier.height(12.dp))
            Card(
                onClick = { onNavigate("arifa_gift_store") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3D4)),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🎁", fontSize = 30.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("GIFT STORE", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B5B00))
                        Text("Spend your coins on learning rewards", fontSize = 12.sp, color = Color(0xFF9A6B16))
                    }
                    Text("›", fontSize = 30.sp, color = Color(0xFF8B5B00))
                }
            }

            Spacer(Modifier.height(18.dp))
            Text("CONTINUE LEARNING", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF65738A))
            Spacer(Modifier.height(6.dp))
            SubjectCard(continueInfo.first, continueInfo.second, continueInfo.third, Color(0xFFF0F5FF), Color(0xFF315FBA)) { onNavigate(weakest) }

            Spacer(Modifier.height(14.dp))
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
                Column(Modifier.padding(15.dp)) {
                    Text("YOUR PROGRESS", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7A8798))
                    Spacer(Modifier.height(10.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        ScorePill("Math", data?.mathScore ?: 0, Color(0xFF2457A6))
                        ScorePill("English", data?.englishScore ?: 0, Color(0xFF7043A8))
                        ScorePill("Science", data?.scienceScore ?: 0, Color(0xFF23754A))
                        ScorePill("Puzzle", data?.puzzleScore ?: 0, Color(0xFF9A5A00))
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CompactSubject("🔁", "Review Mistakes", Color(0xFFFFE8E8), Color(0xFFC62828), Modifier.weight(1f)) { onNavigate("arifa_review_mistakes") }
                CompactSubject("🗺️", "Learning Path", Color(0xFFE8F8EF), Color(0xFF247A57), Modifier.weight(1f)) { onNavigate("arifa_path") }
            }

            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), onClick = { showMore = !showMore }, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color(0xFFF1F5FF))) {
                Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🚀", fontSize = 26.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("MORE ACTIVITIES", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                        Text("Brain, reading, writing, world & more", fontSize = 12.sp, color = Color(0xFF71809A))
                    }
                    Text(if (showMore) "⌃" else "⌄", fontSize = 22.sp, color = Color(0xFF315FBA))
                }
            }

            if (showMore) {
                Spacer(Modifier.height(5.dp))
                SubjectCard("🧠", "Brain Games", "Logic, patterns & thinking", Color(0xFFF3E5FF), Color(0xFF6A1B9A)) { onNavigate("arifa_brain") }
                SubjectCard("📖", "Reading Adventure", "Stories & comprehension", Color(0xFFE3F2FD), Color(0xFF1565C0)) { onNavigate("arifa_reading") }
                SubjectCard("✍️", "Writing Practice", "Spelling & sentences", Color(0xFFE0F7F4), Color(0xFF00897B)) { onNavigate("arifa_writing") }
                SubjectCard("📝", "Written Maths", "Solve word problems and write your answer", Color(0xFFE8F0FF), Color(0xFF315FBA)) { onNavigate("arifa_advanced_math") }
                SubjectCard("🌍", "World Explorer", "India, science & our world", Color(0xFFFFF1DE), Color(0xFFE67E22)) { onNavigate("arifa_world") }
                SubjectCard("🧩", "Visual Puzzles", "Patterns & sequences", Color(0xFFFFF1DE), Color(0xFF9A5A00)) { onNavigate("arifa_visual") }
                SubjectCard("🗣️", "English Speaking", "Listen and practise", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("arifa_speaking") }
                SubjectCard("🔥", "Daily Challenge", "10 daily questions", Color(0xFFFFEEDB), Color(0xFFD35400)) { onNavigate("arifa_daily") }
                SubjectCard("🏆", "Achievements", "Unlock milestones", Color(0xFFFFF5D9), Color(0xFFB77900)) { onNavigate("arifa_achievements2") }
            }

            Spacer(Modifier.height(18.dp))
            Text("Learn • Play • Grow 🚀", Modifier.fillMaxWidth().padding(bottom = 12.dp), fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF8A96A8))
        }

        val rewardEffect = when (equippedGift?.id) {
            "rocket" -> "🚀 BOOST!"
            "ufo" -> "🛸 SPACE BONUS!"
            "balloon" -> "🎈 UP YOU GO!"
            "teddy" -> "🧸 SUPER CUDDLE!"
            "guitar" -> "🎸 ROCK ON!"
            "skateboard" -> "🛹 AWESOME!"
            "rainbow" -> "🌈 RAINBOW MAGIC!"
            "space" -> "🌌 SPACE POWER!"
            "ocean" -> "🌊 OCEAN POWER!"
            "forest" -> "🌲 FOREST POWER!"
            "sky" -> "☁️ SKY POWER!"
            "garden" -> "🌷 GARDEN MAGIC!"
            "star" -> "⭐ STAR POWER!"
            "crown" -> "👑 ROYAL REWARD!"
            "diamond" -> "💎 DIAMOND BONUS!"
            "trophy" -> "🏆 CHAMPION!"
            "fire" -> "🔥 FIRE POWER!"
            "lightning" -> "⚡ LIGHTNING!"
            else -> "🎉 GREAT JOB!"
        }

        AnimatedVisibility(
            visible = showReward,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 76.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFFFF3D4),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 11.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val effectModifier = when (equippedGift?.id) {
                        "rocket" -> Modifier.offset(y = (-10).dp).graphicsLayer { scaleX = giftPulse; scaleY = giftPulse }
                        "balloon" -> Modifier.offset(y = (-6).dp).graphicsLayer { scaleX = giftPulse; scaleY = giftPulse }
                        "rainbow", "star", "diamond", "lightning" -> Modifier.graphicsLayer { scaleX = giftPulse; scaleY = giftPulse; rotationZ = giftRotation }
                        "crown", "trophy" -> Modifier.graphicsLayer { scaleX = giftPulse; scaleY = giftPulse; rotationZ = giftRotation / 2f }
                        "ufo", "skateboard" -> Modifier.graphicsLayer { rotationZ = giftRotation }
                        else -> Modifier.graphicsLayer { scaleX = giftPulse; scaleY = giftPulse }
                    }
                    Text(rewardEffect, modifier = effectModifier, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B5B00))
                    Text("+$rewardDelta 🪙 Coins earned!", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5B00))
                }
            }
        }
    }
}

@Composable
private fun CompactSubject(icon: String, title: String, background: Color, accent: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = modifier.padding(vertical = 3.dp), shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(background), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(Modifier.fillMaxWidth().padding(13.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 25.sp)
            Spacer(Modifier.height(4.dp))
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = accent)
        }
    }
}

@Composable
private fun ScorePill(subject: String, score: Int, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$score", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = accent)
        Text(subject, fontSize = 10.sp, color = Color(0xFF7A8798))
    }
}

@Composable
private fun SubjectCard(
    icon: String,
    title: String,
    subtitle: String,
    color: Color,
    titleColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.8f)) {
                Text(icon, modifier = Modifier.padding(11.dp), fontSize = 21.sp)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = titleColor)
                Text(subtitle, fontSize = 12.sp, color = Color(0xFF65778B))
            }
            Text("›", fontSize = 30.sp, fontWeight = FontWeight.Light, color = titleColor)
        }
    }
}