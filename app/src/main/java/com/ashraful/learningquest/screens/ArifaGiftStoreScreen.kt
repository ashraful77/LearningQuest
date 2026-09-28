package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.ashraful.learningquest.data.Gift
import com.ashraful.learningquest.data.GiftCategory
import com.ashraful.learningquest.data.giftCatalog
import kotlinx.coroutines.launch

@Composable
fun ArifaGiftStoreScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val scope = rememberCoroutineScope()
    val data by store.gameData.collectAsState(initial = null)
    val owned by store.ownedGiftIds().collectAsState(initial = emptySet())
    val equipped by store.equippedGiftId.collectAsState(initial = null)

    var selectedCategory by rememberSaveable { mutableStateOf(GiftCategory.CHARACTERS.name) }
    var purchaseGift by remember { mutableStateOf<Gift?>(null) }
    var message by remember { mutableStateOf<String?>(null) }

    val category = GiftCategory.valueOf(selectedCategory)
    val visibleGifts = giftCatalog.filter { it.category == category }
    val coins = data?.coins ?: 0

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFFFF8E8), Color(0xFFF4F7FF), Color.White))
        )
    ) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(onClick = onBack, shape = RoundedCornerShape(15.dp)) {
                    Text("‹ Back", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("🎁 GIFT STORE", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    Text("Spend your learning coins!", fontSize = 12.sp, color = Color(0xFF71809A))
                }
                Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFFFE9A8)) {
                    Text("🪙 $coins", Modifier.padding(horizontal = 11.dp, vertical = 8.dp), fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B5B00))
                }
            }

            Spacer(Modifier.height(14.dp))

            Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), color = Color.White, tonalElevation = 2.dp) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("MY COLLECTION", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7A8798))
                            Text("${owned.size} / ${giftCatalog.size} gifts unlocked", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                        }
                        if (equipped != null) {
                            val equippedGift = giftCatalog.firstOrNull { it.id == equipped }
                            if (equippedGift != null) {
                                Text("${equippedGift.emoji} Equipped", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(progress = { owned.size / giftCatalog.size.toFloat() }, Modifier.fillMaxWidth().height(7.dp))
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                GiftCategory.values().forEach { item ->
                    FilterChip(
                        selected = item == category,
                        onClick = { selectedCategory = item.name },
                        label = { Text(item.label.substringAfter(" ").trim(), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            visibleGifts.chunked(2).forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { gift ->
                        GiftCard(
                            gift = gift,
                            owned = gift.id in owned,
                            equipped = gift.id == equipped,
                            canAfford = coins >= gift.price,
                            modifier = Modifier.weight(1f),
                            onBuy = { purchaseGift = gift },
                            onEquip = {
                                scope.launch {
                                    store.equipGift(gift.id)
                                    message = "${gift.emoji} ${gift.name} equipped!"
                                }
                            }
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
            }

            Surface(Modifier.fillMaxWidth().padding(top = 4.dp), shape = RoundedCornerShape(18.dp), color = Color(0xFFEAF2FF)) {
                Text(
                    "🌟 Keep learning to earn more coins. Every gift is permanent once unlocked!",
                    Modifier.padding(13.dp), fontSize = 12.sp, color = Color(0xFF315FBA),
                    textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        if (message != null) {
            Snackbar(
                modifier = Modifier.align(Alignment.BottomCenter).padding(14.dp),
                action = { TextButton(onClick = { message = null }) { Text("OK") } }
            ) { Text(message.orEmpty()) }
        }
    }

    purchaseGift?.let { gift ->
        AlertDialog(
            onDismissRequest = { purchaseGift = null },
            title = { Text("${gift.emoji} ${gift.name}", fontWeight = FontWeight.ExtraBold) },
            text = {
                Column {
                    Text(gift.description)
                    Spacer(Modifier.height(10.dp))
                    Text("🪙 ${gift.price} Coins", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B5B00))
                    Text("Your balance: 🪙 $coins", fontSize = 13.sp, color = Color(0xFF71809A))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val success = store.purchaseGift(gift)
                            purchaseGift = null
                            message = if (success) "${gift.emoji} ${gift.name} unlocked!" else "🔒 Not enough coins for ${gift.name}."
                        }
                    },
                    enabled = coins >= gift.price
                ) { Text(if (coins >= gift.price) "BUY NOW" else "NOT ENOUGH") }
            },
            dismissButton = { TextButton(onClick = { purchaseGift = null }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun GiftCard(
    gift: Gift,
    owned: Boolean,
    equipped: Boolean,
    canAfford: Boolean,
    modifier: Modifier,
    onBuy: () -> Unit,
    onEquip: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (owned) Color(0xFFEAF8EF) else Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(gift.emoji, fontSize = 46.sp)
            Text(gift.name, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            Text(gift.description, fontSize = 10.sp, color = Color(0xFF71809A), textAlign = TextAlign.Center, minLines = 2)
            Spacer(Modifier.height(7.dp))

            if (owned) {
                if (equipped) {
                    Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFF20B957)) {
                        Text("✓ EQUIPPED", Modifier.padding(horizontal = 10.dp, vertical = 6.dp), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                } else {
                    OutlinedButton(
                        onClick = onEquip,
                        modifier = Modifier.fillMaxWidth().height(38.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("USE", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold) }
                }
            } else {
                Text("🪙 ${gift.price}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF8B5B00))
                Spacer(Modifier.height(5.dp))
                Button(
                    onClick = onBuy,
                    enabled = canAfford,
                    modifier = Modifier.fillMaxWidth().height(38.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text(if (canAfford) "BUY" else "LOCKED", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold) }
            }
        }
    }
}
