package com.ashraful.learningquest.screens

import android.speech.tts.TextToSpeech
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale
@Composable
fun AbidBengaliListenChooseSetsScreen(onSetSelected: (Int) -> Unit, onBack: () -> Unit) {
    val setSizes = listOf(10, 10, 10, 10, 6)
    Column(Modifier.fillMaxSize().background(Color(0xFFFFF4E5)).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Back", fontSize = 19.sp, fontWeight = FontWeight.Bold) }
            Text("🎧 বাংলা শুনে বেছে নিই", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
        }
        Spacer(Modifier.height(10.dp))
        Text("প্রতি সেটে নির্দিষ্ট বর্ণ", fontSize = 15.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(18.dp))
        setSizes.forEachIndexed { index, size ->
            val start = index * 10 + 1
            val end = start + size - 1
            Card(onClick = { onSetSelected(index + 1) }, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).height(76.dp),
                shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Row(Modifier.fillMaxSize().padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🎧", fontSize = 30.sp)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Set ${index + 1}", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
                        Text("বর্ণ $start – $end", fontSize = 13.sp, color = Color(0xFF60758A))
                    }
                    Text("›", fontSize = 30.sp, color = Color(0xFFB05A00))
                }
            }
        }
    }
}

@Composable
fun AbidBengaliListenChooseScreen(setNumber: Int, onBack: () -> Unit) {
    val allLetters = listOf("অ","আ","ই","ঈ","উ","ঊ","ঋ","এ","ঐ","ও","ঔ","ক","খ","গ","ঘ","ঙ","চ","ছ","জ","ঝ","ঞ","ট","ঠ","ড","ঢ","ণ","ত","থ","দ","ধ","ন","প","ফ","ব","ভ","ম","য","র","ল","শ","ষ","স","হ","ড়","ঢ়","য়")
    val start = (setNumber - 1) * 10
    val setLetters = allLetters.drop(start).take(10)
    var round by remember { mutableIntStateOf(0) }
    var target by remember(round) { mutableStateOf(setLetters.random()) }
    var options by remember(round) { mutableStateOf((listOf(target) + setLetters.filter { it != target }.shuffled().take(3)).shuffled()) }
    var selected by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val tts = remember(context) { TextToSpeech(context) { }.apply { language = Locale("bn", "IN") } }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }

    fun speak() {
        tts.language = Locale("bn", "IN")
        tts.speak(target, TextToSpeech.QUEUE_FLUSH, null, "bn_listen")
    }
    fun next() {
        if (round == 9) finished = true
        else {
            round++
            target = setLetters.random()
            options = (listOf(target) + setLetters.filter { it != target }.shuffled().take(3)).shuffled()
            selected = null
        }
    }

    Column(Modifier.fillMaxSize().background(Color(0xFFFFF4E5)).padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Back", fontSize = 19.sp, fontWeight = FontWeight.Bold) }
            Text("🎧 বাংলা • Set $setNumber", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
        }
        if (finished) {
            Spacer(Modifier.height(70.dp))
            Text("🎉 Test Complete!", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
            Spacer(Modifier.height(14.dp))
            Text("Score: $score / 10", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF26354A))
            Spacer(Modifier.height(24.dp))
            Button(onClick = { round = 0; score = 0; finished = false; target = setLetters.random(); options = (listOf(target) + setLetters.filter { it != target }.shuffled().take(3)).shuffled(); selected = null },
                modifier = Modifier.fillMaxWidth().height(58.dp)) { Text("🔄 আবার খেলি", fontSize = 20.sp) }
        } else {
            Spacer(Modifier.height(10.dp))
            Text("প্রশ্ন ${round + 1} / 10", fontSize = 17.sp, color = Color(0xFF60758A))
            Spacer(Modifier.height(16.dp))
            Button(onClick = { speak() }, modifier = Modifier.fillMaxWidth().height(78.dp), shape = RoundedCornerShape(22.dp)) {
                Text("🔊 বর্ণটি শুনি", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(12.dp))
            Text("শুনে সঠিক বর্ণটি বেছে নাও", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF26354A))
            Spacer(Modifier.height(16.dp))
            options.forEach { option ->
                val correct = option == target
                val chosen = selected == option
                val bg = when { selected == null -> Color.White; correct -> Color(0xFFE8F8EF); chosen -> Color(0xFFFFE8E8); else -> Color(0xFFF5F5F5) }
                val tc = when { selected == null -> Color(0xFF1769AA); correct -> Color(0xFF23754A); chosen -> Color(0xFFC62828); else -> Color(0xFF8A96A8) }
                Card(onClick = { if (selected == null) { selected = option; if (option == target) score++ } },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).height(68.dp), shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = bg)) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(option, fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = tc) }
                }
            }
            if (selected != null) {
                Spacer(Modifier.height(8.dp))
                Text(if (selected == target) "🎉 সঠিক!" else "😊 সঠিক বর্ণ: $target", fontSize = 19.sp, fontWeight = FontWeight.Bold,
                    color = if (selected == target) Color(0xFF23754A) else Color(0xFFC62828))
                Spacer(Modifier.height(8.dp))
                Button(onClick = { next() }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                    Text(if (round == 9) "🏆 ফলাফল" else "পরের প্রশ্ন →", fontSize = 19.sp)
                }
            }
        }
    }
}

