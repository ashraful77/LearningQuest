package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.AbidRewardStore
import kotlin.random.Random
import kotlinx.coroutines.delay

private enum class FunGame(val title: String, val icon: String) {
    TREASURE("Treasure Hunt", "🕵️"),
    ODD_ONE("Odd One Out", "🧩"),
    NUMBER_JUMP("Number Jump", "🎲"),
    LETTER_HUNT("Letter Hunt", "🔤"),
    PATTERN("Complete Pattern", "🧩"),
    NOT_BELONG("Doesn't Belong", "🚫"),
    FAST_FINGER("Fast Finger", "⚡")
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
    var showContents by rememberSaveable { mutableStateOf(true) }
    var timeLeft by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val rewardStore = remember(context) { AbidRewardStore(context) }
    var chipAwardTrigger by remember { mutableIntStateOf(0) }

    fun startGame(g: FunGame) {
        game = g
        round = makeFunRound(g)
        selected = -1
        score = 0
        rounds = 0
        finished = false
        showContents = false
        timeLeft = if (g == FunGame.FAST_FINGER) 5 else 0
    }

    fun answer(index: Int) {
        if (selected != -1 || finished) return
        selected = index
        if (index == round.correct) {
            score++
            chipAwardTrigger++
        }
    }

    fun next() {
        if (rounds + 1 >= 20) finished = true
        else {
            rounds++
            round = makeFunRound(game)
            selected = -1
            timeLeft = if (game == FunGame.FAST_FINGER) 5 else 0
        }
    }

    LaunchedEffect(chipAwardTrigger) {
        if (chipAwardTrigger > 0) rewardStore.addChip()
    }

    LaunchedEffect(game, rounds, finished) {
        if (game == FunGame.FAST_FINGER && !finished && selected == -1) {
            for (t in 5 downTo 1) {
                timeLeft = t
                delay(1000)
                if (selected != -1) return@LaunchedEffect
            }
            if (selected == -1) {
                timeLeft = 0
                selected = -2
            }
        }
    }

