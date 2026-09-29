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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private enum class FunGame(val title: String, val icon: String) {
    TREASURE("Treasure Hunt", "🕵️"),
    ODD_ONE("Odd One Out", "🧩"),
    NUMBER_JUMP("Number Jump", "🎲")
}

private data class FunRound(val prompt: String, val options: List<String>, val correct: Int)

@Composable
fun AbidFunZoneScreen(onBack: () -> Unit) {
    var game by remember { mutableStateOf(FunGame.TREASURE) }
    var round by remember { mutableStateOf(makeFunRound(FunGame.TREASURE)) }
    var selected by remember { mutableIntStateOf(-1) }
    var score by remember { mutableIntStateOf(0) }
    var rounds by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    fun startGame(g: FunGame) {
        game = g
        round = makeFunRound(g)
        selected = -1
        score = 0
        rounds = 0
        finished = false
    }

    fun answer(index: Int) {
        if (selected != -1 || finished) return
        selected = index
        if (index == round.correct) score++
    }

    fun next() {
        if (rounds + 1 >= 10) finished = true
        else {
            rounds++
            round = makeFunRound(game)
            selected = -1
        }
    }

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF5D9), Color(0xFFF3ECFF), Color.White)))
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Back", fontWeight = FontWeight.Bold, fontSize = 17.sp) }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎪 Fun Zone", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
                Text("Play • Think • Have Fun!", fontSize = 12.sp, color = Color(0xFF60758A))
            }
            Spacer(Modifier.width(70.dp))
        }

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            FunGame.values().forEach { g ->
                FilterChip(
                    selected = game == g,
                    onClick = { startGame(g) },
                    label = { Text(g.icon + " " + g.title, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        if (finished) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(3.dp)) {
                Column(Modifier.fillMaxWidth().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉🏆🎉", fontSize = 48.sp)
                    Text("Fun Complete!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
                    Spacer(Modifier.height(8.dp))
                    Text(score.toString() + " / 10", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                    Text(
                        when {
                            score >= 9 -> "🌟 Amazing! Super explorer!"
                            score >= 7 -> "👏 Great job! Keep playing!"
                            else -> "💪 Good try! Let's play again!"
                        },
                        fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { startGame(game) }, modifier = Modifier.fillMaxWidth()) { Text("Play Again 🎮") }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back to Learning") }
                }
            }
        } else {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Text((rounds + 1).toString() + " / 10", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A96A8))
                    Spacer(Modifier.height(10.dp))
                    Text(round.prompt, Modifier.fillMaxWidth(), fontSize = 23.sp, lineHeight = 31.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, color = Color(0xFF26354A))
                    Spacer(Modifier.height(18.dp))
                    round.options.forEachIndexed { index, option ->
                        val bg = when {
                            selected == -1 -> Color(0xFFF3F7FF)
                            index == round.correct -> Color(0xFFE1F6E8)
                            selected == index -> Color(0xFFFFE3E3)
                            else -> Color(0xFFF3F7FF)
                        }
                        Card(onClick = { answer(index) }, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(bg)) {
                            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(('A'.code + index).toChar().toString() + ".", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
                                Spacer(Modifier.width(12.dp))
                                Text(option, Modifier.weight(1f), fontSize = 21.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                if (selected >= 0 && index == round.correct) Text("✓", fontSize = 25.sp, color = Color(0xFF23754A))
                                else if (selected == index) Text("✗", fontSize = 25.sp, color = Color(0xFFC62828))
                            }
                        }
                    }
                }
            }
            if (selected >= 0) {
                Spacer(Modifier.height(12.dp))
                Text(
                    if (selected == round.correct) "🎉 Correct! Great job!" else "💡 Nice try! The correct answer is " + round.options[round.correct],
                    Modifier.fillMaxWidth(), fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center, color = if (selected == round.correct) Color(0xFF23754A) else Color(0xFFC62828)
                )
                Spacer(Modifier.height(10.dp))
                Button(onClick = ::next, modifier = Modifier.fillMaxWidth()) { Text(if (rounds == 9) "See My Score 🏆" else "Next Fun →") }
            }
        }
    }
}

private fun makeFunRound(game: FunGame): FunRound {
    return when (game) {
        FunGame.TREASURE -> {
            val items = listOf("🔴 Red","🔵 Blue","🟡 Yellow","🟢 Green","🔺 Triangle","🟦 Square","⚪ Circle","⭐ Star","🍎 Apple","🍌 Banana","🐱 Cat","🐶 Dog","A","B","C","D","1","2","3","4")
            val answer = items.random()
            val options = (listOf(answer) + items.filter { it != answer }.shuffled().take(3)).shuffled()
            FunRound("🗺️ Find the treasure: " + answer, options, options.indexOf(answer))
        }
        FunGame.ODD_ONE -> {
            val groups = listOf(
                listOf("🍎","🍎","🍎","🍌"), listOf("🐱","🐱","🐶","🐱"),
                listOf("🔴","🔴","🔵","🔴"), listOf("🔺","🔺","🔺","🟦"),
                listOf("⭐","⭐","🌟","⭐")
            )
            val group = groups.random()
            val counts = group.groupingBy { it }.eachCount()
            val answer = counts.minBy { it.value }.key
            val options = group.shuffled()
            FunRound("🕵️ Which one is different?", options, options.indexOf(answer))
        }
        FunGame.NUMBER_JUMP -> {
            val answer = Random.nextInt(1, 11)
            val options = (listOf(answer) + (1..10).filter { it != answer }.shuffled().take(3)).shuffled()
            FunRound("🎲 Jump to number " + answer, options.map { it.toString() }, options.indexOf(answer))
        }
    }
}
