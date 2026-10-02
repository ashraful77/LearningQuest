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
import androidx.compose.foundation.ScrollState
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
    val arifaHomeScrollState = rememberScrollState()
    val abidHomeScrollState = rememberScrollState()
    val anishHomeScrollState = rememberScrollState()

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

    fun goHome() {
        screen = "home"
        backStack = ""
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
            "arifa" -> HomeContent(
                scrollState = arifaHomeScrollState,
                onNavigate = { route -> navigate(route) }
            )
            "anish" -> AnishHomeScreen(
                scrollState = anishHomeScrollState,
                onNavigate = { route -> navigate(route) }
            )
            else -> AbidHomeScreen(
                scrollState = abidHomeScrollState,
                onNavigate = { route -> navigate(route) }
            )
        }
        return
    }

    // Fixed navigation bar: the activity content scrolls above it.
    Column(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
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
            "arifa_coin_store" -> ArifaCoinStoreScreen { goBack() }
            "arifa_path" -> ArifaLearningPathScreen { goBack() }
            "arifa_gift_store" -> ArifaGiftStoreScreen { goBack() }
            "abid_letters" -> AbidLettersScreen { goBack() }
            "abid_write_letters" -> AbidWriteLettersScreen { goBack() }
            "abid_bengali_letters" -> AbidBengaliLettersScreen { goBack() }
            "abid_bengali_practice" -> AbidBengaliPracticeScreen { goBack() }
            "abid_bengali_listen_sets" -> AbidBengaliListenChooseSetsScreen({ set -> navigate("abid_bengali_listen_" + set) }, { goBack() })
            "abid_bengali_listen_1" -> AbidBengaliListenChooseScreen(1) { goBack() }
            "abid_bengali_listen_2" -> AbidBengaliListenChooseScreen(2) { goBack() }
            "abid_bengali_listen_3" -> AbidBengaliListenChooseScreen(3) { goBack() }
            "abid_bengali_listen_4" -> AbidBengaliListenChooseScreen(4) { goBack() }
            "abid_bengali_listen_5" -> AbidBengaliListenChooseScreen(5) { goBack() }
            "abid_bengali_picture_match" -> AbidBengaliPictureMatchScreen { goBack() }
            "abid_bengali_tracing" -> AbidBengaliTracingScreen { goBack() }
            "abid_numbers" -> AbidNumbersScreen { goBack() }
            "abid_shapes" -> AbidShapesScreen { goBack() }
            "abid_colors" -> AbidColorsScreen { goBack() }
            "abid_color_match" -> AbidColorMatchScreen { goBack() }
            "abid_memory" -> AbidMemoryPairsScreen { goBack() }
            "abid_letter_sounds" -> AbidLetterSoundsScreen { goBack() }
            "abid_listen_choose" -> AbidListenAndChooseScreen { goBack() }
            "abid_picture_match" -> AbidLetterPictureMatchScreen { goBack() }
            "abid_simple_math" -> AbidSimpleMathScreen { goBack() }
            "abid_shape_match" -> AbidShapeMatchScreen { goBack() }
            "abid_animals" -> AbidAnimalSoundsScreen { goBack() }
            "abid_fruits" -> AbidFruitsScreen { goBack() }
            "abid_memory2" -> AbidMemoryGameScreen { goBack() }
            "abid_achievements" -> AbidAchievementsScreen { goBack() }
            "abid_match_letters" -> AbidMatchLettersScreen { goBack() }
            "abid_number_match" -> AbidNumberMatchScreen { goBack() }
            "abid_match_numbers_test" -> AbidMatchNumbersTestScreen { goBack() }
            "abid_challenge" -> AbidMiniChallengeScreen { goBack() }
            "abid_fun_zone" -> AbidFunZoneScreen { goBack() }
            "abid_rewards" -> AbidRewardRoomScreen { goBack() }
            "anish_bengali" -> AnishSetSelectionScreen("বাংলা", { set -> navigate("anish_quiz_bengali_" + set) }, { goBack() })
            "anish_math" -> AnishSetSelectionScreen("গণিত", { set -> navigate("anish_quiz_math_" + set) }, { goBack() })
            "anish_science" -> AnishSetSelectionScreen("বিজ্ঞান", { set -> navigate("anish_quiz_science_" + set) }, { goBack() })
            "anish_history" -> AnishSetSelectionScreen("ইতিহাস ও ভূগোল", { set -> navigate("anish_quiz_history_" + set) }, { goBack() })
            "anish_english" -> AnishSetSelectionScreen("ইংরেজি", { set -> navigate("anish_quiz_english_" + set) }, { goBack() })
            "anish_gk" -> AnishSetSelectionScreen("সাধারণ জ্ঞান", { set -> navigate("anish_quiz_gk_" + set) }, { goBack() })
            "anish_quiz_bengali_1" -> AnishSubjectQuizScreen("বাংলা", 1) { goBack() }
            "anish_quiz_bengali_2" -> AnishSubjectQuizScreen("বাংলা", 2) { goBack() }
            "anish_quiz_bengali_3" -> AnishSubjectQuizScreen("বাংলা", 3) { goBack() }
            "anish_quiz_math_1" -> AnishSubjectQuizScreen("গণিত", 1) { goBack() }
            "anish_quiz_math_2" -> AnishSubjectQuizScreen("গণিত", 2) { goBack() }
            "anish_quiz_math_3" -> AnishSubjectQuizScreen("গণিত", 3) { goBack() }
            "anish_quiz_science_1" -> AnishSubjectQuizScreen("বিজ্ঞান", 1) { goBack() }
            "anish_quiz_science_2" -> AnishSubjectQuizScreen("বিজ্ঞান", 2) { goBack() }
            "anish_quiz_science_3" -> AnishSubjectQuizScreen("বিজ্ঞান", 3) { goBack() }
            "anish_quiz_history_1" -> AnishSubjectQuizScreen("ইতিহাস ও ভূগোল", 1) { goBack() }
            "anish_quiz_history_2" -> AnishSubjectQuizScreen("ইতিহাস ও ভূগোল", 2) { goBack() }
            "anish_quiz_history_3" -> AnishSubjectQuizScreen("ইতিহাস ও ভূগোল", 3) { goBack() }
            "anish_quiz_english_1" -> AnishSubjectQuizScreen("ইংরেজি", 1) { goBack() }
            "anish_quiz_english_2" -> AnishSubjectQuizScreen("ইংরেজি", 2) { goBack() }
            "anish_quiz_english_3" -> AnishSubjectQuizScreen("ইংরেজি", 3) { goBack() }
            "anish_quiz_gk_1" -> AnishSubjectQuizScreen("সাধারণ জ্ঞান", 1) { goBack() }
            "anish_quiz_gk_2" -> AnishSubjectQuizScreen("সাধারণ জ্ঞান", 2) { goBack() }
            "anish_quiz_gk_3" -> AnishSubjectQuizScreen("সাধারণ জ্ঞান", 3) { goBack() }
            "anish_store" -> AnishStoreScreen { goBack() }
            "anish_rewards" -> AnishRewardGalleryScreen { goBack() }
            "anish_model_test" -> AnishModelTestScreen { goBack() }
        }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            color = Color.White,
            shadowElevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { goHome() },
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text("⌂  Home", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                }

                TextButton(
                    onClick = { goBack() },
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text("Back  ›", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
                }
            }
        }
    }
}
 
