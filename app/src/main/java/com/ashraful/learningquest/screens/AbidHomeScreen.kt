package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.AbidProgress
import com.ashraful.learningquest.data.AbidProgressStore

@Composable
fun AbidHomeScreen(
    scrollState: ScrollState,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val progressStore = remember(context) { AbidProgressStore(context) }
    val progress by progressStore.progress.collectAsState(initial = AbidProgress())
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Column(
        Modifier.fillMaxSize().verticalScroll(scrollState)
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF4FF), Color.White, Color(0xFFFFF7DF))))
            .padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("👦 Abid's Learning Zone", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA), textAlign = TextAlign.Center)
        Spacer(Modifier.height(5.dp))
        Text("LKG • Fun Learning", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7043A8))
        Text("Learn • Play • Practise • Grow 🚀", fontSize = 13.sp, color = Color(0xFF60758A), textAlign = TextAlign.Center)

        Spacer(Modifier.height(14.dp))
        OutlinedButton(onClick = { onNavigate("switch_to_arifa") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Text("👧 Switch to Arifa", fontWeight = FontWeight.Bold) }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = { onNavigate("switch_to_anish") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Text("🧑 Switch to Anish", fontWeight = FontWeight.Bold) }

        Spacer(Modifier.height(14.dp))
        AbidProgressCard(progress)
        Spacer(Modifier.height(14.dp))
        DailyMissionCard(progress)
        Spacer(Modifier.height(14.dp))

        Card(onClick = { onNavigate("abid_achievements") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("🏆", fontSize = 30.sp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("My Rewards", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
                        Text("Stars • Badges • Achievements", fontSize = 12.sp, color = Color(0xFF60758A))
                    }
                    Text("›", fontSize = 28.sp, color = Color(0xFFB05A00))
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AbidRewardStat("⭐", progress.stars.toString(), "Stars", Modifier.weight(1f))
                    AbidRewardStat("🏅", progress.level.toString(), "Level", Modifier.weight(1f))
                    AbidRewardStat("🔥", progress.streak.toString(), "Streak", Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        Text("📚 My Learning", modifier = Modifier.fillMaxWidth(), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))
        Text("Choose a subject to learn today", modifier = Modifier.fillMaxWidth(), fontSize = 12.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = Color(0xFF1769AA)
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("🔤 English", fontWeight = FontWeight.ExtraBold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("অ বাংলা", fontWeight = FontWeight.ExtraBold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("🔢 Maths", fontWeight = FontWeight.ExtraBold) }
                    )
                }

                Column(Modifier.fillMaxWidth().padding(12.dp)) {
                    when (selectedTab) {
                        0 -> {
                            LearningCard("🔤", "Letters", "Learn A, B, C and more!", Color(0xFFE8F4FF), Color(0xFF1769AA)) { onNavigate("abid_letters") }
                            LearningCard("🔊", "Letter Sounds", "Hear letters and phonics!", Color(0xFFEAF7FF), Color(0xFF1769AA)) { onNavigate("abid_letter_sounds") }
            LearningCard("🎧", "Listen & Choose", "Hear a lowercase letter and choose it!", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("abid_listen_choose") }
                            LearningCard("🖼️", "Letter → Picture", "Match letters with pictures!", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("abid_picture_match") }
                            LearningCard("✍️", "Write Letters", "Practice writing A, B, C and more!", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("abid_write_letters") }
                            LearningCard("🔗", "Match Letters", "Match small letters with CAPITAL letters!", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("abid_match_letters") }
                        }

                        1 -> {
                            LearningCard("অ", "বাংলা বর্ণমালা", "শিখি অ, আ, ক, খ এবং আরও!", Color(0xFFFFF1D6), Color(0xFFB05A00)) { onNavigate("abid_bengali_letters") }
                            LearningCard("🎯", "বাংলা বর্ণমালা অনুশীলন", "১০টি প্রশ্নে বর্ণ চিনে অনুশীলন করি!", Color(0xFFFFE8EC), Color(0xFFC13A63)) { onNavigate("abid_bengali_practice") }
                            LearningCard("🎧", "শুনে বেছে নিই", "বর্ণ শুনে সঠিক বর্ণটি বেছে নাও • আলাদা সেট", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("abid_bengali_listen_sets") }
                            LearningCard("🖼️", "বাংলা বর্ণ → ছবি", "বর্ণ দেখে সঠিক ছবি খুঁজি!", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("abid_bengali_picture_match") }
                            LearningCard("✍️", "বাংলা বর্ণ লেখা", "আঙুল দিয়ে বর্ণ অনুসরণ করে লিখি!", Color(0xFFFFF1D6), Color(0xFFB05A00)) { onNavigate("abid_bengali_tracing") }
                        }

                        else -> {
                            LearningCard("🔢", "Numbers", "Learn numbers and counting!", Color(0xFFFFF1D6), Color(0xFF9A5A00)) { onNavigate("abid_numbers") }
                            LearningCard("🔢", "Count & Match", "Count objects and choose the number!", Color(0xFFFFE8EC), Color(0xFFC13A63)) { onNavigate("abid_number_match") }
                            LearningCard("➕", "Little Maths", "Practice easy addition!", Color(0xFFFFF3D9), Color(0xFF9A5A00)) { onNavigate("abid_simple_math") }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("🎨 More Learning", modifier = Modifier.fillMaxWidth(), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))
        LearningCard("🔷", "Shapes", "Learn 2D and 3D shapes!", Color(0xFFEAF2FF), Color(0xFF1769AA)) { onNavigate("abid_shapes") }
        LearningCard("🔷", "Shape Match", "Identify the correct shape!", Color(0xFFE8F4FF), Color(0xFF1769AA)) { onNavigate("abid_shape_match") }
        LearningCard("🎨", "Colors", "Learn red, blue, green and more!", Color(0xFFFFE8F5), Color(0xFFB02A7A)) { onNavigate("abid_colors") }
        LearningCard("🎯", "Match Colors", "Find the name of the color!", Color(0xFFEAF7FF), Color(0xFF1769AA)) { onNavigate("abid_color_match") }
        LearningCard("🐾", "Animal Sounds", "Learn animals and their sounds!", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("abid_animals") }
        LearningCard("🍎", "Fruits & Veggies", "Learn healthy foods!", Color(0xFFFFE8EC), Color(0xFFC13A63)) { onNavigate("abid_fruits") }

        Spacer(Modifier.height(6.dp))
        SimpleFeatureCard("🏆", "Little Challenge", "5 fun questions • Letters, numbers, colors & shapes", Color(0xFFEAF2FF), Color(0xFF1769AA)) { onNavigate("abid_challenge") }
        Spacer(Modifier.height(8.dp))
        SimpleFeatureCard("🎪", "Fun Zone", "Treasure Hunt • Odd One Out • Number Jump", Color(0xFFFFF0E6), Color(0xFFB05A00)) { onNavigate("abid_fun_zone") }
        Spacer(Modifier.height(8.dp))
        SimpleFeatureCard("🧠", "Games & Memory", "Memory Match • Fun learning game", Color(0xFFF0EAFF), Color(0xFF7043A8)) { onNavigate("abid_memory2") }

        Spacer(Modifier.height(18.dp))
        Text("🌟 Keep learning, Abid! Every activity makes you smarter!", modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), fontSize = 12.sp, textAlign = TextAlign.Center, color = Color(0xFF7A8798))
    }
}

@Composable
private fun AbidProgressCard(progress: AbidProgress) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("🌟 My Progress", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
                    Text("Level ${progress.level} • ${progress.xp} XP", fontSize = 13.sp, color = Color(0xFF60758A))
                }
                Text("⭐ ${progress.stars}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                AbidProgressPill("🔤", progress.letters, Modifier.weight(1f))
                AbidProgressPill("🔢", progress.numbers, Modifier.weight(1f))
                AbidProgressPill("🎨", progress.colors, Modifier.weight(1f))
            }
            Spacer(Modifier.height(7.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                AbidProgressPill("🔷", progress.shapes, Modifier.weight(1f))
                AbidProgressPill("🌍", progress.world, Modifier.weight(1f))
                AbidProgressPill("🧠", progress.games, Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            Text("🔥 ${progress.streak} day streak • ${progress.todayActivities}/5 today", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF23754A))
        }
    }
}

@Composable
private fun AbidProgressPill(icon: String, value: Int, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().background(Color(0xFFF5F7FA), RoundedCornerShape(14.dp)).padding(vertical = 7.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 17.sp)
        Text("$value%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AbidRewardStat(icon: String, value: String, label: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(14.dp), color = Color(0xFFFFF7E8)) {
        Column(Modifier.padding(vertical = 9.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 20.sp)
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))
            Text(label, fontSize = 10.sp, color = Color(0xFF60758A))
        }
    }
}

