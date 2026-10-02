package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val memorySymbols = listOf("🍎","🍌","🍊","🍇","🍓","🍉","🍐","🍒","🥝","🥭","🍍","🥕")

private enum class MemoryMode(val pairs: Int, val label: String) {
    THREE(3, "🟢 3 Pairs"),
    FOUR(4, "🔵 4 Pairs"),
    FIVE(5, "🟣 5 Pairs"),
    SIX(6, "🟠 6 Pairs")
}

@Composable
fun AbidMemoryPairsScreen(onBack: () -> Unit) {
    var mode by remember { mutableStateOf<MemoryMode?>(null) }

    if (mode == null) {
        Column(
            Modifier.fillMaxSize().background(Color(0xFFF4F0FF))
                .padding(18.dp).navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) { Text("‹ Home", fontSize = 18.sp) }
                Spacer(Modifier.width(8.dp))
                Text("🧠 Memory Match", fontSize = 27.sp,
                    fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
            }
            Spacer(Modifier.height(28.dp))
            Text("Choose the number of pairs", fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Find two cards with the same picture!", fontSize = 17.sp)
            Spacer(Modifier.height(28.dp))
            MemoryMode.entries.forEach { item ->
                Button(
                    onClick = { mode = item },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp).height(62.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(item.label, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        MemoryGame(pairs = mode!!.pairs, onBack = onBack, onChangeMode = { mode = null })
    }
}

@Composable
private fun MemoryGame(pairs: Int, onBack: () -> Unit, onChangeMode: () -> Unit) {
    var round by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<List<Int>>(emptyList()) }
    var matched by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var moves by remember { mutableIntStateOf(0) }
    var locked by remember { mutableStateOf(false) }

    val cards = remember(pairs, round) {
        (memorySymbols.take(pairs) + memorySymbols.take(pairs)).shuffled()
    }

    LaunchedEffect(selected) {
        if (selected.size == 2) {
            locked = true
            moves++
            if (cards[selected[0]] == cards[selected[1]]) {
                matched = matched + selected
                selected = emptyList()
                locked = false
            } else {
                delay(850)
                selected = emptyList()
                locked = false
            }
        }
    }

    val finished = matched.size == cards.size

    Column(
        Modifier.fillMaxSize().background(Color(0xFFF4F0FF))
            .padding(horizontal = 12.dp).navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Home", fontSize = 17.sp) }
            Spacer(Modifier.weight(1f))
            Text("🧠 Memory Match", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF7043A8))
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onChangeMode) { Text("Change", fontSize = 15.sp) }
        }

        Text("${pairs} Pairs", fontSize = 20.sp, fontWeight = FontWeight.Bold,
            color = Color(0xFF7043A8))
        Text("Pairs found: ${matched.size / 2} / $pairs  •  Moves: ${moves}",
            fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(10.dp))

        if (!finished) {
            cards.mapIndexed { index, value -> index to value }
                .chunked(4).forEach { row ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { (index, value) ->
                            val revealed = index in selected || index in matched
                            Card(
                                onClick = {
                                    if (!locked && index !in matched && index !in selected &&
                                        selected.size < 2) {
                                        selected = selected + index
                                    }
                                },
                                enabled = !locked && index !in matched && index !in selected,
                                modifier = Modifier.weight(1f).height(86.dp).padding(vertical = 4.dp),
                                shape = RoundedCornerShape(17.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (revealed) Color.White else Color(0xFF8E63B7)
                                ),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(if (revealed) value else "❓",
                                        fontSize = if (revealed) 37.sp else 29.sp)
                                }
                            }
                        }
                        repeat(4 - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
        } else {
            Spacer(Modifier.height(20.dp))
            Text("🎉 Fantastic Memory!", fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E8B57))
            Text("You found all $pairs pairs!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("⭐ $moves moves", fontSize = 18.sp)
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = {
                    round++
                    selected = emptyList()
                    matched = emptySet()
                    moves = 0
                    locked = false
                },
                Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("🔄 Play Again", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = onChangeMode,
                Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Choose Another Mode", fontSize = 17.sp)
            }
        }
    }
}