    Column(
        Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF5D9), Color(0xFFF3ECFF), Color.White)))
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { if (showContents) onBack() else showContents = true }) { Text(if (showContents) "‹ Back" else "‹ Games", fontWeight = FontWeight.Bold, fontSize = 17.sp) }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎪 Fun Zone", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
                Text("Play • Think • Have Fun!", fontSize = 12.sp, color = Color(0xFF60758A))
            }
            Spacer(Modifier.width(70.dp))
        }

        Spacer(Modifier.height(12.dp))

        if (showContents) {
            Text(
                "📚 Choose a game to play",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF7043A8),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(Modifier.height(8.dp))
            FunGame.values().forEach { g ->
                Card(
                    onClick = { startGame(g) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(g.icon, fontSize = 34.sp)
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(g.title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF26354A))
                            Text(
                                when (g) {
                                    FunGame.TREASURE -> "Find the correct treasure."
                                    FunGame.ODD_ONE -> "Find what is different."
                                    FunGame.NUMBER_JUMP -> "Find the requested number."
                                    FunGame.LETTER_HUNT -> "Find the requested letter."
                                    FunGame.PATTERN -> "Complete the missing pattern."
                                    FunGame.NOT_BELONG -> "Find the item that doesn't belong."
                                    FunGame.FAST_FINGER -> "Tap the correct answer quickly."
                                },
                                fontSize = 12.sp,
                                color = Color(0xFF60758A)
                            )
                        }
                        Text("›", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7043A8))
                    }
                }
            }
        } else if (finished) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(3.dp)) {
                Column(Modifier.fillMaxWidth().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉🏆🎉", fontSize = 48.sp)
                    Text("Fun Complete!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7043A8))
                    Spacer(Modifier.height(8.dp))
                    Text(score.toString() + " / 20", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF315FBA))
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
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text((rounds + 1).toString() + " / 20", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A96A8))
                        if (game == FunGame.FAST_FINGER && selected == -1) Text("⏱️ " + timeLeft + "s", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = if (timeLeft <= 2) Color(0xFFC62828) else Color(0xFF7043A8))
                    }
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
                                if (selected != -1 && index == round.correct) Text("✓", fontSize = 25.sp, color = Color(0xFF23754A))
                                else if (selected != -1 && selected == index) Text("✗", fontSize = 25.sp, color = Color(0xFFC62828))
                            }
                        }
                    }
                }
            }
            if (selected != -1) {
                Spacer(Modifier.height(12.dp))
                Text(
                    when {
                        selected == round.correct -> "🎉 Correct! +1 🔵 Blue Coin!"
                        selected == -2 -> "⏰ Time's up! The correct answer is " + round.options[round.correct]
                        else -> "💡 Nice try! The correct answer is " + round.options[round.correct]
                    },
                    Modifier.fillMaxWidth(), fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center, color = if (selected == round.correct) Color(0xFF23754A) else Color(0xFFC62828)
                )
                Spacer(Modifier.height(10.dp))
                Button(onClick = ::next, modifier = Modifier.fillMaxWidth()) { Text(if (rounds == 19) "See My Score 🏆" else "Next Fun →") }
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
            val rounds = listOf(
                Pair(listOf("🍎","🍎","🍎","🍌"), "🍌"),
                Pair(listOf("🐱","🐱","🐶","🐱"), "🐶"),
                Pair(listOf("🔴","🔴","🔵","🔴"), "🔵"),
                Pair(listOf("🔺","🔺","🔺","🟦"), "🟦"),
                Pair(listOf("⭐","⭐","🌟","⭐"), "🌟"),
                Pair(listOf("🍎","🍎","🍎","🍊"), "🍊"),
                Pair(listOf("🐶","🐶","🐱","🐶"), "🐱"),
                Pair(listOf("🟦","🟦","🟩","🟦"), "🟩"),
                Pair(listOf("❤️","❤️","💙","❤️"), "💙"),
                Pair(listOf("1","1","2","1"), "2"),
                Pair(listOf("A","A","B","A"), "B"),
                Pair(listOf("🍌","🍌","🍓","🍌"), "🍓"),
                Pair(listOf("🚗","🚗","🚌","🚗"), "🚌")
            )
            val data = rounds.random()
            val options = data.first.shuffled()
            FunRound("🕵️ Which one is different?", options, options.indexOf(data.second))
        }
        FunGame.NUMBER_JUMP -> {
            val answer = Random.nextInt(1, 11)
            val options = (listOf(answer) + (1..10).filter { it != answer }.shuffled().take(3)).shuffled()
            FunRound("🎲 Jump to number " + answer, options.map { it.toString() }, options.indexOf(answer))
        }
        FunGame.LETTER_HUNT -> {
            val letters = ('A'..'Z').toList().map { it.toString() }
            val answer = letters.random()
            val options = (listOf(answer) + letters.filter { it != answer }.shuffled().take(3)).shuffled()
            FunRound("🔤 Find the letter: " + answer, options, options.indexOf(answer))
        }
        FunGame.PATTERN -> {
            val data = listOf(
                Pair(listOf("⭐","🔵","⭐","🔵","❓"), "⭐"),
                Pair(listOf("🔴","🔴","🟢","🔴","🔴","❓"), "🟢"),
                Pair(listOf("🍎","🍌","🍎","🍌","❓"), "🍎"),
                Pair(listOf("🔺","🟦","🔺","🟦","❓"), "🔺"),
                Pair(listOf("1","2","1","2","❓"), "1"),
                Pair(listOf("A","B","A","B","❓"), "A"),
                Pair(listOf("🍎","🍎","🍌","🍎","🍎","❓"), "🍌"),
                Pair(listOf("🔴","🟢","🔵","🔴","🟢","❓"), "🔵"),
                Pair(listOf("⭐","⭐","🔵","⭐","⭐","❓"), "🔵"),
                Pair(listOf("2","4","2","4","❓"), "2"),
                Pair(listOf("5","6","7","5","6","❓"), "7"),
                Pair(listOf("🐱","🐶","🐱","🐶","❓"), "🐱"),
                Pair(listOf("🟢","🟢","🟡","🟢","🟢","❓"), "🟡")
            ).random()
            val options = (listOf(data.second) + listOf("⭐","🔵","🔴","🟢","🍎","🍌","🔺","🟦","1","2").filter { it != data.second }.shuffled().take(3)).shuffled()
            FunRound("🧩 Complete the pattern!\n" + data.first.joinToString("  "), options, options.indexOf(data.second))
        }
        FunGame.NOT_BELONG -> {
            val rounds = listOf(
                Pair(listOf("🐶","🐱","🐰","🍌"), "🍌"),
                Pair(listOf("🍎","🍌","🍊","🐶"), "🐶"),
                Pair(listOf("🐶","🐱","🐰","🚗"), "🚗"),
                Pair(listOf("🔴","🔵","🟢","⭐"), "⭐"),
                Pair(listOf("🔺","🟦","⚪","🍌"), "🍌"),
                Pair(listOf("1","2","3","🍎"), "🍎"),
                Pair(listOf("A","B","C","🍎"), "🍎"),
                Pair(listOf("🐱","🐶","🐰","🔵"), "🔵"),
                Pair(listOf("🍎","🍌","🥕","🐱"), "🐱"),
                Pair(listOf("⭐","🌟","✨","🍎"), "🍎"),
                Pair(listOf("🔴","🟢","🔵","🍌"), "🍌"),
                Pair(listOf("2","4","6","🍎"), "🍎"),
                Pair(listOf("A","E","I","7"), "7"),
                Pair(listOf("🐶","🐱","🐰","🔺"), "🔺"),
                Pair(listOf("🍎","🍌","🍊","🚲"), "🚲")
            )
            val data = rounds.random()
            val options = data.first.shuffled()
            FunRound("🚫 Which one doesn't belong?", options, options.indexOf(data.second))
        }
        FunGame.FAST_FINGER -> {
            val targets = listOf("⭐","🔴","🔵","🟢","🍎","🔺","🐱","❤️")
            val answer = targets.random()
            val options = (listOf(answer) + targets.filter { it != answer }.shuffled().take(3)).shuffled()
            FunRound("⚡ FAST! Tap the " + answer + "!", options, options.indexOf(answer))
        }
    }
}
