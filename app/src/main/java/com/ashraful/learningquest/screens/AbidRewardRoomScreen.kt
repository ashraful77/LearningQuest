package com.ashraful.learningquest.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.AbidRewardItem
import com.ashraful.learningquest.data.AbidRewardProgress
import com.ashraful.learningquest.data.AbidRewardStore
import com.ashraful.learningquest.data.abidRewardCatalog
import kotlinx.coroutines.launch

@Composable
fun AbidRewardRoomScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember(context) { AbidRewardStore(context) }
    val progress by store.progress.collectAsState(initial = AbidRewardProgress())
    var shop by rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFEAF4FF), Color.White, Color(0xFFFFF4D8)))).padding(14.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("🎁 My Rewards", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA), modifier = Modifier.weight(1f))
            Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFE7F1FF)) {
                Text("🔵 " + progress.chips, modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp), fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = !shop, onClick = { shop = false }, label = { Text("🏠 Display Board") }, modifier = Modifier.weight(1f))
            FilterChip(selected = shop, onClick = { shop = true }, label = { Text("🛍️ Store") }, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))

        if (shop) {
            Text("🛍️ Reward Store", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
            Text("Spend blue coin chips to unlock gifts!", fontSize = 12.sp, color = Color(0xFF60758A))
            Spacer(Modifier.height(8.dp))
            abidRewardCatalog.forEach { item -> StoreItem(item, progress, store) }
        } else {
            Text("🏠 Abid's Display Board", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
            Text("Equipped rewards gently animate here.", fontSize = 12.sp, color = Color(0xFF60758A))
            Spacer(Modifier.height(8.dp))
            val equippedItems = abidRewardCatalog.filter { it.id in progress.equipped }
            if (equippedItems.isEmpty()) {
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color.White), shape = RoundedCornerShape(22.dp)) {
                    Column(Modifier.fillMaxWidth().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎁", fontSize = 54.sp)
                        Text("Buy a reward and equip it!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { shop = true }) { Text("Open Reward Store 🛍️") }
                    }
                }
            } else {
                equippedItems.chunked(2).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { item -> AnimatedReward(item, Modifier.weight(1f)) }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("🎒 My Collection: " + progress.owned.size + " / " + abidRewardCatalog.size, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun StoreItem(item: AbidRewardItem, progress: AbidRewardProgress, store: AbidRewardStore) {
    val scope = rememberCoroutineScope()
    val owned = item.id in progress.owned
    val equipped = item.id in progress.equipped
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(Color.White), shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(item.icon, fontSize = 34.sp)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                Text(if (owned) "Owned" else "🔵 " + item.price + " blue chips", fontSize = 11.sp, color = Color(0xFF60758A))
            }
            if (owned) {
                OutlinedButton(onClick = { scope.launch { store.toggleEquipped(item) } }) { Text(if (equipped) "Unequip" else "Equip") }
            } else {
                Button(enabled = progress.chips >= item.price, onClick = { scope.launch { store.buy(item) } }) { Text("BUY") }
            }
        }
    }
}

@Composable
private fun AnimatedReward(item: AbidRewardItem, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "reward_" + item.id)
    val phase by transition.animateFloat(-1f, 1f, infiniteRepeatable(tween(1400), RepeatMode.Reverse), label = "phase_" + item.id)
    val rotation = if (item.kind == "toy") phase * 4f else phase * 2f
    val y = if (item.kind == "magic") phase * 8f else phase * 4f
    Card(modifier = modifier, colors = CardDefaults.cardColors(Color.White), shape = RoundedCornerShape(22.dp)) {
        Column(Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(item.icon, fontSize = 64.sp, modifier = Modifier.graphicsLayer(rotationZ = rotation, translationY = y))
            Text(item.name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            Text("Equipped", fontSize = 10.sp, color = Color(0xFF23754A))
        }
    }
}
