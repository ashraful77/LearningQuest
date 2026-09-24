package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbidHomeScreen(onNavigate: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        Modifier.fillMaxSize().verticalScroll(scrollState)
            .background(Brush.verticalGradient(listOf(Color(0xFFEAF7FF), Color.White, Color(0xFFFFF7DF))))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("👦 Hi Abid!", fontSize = 34.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF1769AA))
        Text("Let's learn and play! 🚀", fontSize = 17.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(18.dp))

        SectionHeader("🔤", "Language & Letters", Color(0xFF1769AA))
        LearningCard("🔤", "Letters", "Learn A, B, C and more!", Color(0xFFE8F4FF), Color(0xFF1769AA)) { onNavigate("abid_letters") }
        LearningCard("🔊", "Letter Sounds", "Hear letters and phonics!", Color(0xFFEAF7FF), Color(0xFF1769AA)) { onNavigate("abid_letter_sounds") }
        LearningCard("🖼️", "Letter → Picture", "Match letters with pictures!", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("abid_picture_match") }
        LearningCard("✍️", "Write Letters", "Practice writing A, B, C and more!", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("abid_write_letters") }
        LearningCard("অ", "বাংলা বর্ণমালা", "শিখি অ, আ, ক, খ এবং আরও!", Color(0xFFFFF1D6), Color(0xFFB05A00)) { onNavigate("abid_bengali_letters") }
        LearningCard("🔗", "Match Letters", "Match small letters with CAPITAL letters!", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("abid_match_letters") }

        SectionHeader("🔢", "Numbers & Maths", Color(0xFF9A5A00))
        LearningCard("🔢", "Numbers", "Learn numbers and counting!", Color(0xFFFFF1D6), Color(0xFF9A5A00)) { onNavigate("abid_numbers") }
        LearningCard("🔢", "Count & Match", "Count objects and choose the number!", Color(0xFFFFE8EC), Color(0xFFC13A63)) { onNavigate("abid_number_match") }
        LearningCard("➕", "Little Maths", "Practice easy addition!", Color(0xFFFFF3D9), Color(0xFF9A5A00)) { onNavigate("abid_simple_math") }

        SectionHeader("🎨", "Colors & Shapes", Color(0xFFB02A7A))
        LearningCard("🔷", "Shapes", "Learn 2D and 3D shapes!", Color(0xFFEAF2FF), Color(0xFF1769AA)) { onNavigate("abid_shapes") }
        LearningCard("🔷", "Shape Match", "Identify the correct shape!", Color(0xFFE8F4FF), Color(0xFF1769AA)) { onNavigate("abid_shape_match") }
        LearningCard("🎨", "Colors", "Learn red, blue, green and more!", Color(0xFFFFE8F5), Color(0xFFB02A7A)) { onNavigate("abid_colors") }
        LearningCard("🎯", "Match Colors", "Find the name of the color!", Color(0xFFEAF7FF), Color(0xFF1769AA)) { onNavigate("abid_color_match") }

        SectionHeader("🌍", "Explore the World", Color(0xFF23754A))
        LearningCard("🐾", "Animal Sounds", "Learn animals and their sounds!", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("abid_animals") }
        LearningCard("🍎", "Fruits & Veggies", "Learn healthy foods!", Color(0xFFFFE8EC), Color(0xFFC13A63)) { onNavigate("abid_fruits") }

        SectionHeader("🧠", "Games & Memory", Color(0xFF7043A8))
        LearningCard("🧠", "Memory Match", "Remember and find the same fruit!", Color(0xFFF0EAFF), Color(0xFF7043A8)) { onNavigate("abid_memory2") }
        LearningCard("🏆", "My Achievements", "See your learning stars!", Color(0xFFFFF1D6), Color(0xFFB05A00)) { onNavigate("abid_achievements") }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun SectionHeader(icon: String, title: String, color: Color) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 24.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            title,
            fontSize = 21.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
            color = color
        )
        Spacer(Modifier.width(10.dp))
        HorizontalDivider(Modifier.weight(1f), color = color.copy(alpha = 0.25f))
    }
}

@Composable
private fun LearningCard(
    icon: String,
    title: String,
    subtitle: String,
    background: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 42.sp)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 25.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = textColor)
                Text(subtitle, fontSize = 14.sp, color = Color(0xFF60758A))
            }
            Text("›", fontSize = 34.sp, color = textColor)
        }
    }
}
