package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.GameDataStore

@Composable
fun HomeScreen() {
    var profile by remember { mutableStateOf<String?>(null) }
    var screen by remember { mutableStateOf("home") }

    BackHandler(enabled = profile != null && screen != "home") {
        screen = "home"
    }

    if (profile == null) {
        ProfileSelectionScreen { selected ->
            profile = selected
            screen = "home"
        }
        return
    }

    when (screen) {
        "home" -> if (profile == "arifa") {
            HomeContent { screen = it }
        } else {
            AbidHomeScreen { screen = it }
        }
        "math" -> MathScreen { screen = "home" }
        "english" -> EnglishScreen { screen = "home" }
        "science" -> ScienceScreen { screen = "home" }
        "puzzle" -> PuzzleScreen { screen = "home" }
        "mixed" -> MixedQuizScreen { screen = "home" }
        "arifa_brain" -> ArifaBrainGamesScreen { screen = "home" }
        "arifa_reading" -> ArifaReadingAdventureScreen { screen = "home" }
        "arifa_writing" -> ArifaWritingPracticeScreen { screen = "home" }
        "arifa_advanced_math" -> ArifaAdvancedMathScreen { screen = "home" }
        "arifa_world" -> ArifaWorldExplorerScreen { screen = "home" }
        "arifa_visual" -> ArifaVisualPuzzlesScreen { screen = "home" }
        "arifa_speaking" -> ArifaEnglishSpeakingScreen { screen = "home" }
        "arifa_daily" -> ArifaDailyChallengeScreen { screen = "home" }
        "arifa_achievements2" -> ArifaAchievements2Screen { screen = "home" }
        "arifa_path" -> ArifaLearningPathScreen { screen = "home" }
        "abid_letters" -> AbidLettersScreen { screen = "home" }
        "abid_write_letters" -> AbidWriteLettersScreen { screen = "home" }
        "abid_bengali_letters" -> AbidBengaliLettersScreen { screen = "home" }
        "abid_numbers" -> AbidNumbersScreen { screen = "home" }
        "abid_shapes" -> AbidShapesScreen { screen = "home" }
        "abid_colors" -> AbidColorsScreen { screen = "home" }
        "abid_color_match" -> AbidColorMatchScreen { screen = "home" }
        "abid_memory" -> AbidMemoryPairsScreen { screen = "home" }
        "abid_letter_sounds" -> AbidLetterSoundsScreen { screen = "home" }
        "abid_picture_match" -> AbidLetterPictureMatchScreen { screen = "home" }
        "abid_simple_math" -> AbidSimpleMathScreen { screen = "home" }
        "abid_shape_match" -> AbidShapeMatchScreen { screen = "home" }
        "abid_animals" -> AbidAnimalSoundsScreen { screen = "home" }
        "abid_fruits" -> AbidFruitsScreen { screen = "home" }
        "abid_memory2" -> AbidMemoryGameScreen { screen = "home" }
        "abid_achievements" -> AbidAchievementsScreen { screen = "home" }
        "abid_match_letters" -> AbidMatchLettersScreen { screen = "home" }
        "abid_number_match" -> AbidNumberMatchScreen { screen = "home" }
    }
}