@Composable
private fun DailyMissionCard(progress: AbidProgress) {
    val completed = progress.todayActivities.coerceAtMost(3)
    val missionDone = progress.dailyBonusClaimed
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = if (missionDone) Color(0xFFE8F8EF) else Color(0xFFFFF4D8)), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (missionDone) "🎉" else "🎯", fontSize = 30.sp)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(if (missionDone) "Daily Mission Complete!" else "Daily Learning Mission", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = if (missionDone) Color(0xFF23754A) else Color(0xFF9A5A00))
                    Text(if (missionDone) "Great job! +3 ⭐ bonus earned" else "Complete 3 activities today", fontSize = 12.sp, color = Color(0xFF60758A))
                }
                Text("$completed/3", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            }
            if (!missionDone) {
                Spacer(Modifier.height(9.dp))
                LinearProgressIndicator(progress = { completed / 3f }, modifier = Modifier.fillMaxWidth().height(8.dp), color = Color(0xFFFFA000), trackColor = Color.White)
                Spacer(Modifier.height(6.dp))
                Text("🎁 Finish 3 activities to earn +3 ⭐ and +10 XP!", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5B00))
            }
        }
    }
}

@Composable
private fun AbidLearningSection(icon: String, title: String, subtitle: String, count: String, accent: Color, expanded: Boolean, onToggle: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Card(onClick = onToggle, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(Modifier.fillMaxWidth().padding(15.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(15.dp), color = accent.copy(alpha = 0.12f)) {
                    Text(icon, fontSize = 30.sp, modifier = Modifier.padding(9.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = accent)
                    Text(subtitle, fontSize = 11.sp, color = Color(0xFF60758A))
                    Text(count, fontSize = 10.sp, color = Color(0xFF8A96A8))
                }
                Text(if (expanded) "⌃" else "›", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = accent)
            }
            if (expanded) {
                Spacer(Modifier.height(6.dp))
                content()
            }
        }
    }
}

@Composable
private fun SimpleFeatureCard(icon: String, title: String, subtitle: String, background: Color, accent: Color, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(background), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 34.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = accent)
                Text(subtitle, fontSize = 12.sp, color = Color(0xFF60758A))
            }
            Text("›", fontSize = 28.sp, color = accent)
        }
    }
}

@Composable
private fun LearningCard(icon: String, title: String, subtitle: String, background: Color, textColor: Color, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(background), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.fillMaxWidth().padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 30.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = textColor)
                Text(subtitle, fontSize = 11.sp, color = Color(0xFF60758A))
            }
            Text("›", fontSize = 26.sp, color = textColor)
        }
    }
}