@Composable
private fun HomeContent(
    scrollState: ScrollState,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val data by store.gameData.collectAsState(initial = null)
    val completed = (data?.todayProgress ?: 0).coerceAtMost(3)

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF4F7FF), Color.White, Color(0xFFF8F3FF))))
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("👧 Arifa's Learning Zone", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA), maxLines = 1, textAlign = TextAlign.Center)
        Text("Class II • Fun Learning", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7043A8))
        Spacer(Modifier.height(8.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { onNavigate("switch_to_abid") }, modifier = Modifier.weight(1f)) {
                    Text("👦 Abid", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
                VerticalDivider(modifier = Modifier.height(28.dp), color = Color(0xFFD9E0EA))
                TextButton(onClick = { onNavigate("switch_to_anish") }, modifier = Modifier.weight(1f)) {
                    Text("🧑 Anish", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(Color(0xFFFFF4D8)), elevation = CardDefaults.cardElevation(1.dp)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(if (completed >= 3) "🎉" else "🎯", fontSize = 22.sp)
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(if (completed >= 3) "Daily Goal Complete!" else "Daily Goal", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9A5A00))
                    Text(if (completed >= 3) "+3 ⭐ and +10 XP earned" else "Complete 3 activities • +3 ⭐ +10 XP", fontSize = 10.sp, color = Color(0xFF60758A))
                }
                Text("${completed}/3", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("📚 My Learning", modifier = Modifier.fillMaxWidth(), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))
        Text("Choose a subject to learn today", modifier = Modifier.fillMaxWidth(), fontSize = 11.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(6.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            CompactSubject("➗", "Maths", Color(0xFFEAF2FF), Color(0xFF2457A6), Modifier.weight(1f)) { onNavigate("math") }
            CompactSubject("🔤", "English", Color(0xFFF3ECFF), Color(0xFF7043A8), Modifier.weight(1f)) { onNavigate("english") }
            CompactSubject("🔬", "Science", Color(0xFFE8F8EF), Color(0xFF23754A), Modifier.weight(1f)) { onNavigate("science") }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            CompactSubject("🧩", "Puzzles", Color(0xFFFFEAF4), Color(0xFFB12A73), Modifier.weight(1f)) { onNavigate("puzzle") }
            CompactSubject("🎯", "Mixed Quiz", Color(0xFFFFF4D8), Color(0xFF9A5A00), Modifier.weight(1f)) { onNavigate("mixed") }
            CompactSubject("🧠", "Hub", Color(0xFFE8F0FF), Color(0xFF315FBA), Modifier.weight(1f)) { onNavigate("arifa_learning_hub") }
        }

        Spacer(Modifier.height(8.dp))
        Text("⭐ Practice & Explore", modifier = Modifier.fillMaxWidth(), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))
        SubjectCard("➗", "Advanced Maths", "Word problems and challenging maths", Color(0xFFEAF2FF), Color(0xFF2457A6)) { onNavigate("arifa_advanced_math") }
        SubjectCard("📖", "Topic Practice", "Practise individual topics", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("arifa_topic_practice") }
        SubjectCard("🧠", "Brain Games", "Think, solve and discover", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("arifa_brain") }
        SubjectCard("📚", "Reading Adventure", "Read and understand stories", Color(0xFFFFF1DE), Color(0xFF9A5A00)) { onNavigate("arifa_reading") }
        SubjectCard("✍️", "Writing Practice", "Build better writing skills", Color(0xFFFFEAF4), Color(0xFFB12A73)) { onNavigate("arifa_writing") }
        SubjectCard("🌍", "World Explorer", "Explore our world", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("arifa_world") }
        SubjectCard("🧩", "Visual Puzzles", "Solve visual challenges", Color(0xFFEAF2FF), Color(0xFF315FBA)) { onNavigate("arifa_visual") }
        SubjectCard("🗣️", "English Speaking", "Practise speaking English", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("arifa_speaking") }
        SubjectCard("🏆", "Daily Challenge", "A new challenge every day", Color(0xFFFFF4D8), Color(0xFF9A5A00)) { onNavigate("arifa_daily") }
        SubjectCard("🔄", "Review Mistakes", "Practise questions you missed", Color(0xFFFFEAF4), Color(0xFFC13A63)) { onNavigate("arifa_review_mistakes") }
        SubjectCard("🛤️", "Learning Path", "Follow your learning journey", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("arifa_path") }

        Spacer(Modifier.height(10.dp))
        Text("🏆 Rewards & Collection", modifier = Modifier.fillMaxWidth(), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))

        val equippedGift = giftCatalog.firstOrNull { it.id == data?.equippedGiftId }

        if (equippedGift != null) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(Color(0xFFFFF8E8)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(equippedGift.emoji, fontSize = 38.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("🎁 Equipped Gift", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B5B00))
                        Text(equippedGift.name, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Your reward is displayed here!", fontSize = 10.sp, color = Color(0xFF71809A))
                    }
                }
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            RewardHomeCard("🏆", "Achievements", "${data?.achievementCount ?: 0} unlocked", Modifier.weight(1f)) {
                onNavigate("arifa_achievements2")
            }
            RewardHomeCard("🎁", "Gift Store", "${data?.ownedGiftCount ?: 0}/${giftCatalog.size} collected", Modifier.weight(1f)) {
                onNavigate("arifa_gift_store")
            }
            RewardHomeCard("🪙", "Coin Store", "${data?.coins ?: 0} coins", Modifier.weight(1f)) {
                onNavigate("arifa_coin_store")
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("🌟 Keep learning, Arifa!", modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), fontSize = 11.sp, textAlign = TextAlign.Center, color = Color(0xFF7A8798))
    }
}

@Composable
private fun RewardHomeCard(icon: String, title: String, subtitle: String, modifier: Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 25.sp)
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA), textAlign = TextAlign.Center)
            Text(subtitle, fontSize = 9.sp, color = Color(0xFF71809A), textAlign = TextAlign.Center, maxLines = 2)
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