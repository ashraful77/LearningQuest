package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnishHomeScreen(onNavigate: (String) -> Unit) {
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
            "📚 অনিশের লার্নিং জোন",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF315FBA),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(5.dp))
        Text(
            "শ্রেণি ৫ • বাংলা মাধ্যম",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7043A8)
        )
        Text(
            "পড়ি • অনুশীলন করি • পরীক্ষা দিই • এগিয়ে যাই 🚀",
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
            Text("👧 আরিফার স্ক্রিনে যাও", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onNavigate("switch_to_abid") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("👦 আবিদের স্ক্রিনে যাও", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(18.dp))

        Text(
            "📖 আমার বিষয়সমূহ",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF26354A)
        )

        Spacer(Modifier.height(8.dp))

        AnishSubjectCard("বাংলা", "পাঠ, ব্যাকরণ, শব্দার্থ ও অনুশীলন", "📖", Color(0xFFFFEAF4), Color(0xFFB12A73)) { onNavigate("anish_bengali") }
        AnishSubjectCard("গণিত", "হিসাব, ভগ্নাংশ, জ্যামিতি ও সমস্যা সমাধান", "➗", Color(0xFFEAF2FF), Color(0xFF2457A6)) { onNavigate("anish_math") }
        AnishSubjectCard("বিজ্ঞান", "জীবন, পদার্থ, শক্তি, পরিবেশ ও পরীক্ষা", "🔬", Color(0xFFE8F8EF), Color(0xFF23754A)) { onNavigate("anish_science") }
        AnishSubjectCard("ইতিহাস ও ভূগোল", "ভারত, পৃথিবী, মানচিত্র ও গুরুত্বপূর্ণ ঘটনা", "🌍", Color(0xFFFFF1DE), Color(0xFF9A5A00)) { onNavigate("anish_history") }
        AnishSubjectCard("ইংরেজি", "শব্দভাণ্ডার, ব্যাকরণ, পাঠ ও অনুশীলন", "🔤", Color(0xFFF3ECFF), Color(0xFF7043A8)) { onNavigate("anish_english") }
        AnishSubjectCard("সাধারণ জ্ঞান", "দেশ, রাজ্য, বিজ্ঞান ও দৈনন্দিন জ্ঞান", "🧠", Color(0xFFFFF8E8), Color(0xFF8B6500)) { onNavigate("anish_gk") }

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FF)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.fillMaxWidth().padding(17.dp)) {
                Text(
                    "📝 মডেল টেস্ট",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF315FBA)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "বিষয়ভিত্তিক ও মিশ্র প্রশ্নের পরীক্ষা এখানে যোগ করা হবে।",
                    fontSize = 13.sp,
                    color = Color(0xFF60758A)
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        Text(
            "অনিশের জন্য সব শেখার উপকরণ ধাপে ধাপে যোগ করা হবে। 🌟",
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF7A8798)
        )
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
