package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.GameDataStore

@Composable
fun ArifaCoinStoreScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val data by store.gameData.collectAsState(initial = null)
    val coins = data?.coins ?: 0

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF8E8), Color.White, Color(0xFFF4F7FF))))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onBack, shape = RoundedCornerShape(14.dp)) { Text("‹ Back") }
            Spacer(Modifier.width(10.dp))
            Text("🪙 COIN STORE", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B5B00))
        }
        Spacer(Modifier.height(18.dp))

        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(Color(0xFFFFE9A8)), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🪙", fontSize = 52.sp)
                Text("$coins", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B5B00))
                Text("Learning Coins", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(14.dp))
        InfoCard("🎯 Earn Coins", "Answer questions correctly. Each Arifa question can reward coins once per calendar day.")
        InfoCard("🎁 Spend Coins", "Use your coins in the Gift Store to permanently unlock gifts and equip your favourites.")
        InfoCard("⭐ Keep Learning", "Different questions can earn rewards on the same day. The same question becomes eligible again tomorrow.")
    }
}

@Composable
private fun InfoCard(icon: String, title: String, text: String) {
    Card(Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.Top) {
            Text(icon, fontSize = 27.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                Text(text, fontSize = 12.sp, color = Color(0xFF71809A))
            }
        }
    }
}
