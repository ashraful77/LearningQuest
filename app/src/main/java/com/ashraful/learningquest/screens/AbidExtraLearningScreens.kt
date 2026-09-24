package com.ashraful.learningquest.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import kotlin.random.Random

@Composable
private fun AbidGameFrame(title: String, color: Color, onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().background(color.copy(alpha = 0.12f)).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Home", fontSize = 18.sp) }
            Text(title, fontSize = 27.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = color)
        }
        Spacer(Modifier.height(16.dp))
        content()
    }
}

@Composable
fun AbidLetterSoundsScreen(onBack: () -> Unit) {
    val letters = listOf(
        "A" to "A says /æ/ — Apple 🍎", "B" to "B says /b/ — Ball ⚽",
        "C" to "C says /k/ — Cat 🐱", "D" to "D says /d/ — Dog 🐶",
        "E" to "E says /e/ — Egg 🥚", "F" to "F says /f/ — Fish 🐟",
        "G" to "G says /g/ — Goat 🐐", "H" to "H says /h/ — Hat 🎩",
        "I" to "I says /i/ — Igloo 🏠", "J" to "J says /j/ — Jam 🍓"
    )
    var index by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val tts = remember(context) { TextToSpeech(context) { }.apply { language = Locale.US } }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }
    val current = letters[index]
    AbidGameFrame("🔊 Letter Sounds", Color(0xFF1769AA), onBack) {
        Text("Letter ${index + 1} of ${letters.size}", fontSize = 17.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(20.dp))
        Text(current.first, fontSize = 110.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF1769AA))
        Text(current.second, fontSize = 23.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color(0xFF34495E))
        Spacer(Modifier.height(25.dp))
        Button(onClick = { tts.speak(current.second.replace("—", ""), TextToSpeech.QUEUE_FLUSH, null, "letter") }, modifier = Modifier.fillMaxWidth().height(58.dp)) { Text("🔊 Hear It", fontSize = 20.sp) }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = { index = (index - 1).coerceAtLeast(0) }, Modifier.weight(1f).height(56.dp), enabled = index > 0) { Text("← Previous") }
            Button(onClick = { index = (index + 1) % letters.size }, Modifier.weight(1f).height(56.dp)) { Text(if (index == letters.lastIndex) "🔄 Again" else "Next →") }
        }
    }
}