@Composable
private fun HomeContent(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val data by store.gameData.collectAsState(initial = null)
    var showLevel2 by rememberSaveable { mutableStateOf(false) }

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

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF4F7FF), Color.White)))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp)
    ) {
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Good to see you, Arifa! 👋", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF172B5C))
                Text("Ready for today's learning adventure?", fontSize = 13.sp, color = Color(0xFF71809A))
            }
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFFF3D4)) {
                Text("🪙 ${data?.coins ?: 0}", Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5B00))
            }
        }

        Spacer(Modifier.height(14.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(Color.Transparent)) {
            Box(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF315FBA), Color(0xFF5636A8)))).padding(20.dp)) {
                Column {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("LEVEL ${data?.level ?: 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDDE7FF))
                            Text("${data?.xp ?: 0} XP", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                        Text("🔥 ${data?.streak ?: 0} day streak", fontSize = 13.sp, color = Color.White)
                    }
                    Spacer(Modifier.height(14.dp))
                    LinearProgressIndicator(
                        progress = { data?.xpProgress ?: 0f },
                        Modifier.fillMaxWidth().height(8.dp),
                        color = Color.White, trackColor = Color.White.copy(alpha = .22f)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text("${data?.xpInCurrentLevel ?: 0} / 100 XP to next level", fontSize = 12.sp, color = Color(0xFFE6ECFF))
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(Color(0xFFFFF8E8))) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🎯", fontSize = 28.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("TODAY'S GOAL", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9A6800))
                    Text("Complete 3 learning challenges", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5F4700))
                    Text("${data?.todayProgress ?: 0} / 3 completed", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B6500))
                    LinearProgressIndicator(
                        progress = { (data?.todayProgress ?: 0) / 3f },
                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp).height(6.dp),
                        color = Color(0xFFE0A400),
                        trackColor = Color(0xFFFFE9A8)
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Text("CONTINUE LEARNING", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF65738A))
        Spacer(Modifier.height(6.dp))
        SubjectCard(continueInfo.first, continueInfo.second, continueInfo.third, Color(0xFFEAF2FF), Color(0xFF315FBA)) { onNavigate(weakest) }

        Spacer(Modifier.height(14.dp))
        Text("CORE SUBJECTS", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF65738A))
        Spacer(Modifier.height(5.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompactSubject("➗", "Math", Color(0xFFEAF2FF), Color(0xFF2457A6), Modifier.weight(1f)) { onNavigate("math") }
            CompactSubject("🔤", "English", Color(0xFFF3ECFF), Color(0xFF7043A8), Modifier.weight(1f)) { onNavigate("english") }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CompactSubject("🔬", "Science", Color(0xFFE8F8EF), Color(0xFF23754A), Modifier.weight(1f)) { onNavigate("science") }
            CompactSubject("🧩", "Puzzle", Color(0xFFFFF1DE), Color(0xFF9A5A00), Modifier.weight(1f)) { onNavigate("puzzle") }
        }

        Spacer(Modifier.height(14.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
            Column(Modifier.padding(15.dp)) {
                Text("YOUR SKILLS", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7A8798))
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
        SubjectCard("🌈", "Mixed Quiz", "20 questions • all core subjects", Color(0xFFFFE8F5), Color(0xFF6A1B9A)) { onNavigate("mixed") }

        Spacer(Modifier.height(14.dp))
        Card(onClick = { showLevel2 = !showLevel2 }, Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color(0xFFF1F5FF))) {
            Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("🚀", fontSize = 26.sp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("LEVEL 2 • MORE ADVENTURES", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    Text("Brain, reading, writing, world & more", fontSize = 12.sp, color = Color(0xFF71809A))
                }
                Text(if (showLevel2) "⌃" else "⌄", fontSize = 22.sp, color = Color(0xFF315FBA))
            }
        }

        if (showLevel2) {
            Spacer(Modifier.height(5.dp))
            SubjectCard("🧠", "Brain Games", "Logic, patterns & thinking", Color(0xFFF3E5FF), Color(0xFF6A1B9A)) { onNavigate("arifa_brain") }
            SubjectCard("📖", "Reading Adventure", "Stories & comprehension", Color(0xFFE3F2FD), Color(0xFF1565C0)) { onNavigate("arifa_reading") }
            SubjectCard("✍️", "Writing Practice", "Spelling & sentences", Color(0xFFE0F7F4), Color(0xFF00897B)) { onNavigate("arifa_writing") }
            SubjectCard("🔢", "Advanced Maths", "Multiplication, fractions & money", Color(0xFFEAF2FF), Color(0xFF2457A6)) { onNavigate("arifa_advanced_math") }
            SubjectCard("🌍", "World Explorer", "India, science & our world", Color(0xFFFFF1DE), Color(0xFFE67E22)) { onNavigate("arifa_world") }
            SubjectCard("🧩", "Visual Puzzles", "Patterns & sequences", Color(0xFFFFF1DE), Color(0xFF9A5A00)) { onNavigate("arifa_visual") }
            SubjectCard("🗣️", "English Speaking", "Listen and practise", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("arifa_speaking") }
            SubjectCard("🔥", "Daily Challenge", "10 daily questions", Color(0xFFFFEEDB), Color(0xFFD35400)) { onNavigate("arifa_daily") }
            SubjectCard("🏆", "Achievements 2.0", "Unlock milestones", Color(0xFFFFF5D9), Color(0xFFB77900)) { onNavigate("arifa_achievements2") }
            SubjectCard("🛤️", "Learning Path", "Your learning journey", Color(0xFFE8F8EF), Color(0xFF247A57)) { onNavigate("arifa_path") }
        }

        Spacer(Modifier.height(18.dp))
        Text("Learn • Play • Grow 🚀", Modifier.fillMaxWidth().padding(bottom = 12.dp), fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF8A96A8))
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