@Composable
fun AbidBengaliLettersScreen(onBack: () -> Unit) {
    var index by rememberSaveable { mutableIntStateOf(0) }
    var ttsReady by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val letters = listOf(
        "অ" to "অ — অজগর", "আ" to "আ — আম", "ই" to "ই — ইলিশ", "ঈ" to "ঈ — ঈগল",
        "উ" to "উ — উট", "ঊ" to "ঊ — ঊষা", "ঋ" to "ঋ — ঋষি", "এ" to "এ — এক",
        "ঐ" to "ঐ — ঐক্য", "ও" to "ও — ওল", "ঔ" to "ঔ — ঔষধ",
        "ক" to "ক — কলা", "খ" to "খ — খরগোশ", "গ" to "গ — গরু", "ঘ" to "ঘ — ঘড়ি", "ঙ" to "ঙ",
        "চ" to "চ — চাঁদ", "ছ" to "ছ — ছাতা", "জ" to "জ — জল", "ঝ" to "ঝ — ঝুড়ি", "ঞ" to "ঞ",
        "ট" to "ট — টিয়া", "ঠ" to "ঠ — ঠেলা", "ড" to "ড — ডাব", "ঢ" to "ঢ — ঢাক", "ণ" to "ণ",
        "ত" to "ত — তাল", "থ" to "থ — থালা", "দ" to "দ — দই", "ধ" to "ধ — ধনুক", "ন" to "ন — নদী",
        "প" to "প — পাখি", "ফ" to "ফ — ফুল", "ব" to "ব — বই", "ভ" to "ভ — ভাত", "ম" to "ম — মাছ",
        "য" to "য — যাত্রা", "র" to "র — রথ", "ল" to "ল — লতা", "শ" to "শ — শাপলা",
        "ষ" to "ষ — ষাঁড়", "স" to "স — সাপ", "হ" to "হ — হাতি",
        "ড়" to "ড় — বড়", "ঢ়" to "ঢ় — আষাঢ়", "য়" to "য় — বয়স"
    )

    val tts = remember(context) {
        lateinit var engine: TextToSpeech
        engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = engine.setLanguage(Locale("bn", "IN"))
                ttsReady = result == TextToSpeech.LANG_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE
            } else {
                ttsReady = false
            }
        }
        engine
    }

    DisposableEffect(Unit) {
        onDispose { tts.shutdown() }
    }

    val current = letters[index]

    fun speakLetter(targetIndex: Int) {
        if (ttsReady) {
            tts.speak(letters[targetIndex].second, TextToSpeech.QUEUE_FLUSH, null, "bengali_letter_" + targetIndex)
        }
    }

    fun speakCurrent() {
        if (ttsReady) {
            speakLetter(index)
        }
    }

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
            Text("🔤 বাংলা বর্ণমালা", fontSize = 27.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFFB05A00))
        }

        Spacer(Modifier.height(22.dp))
        Text((index + 1).toString() + " / " + letters.size, fontSize = 18.sp, color = Color(0xFF60758A))
        Spacer(Modifier.height(14.dp))

        Card(
            Modifier.fillMaxWidth().height(340.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1D6)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("বাংলা", fontSize = 28.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color(0xFFB05A00))
                Spacer(Modifier.height(4.dp))
                Text(current.first, fontSize = 130.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Color(0xFF1769AA))
                Text(current.second.substringAfter(" — "), fontSize = 23.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color(0xFF60758A))
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { speakCurrent() },
            enabled = ttsReady,
            modifier = Modifier.fillMaxWidth().height(58.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text("🔊 উচ্চারণ শুনি", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }

        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = {
                    val newIndex = (index - 1 + letters.size) % letters.size
                    index = newIndex
                    speakLetter(newIndex)
                },
                modifier = Modifier.weight(1f).height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) { Text("← আগের", fontSize = 18.sp) }

            Button(
                onClick = {
                    val newIndex = (index + 1) % letters.size
                    index = newIndex
                    speakLetter(newIndex)
                },
                modifier = Modifier.weight(1f).height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) { Text(if (index == letters.lastIndex) "🔄 আবার" else "পরের →", fontSize = 18.sp) }
        }
    }
}
