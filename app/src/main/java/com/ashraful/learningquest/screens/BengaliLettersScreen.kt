package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbidBengaliLettersScreen(onBack: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }

    val letters = listOf(
        "অ", "আ", "ই", "ঈ", "উ", "ঊ", "ঋ", "এ", "ঐ", "ও", "ঔ",
        "ক", "খ", "গ", "ঘ", "ঙ", "চ", "ছ", "জ", "ঝ", "ঞ",
        "ট", "ঠ", "ড", "ঢ", "ণ", "ত", "থ", "দ", "ধ", "ন",
        "প", "ফ", "ব", "ভ", "ম", "য", "র", "ল", "শ", "ষ", "স", "হ",
        "ড়", "ঢ়", "য়"
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF4E5), Color.White, Color(0xFFEAF7FF))))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text("‹ Home", fontSize = 19.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
            Text(
                "🔤 বাংলা বর্ণমালা",
                fontSize = 27.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                color = Color(0xFFB05A00)
            )
        }

        Spacer(Modifier.height(28.dp))
        Text("${index + 1} / ${letters.size}", fontSize = 18.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(18.dp))

        Card(
            Modifier.fillMaxWidth().height(340.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1D6)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("বাংলা", fontSize = 30.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color(0xFFB05A00))
                Spacer(Modifier.height(8.dp))
                Text(
                    letters[index],
                    fontSize = 130.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                    color = Color(0xFF1769AA)
                )
            }
        }

        Spacer(Modifier.height(28.dp))
        Button(
            onClick = { index = (index + 1) % letters.size },
            modifier = Modifier.fillMaxWidth().height(62.dp)
        ) {
            Text(if (index == letters.lastIndex) "🔄 আবার শুরু" else "পরের বর্ণ →", fontSize = 21.sp)
        }
    }
}