@Composable
fun AbidLetterPictureMatchScreen(onBack: () -> Unit) {
    val items = listOf(
        "A" to listOf("🍎 Apple", "🐱 Cat", "🐟 Fish", "🚌 Bus"),
        "B" to listOf("⚽ Ball", "🍎 Apple", "🐶 Dog", "🍌 Banana"),
        "C" to listOf("🐱 Cat", "🍇 Grapes", "⚽ Ball", "🐟 Fish"),
        "D" to listOf("🐶 Dog", "🍎 Apple", "🚌 Bus", "🐱 Cat")
    )
    var round by remember { mutableIntStateOf(0) }
    val current = items[round % items.size]
    val options = remember(round) { current.second.shuffled() }
    var selected by remember { mutableStateOf<String?>(null) }
    AbidGameFrame("🖼️ Letter → Picture", Color(0xFF23754A), onBack) {
        Text("Which picture starts with ${current.first}?", fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Spacer(Modifier.height(20.dp))
        Text(current.first, fontSize = 92.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF23754A))
        options.forEach { option ->
            val correct = option == current.second.first()
            Button(onClick = { selected = option }, enabled = selected == null, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(58.dp), shape = RoundedCornerShape(18.dp)) { Text(option, fontSize = 19.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
        }
        if (selected != null) {
            Spacer(Modifier.height(12.dp))
            Text(if (selected == current.second.first()) "🎉 Correct!" else "😊 The answer is ${current.second.first()}", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Button(onClick = { round++; selected = null }, Modifier.fillMaxWidth().height(56.dp)) { Text("Next Picture →") }
        }
    }
}

@Composable
fun AbidSimpleMathScreen(onBack: () -> Unit) {
    var round by remember { mutableIntStateOf(0) }
    val a = remember(round) { Random.nextInt(1, 6) }
    val b = remember(round) { Random.nextInt(1, 6) }
    val answer = a + b
    val options = remember(round) { (listOf(answer) + (1..10).filter { it != answer }.shuffled().take(3)).shuffled() }
    var selected by remember { mutableIntStateOf(-1) }
    AbidGameFrame("➕ Little Maths", Color(0xFF9A5A00), onBack) {
        Text("What is $a + $b ?", fontSize = 42.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF9A5A00))
        options.forEach { option ->
            Button(onClick = { selected = option }, enabled = selected == -1, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(58.dp)) { Text(option.toString(), fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
        }
        if (selected != -1) {
            Text(if (selected == answer) "🎉 Great maths!" else "😊 Answer: $answer", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Button(onClick = { round++; selected = -1 }, Modifier.fillMaxWidth().height(56.dp)) { Text("Next Sum →") }
        }
    }
}

@Composable
fun AbidShapeMatchScreen(onBack: () -> Unit) {
    val shapes = listOf("●" to "Circle", "■" to "Square", "▲" to "Triangle", "▭" to "Rectangle")
    var round by remember { mutableIntStateOf(0) }
    val current = shapes[round % shapes.size]
    val options = remember(round) { shapes.map { it.second }.shuffled() }
    var selected by remember { mutableStateOf<String?>(null) }
    AbidGameFrame("🔷 Shape Match", Color(0xFF1769AA), onBack) {
        Text("What shape is this?", fontSize = 23.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text(current.first, fontSize = 110.sp, color = Color(0xFF1769AA))
        options.forEach { option ->
            Button(onClick = { selected = option }, enabled = selected == null, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).height(56.dp)) { Text(option, fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
        }
        if (selected != null) {
            Text(if (selected == current.second) "🎉 Correct shape!" else "😊 It is a ${current.second}", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Button(onClick = { round++; selected = null }, Modifier.fillMaxWidth().height(56.dp)) { Text("Next Shape →") }
        }
    }
}

@Composable
fun AbidAnimalSoundsScreen(onBack: () -> Unit) {
    val animals = listOf("🐶" to "Dog — Woof!", "🐱" to "Cat — Meow!", "🐮" to "Cow — Moo!", "🦁" to "Lion — Roar!", "🐑" to "Sheep — Baa!")
    var index by remember { mutableIntStateOf(0) }
    val tts = remember { TextToSpeech(null) { }.apply { language = Locale.US } }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }
    val current = animals[index]
    AbidGameFrame("🐾 Animal Sounds", Color(0xFF23754A), onBack) {
        Text("What does this animal say?", fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text(current.first, fontSize = 105.sp)
        Text(current.second, fontSize = 25.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF23754A))
        Button(onClick = { tts.speak(current.second, TextToSpeech.QUEUE_FLUSH, null, "animal") }, Modifier.fillMaxWidth().height(58.dp)) { Text("🔊 Hear the sound", fontSize = 20.sp) }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = { index = (index - 1).coerceAtLeast(0) }, Modifier.weight(1f).height(56.dp), enabled = index > 0) { Text("← Previous") }
            Button(onClick = { index = (index + 1) % animals.size }, Modifier.weight(1f).height(56.dp)) { Text("Next →") }
        }
    }
}

@Composable
fun AbidFruitsScreen(onBack: () -> Unit) {
    val foods = listOf("🍎" to "Apple", "🍌" to "Banana", "🍊" to "Orange", "🍇" to "Grapes", "🥕" to "Carrot", "🥦" to "Broccoli")
    var index by remember { mutableIntStateOf(0) }
    val current = foods[index]
    AbidGameFrame("🍎 Fruits & Veggies", Color(0xFFC13A63), onBack) {
        Text("Learn healthy foods!", fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text(current.first, fontSize = 105.sp)
        Text(current.second.uppercase(), fontSize = 32.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFFC13A63))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = { index = (index - 1).coerceAtLeast(0) }, Modifier.weight(1f).height(56.dp), enabled = index > 0) { Text("← Previous") }
            Button(onClick = { index = (index + 1) % foods.size }, Modifier.weight(1f).height(56.dp)) { Text("Next →") }
        }
    }
}

@Composable
fun AbidMemoryGameScreen(onBack: () -> Unit) {
    val emojis = listOf("🍎","🍌","🍊","🍇","🍓","🍉","🐶","🐱","🦁","🐼","⭐","🌈")
    var round by remember { mutableIntStateOf(0) }
    val cards = remember(round) { emojis.shuffled().take(6).flatMap { listOf(it, it) }.shuffled() }
    var open by remember { mutableStateOf<List<Int>>(emptyList()) }
    var matched by remember { mutableStateOf<Set<Int>>(emptySet()) }
    LaunchedEffect(open) {
        if (open.size == 2) {
            if (cards[open[0]] == cards[open[1]]) matched = matched + open
            kotlinx.coroutines.delay(650)
            open = emptyList()
        }
    }
    AbidGameFrame("🧠 Memory Match 2.0", Color(0xFF7043A8), onBack) {
        Text("Find all 6 pairs!", fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        for (row in 0..2) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (col in 0..3) {
                    val i = row * 4 + col
                    val visible = i in open || i in matched
                    Card(onClick = { if (open.size < 2 && i !in matched && i !in open) open = open + i }, modifier = Modifier.weight(1f).height(82.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = if (visible) Color.White else Color(0xFFDCCBFF))) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(if (visible) cards[i] else "❓", fontSize = 34.sp) }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        Text("Matched: ${matched.size / 2} / 6", fontSize = 19.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        if (matched.size == cards.size) {
            Text("🏆 Fantastic memory!", fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF7043A8))
            Button(onClick = { round++; open = emptyList(); matched = emptySet() }, Modifier.fillMaxWidth().height(56.dp)) { Text("Play Again →") }
        }
    }
}

@Composable
fun AbidAchievementsScreen(onBack: () -> Unit) {
    val achievements = listOf("🌟" to "First 10 Letters", "✍️" to "Writing Star", "🔢" to "Counting Champion", "🎨" to "Color Expert", "🔷" to "Shape Explorer", "🧠" to "Memory Master")
    AbidGameFrame("🏆 Achievements", Color(0xFFB05A00), onBack) {
        Text("Collect stars as you learn!", fontSize = 22.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        achievements.forEach { (icon, title) ->
            Card(Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(icon, fontSize = 34.sp)
                    Spacer(Modifier.width(14.dp))
                    Text(title, fontSize = 19.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color(0xFF26354A))
                    Spacer(Modifier.weight(1f))
                    Text("⭐", fontSize = 24.sp)
                }
            }
        }
    }
}
