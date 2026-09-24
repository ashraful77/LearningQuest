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
        "abid_letters" -> AbidLettersScreen { screen = "home" }
        "abid_write_letters" -> AbidWriteLettersScreen { screen = "home" }
        "abid_bengali_letters" -> AbidBengaliLettersScreen { screen = "home" }
        "abid_numbers" -> AbidNumbersScreen { screen = "home" }
        "abid_shapes" -> AbidShapesScreen { screen = "home" }
        "abid_colors" -> AbidColorsScreen { screen = "home" }
        "abid_match_letters" -> AbidMatchLettersScreen { screen = "home" }
        "abid_number_match" -> AbidNumberMatchScreen { screen = "home" }
    }
}

@Composable
private fun HomeContent(onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val dataStore = remember { GameDataStore(context) }
    val gameData by dataStore.gameData.collectAsState(initial = null)
    val data = gameData

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFF4F7FF), Color.White)))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp)
    ) {
        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("LearningQuest", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF172B5C))
                Text("Learn • Play • Grow 🚀", fontSize = 14.sp, color = Color(0xFF71809A))
            }
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFFFF3D4)) {
                Text("🪙 ${data?.coins ?: 0}", modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5B00))
            }
        }

        Spacer(Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(Color(0xFF315FBA), Color(0xFF5636A8))))
                    .padding(22.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("YOUR PROGRESS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDDE7FF))
                            Text("Level ${data?.level ?: 1}", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                        Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.16f)) {
                            Text("⭐", modifier = Modifier.padding(13.dp), fontSize = 24.sp)
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    LinearProgressIndicator(
                        progress = { data?.xpProgress ?: 0f },
                        modifier = Modifier.fillMaxWidth().height(9.dp),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.22f)
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${data?.xp ?: 0} XP", fontSize = 13.sp, color = Color.White)
                        Text("Keep going!", fontSize = 13.sp, color = Color(0xFFE6ECFF))
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MiniStat("🪙", "Coins", "${data?.coins ?: 0}", Modifier.weight(1f))
            MiniStat("🔥", "Streak", "${data?.streak ?: 0} days", Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("YOUR SCORES", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7A8798))
                        Text("Keep building your skills", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF26354A))
                    }
                    Text("📈", fontSize = 24.sp)
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    ScorePill("Math", data?.mathScore ?: 0, Color(0xFF2457A6))
                    ScorePill("English", data?.englishScore ?: 0, Color(0xFF7043A8))
                    ScorePill("Science", data?.scienceScore ?: 0, Color(0xFF23754A))
                    ScorePill("Puzzle", data?.puzzleScore ?: 0, Color(0xFF9A5A00))
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("CHOOSE A CHALLENGE", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF65738A))
        Spacer(Modifier.height(7.dp))

        SubjectCard("➗", "Quick Math", "Numbers & problem solving", Color(0xFFEAF2FF), Color(0xFF2457A6)) { onNavigate("math") }
        SubjectCard("🔤", "English", "Words, grammar & language", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("english") }
        SubjectCard("🔬", "Science", "Explore the world around you", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("science") }
        SubjectCard("🧩", "Puzzles", "Think, solve & discover", Color(0xFFFFF1DE), Color(0xFF9A5A00)) { onNavigate("puzzle") }
        SubjectCard("🌈", "Mixed Quiz", "20 marks • Math + English + Science + Puzzle", Color(0xFFFFE8F5), Color(0xFF6A1B9A)) { onNavigate("mixed") }

        Spacer(Modifier.weight(1f))

        Text(
            "Complete challenges to earn more XP and Coins",
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF8A96A8)
        )
    }
}

@Composable
private fun MiniStat(icon: String, title: String, value: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = Color(0xFFF4F6FA)) {
                Text(icon, modifier = Modifier.padding(9.dp), fontSize = 18.sp)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, fontSize = 11.sp, color = Color(0xFF7A8798))
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF26354A))
            }
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
