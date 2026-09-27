package com.ashraful.learningquest.screens

import android.speech.tts.TextToSpeech
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.AbidProgressStore
import kotlinx.coroutines.launch
import java.util.Locale

private data class BnPracticeItem(val letter: String, val word: String)

@Composable
fun AbidBengaliPracticeScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember(context) { AbidProgressStore(context) }
    val scope = rememberCoroutineScope()
    val letters = remember {
        listOf(
            BnPracticeItem("অ","অজগর"), BnPracticeItem("আ","আম"), BnPracticeItem("ই","ইলিশ"),
            BnPracticeItem("ঈ","ঈগল"), BnPracticeItem("উ","উট"), BnPracticeItem("ঊ","ঊষা"),
            BnPracticeItem("ঋ","ঋষি"), BnPracticeItem("এ","এক"), BnPracticeItem("ঐ","ঐক্য"),
            BnPracticeItem("ও","ওল"), BnPracticeItem("ঔ","ঔষধ"), BnPracticeItem("ক","কলা"),
            BnPracticeItem("খ","খরগোশ"), BnPracticeItem("গ","গরু"), BnPracticeItem("ঘ","ঘড়ি"),
            BnPracticeItem("চ","চাঁদ"), BnPracticeItem("ছ","ছাতা"), BnPracticeItem("জ","জল"),
            BnPracticeItem("ঝ","ঝুড়ি"), BnPracticeItem("ট","টিয়া"), BnPracticeItem("ঠ","ঠেলা"),
            BnPracticeItem("ড","ডাব"), BnPracticeItem("ঢ","ঢাক"), BnPracticeItem("ত","তাল"),
            BnPracticeItem("থ","থালা"), BnPracticeItem("দ","দই"), BnPracticeItem("ধ","ধনুক"),
            BnPracticeItem("ন","নদী"), BnPracticeItem("প","পাখি"), BnPracticeItem("ফ","ফুল"),
            BnPracticeItem("ব","বই"), BnPracticeItem("ভ","ভাত"), BnPracticeItem("ম","মাছ"),
            BnPracticeItem("য","যাত্রা"), BnPracticeItem("র","রথ"), BnPracticeItem("ল","লতা"),
            BnPracticeItem("শ","শাপলা"), BnPracticeItem("ষ","ষাঁড়"), BnPracticeItem("স","সাপ"),
            BnPracticeItem("হ","হাতি"), BnPracticeItem("ড়","বড়"), BnPracticeItem("ঢ়","আষাঢ়"),
            BnPracticeItem("য়","বয়স")
        )
    }
    var ids by rememberSaveable { mutableStateOf(letters.indices.shuffled().take(10).joinToString(",")) }
    var index by rememberSaveable { mutableIntStateOf(0) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var selected by rememberSaveable { mutableStateOf<String?>(null) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var rewarded by rememberSaveable { mutableStateOf(false) }
    var ttsReady by remember { mutableStateOf(false) }

    val tts = remember(context) {
        lateinit var engine: TextToSpeech
        engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = engine.setLanguage(Locale("bn", "IN"))
                ttsReady = result == TextToSpeech.LANG_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE
            }
        }
        engine
    }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }

    if (!finished) {
        val questionIds = ids.split(",").map { it.toInt() }
        val current = letters[questionIds[index]]
        val options = remember(index, ids) {
            (listOf(current.letter) + letters.filter { it.letter != current.letter }
                .shuffled().take(3).map { it.letter }).shuffled()
        }
        fun speak() {
            if (ttsReady) tts.speak(current.letter + " — " + current.word, TextToSpeech.QUEUE_FLUSH, null, "bn_practice")
        }

        Column(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color(0xFFFFF4E5), Color.White, Color(0xFFEAF7FF)))
            ).padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) { Text("‹ Home", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.weight(1f))
                Text("বাংলা অনুশীলন", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
            }
            Spacer(Modifier.height(8.dp))
            Text("প্রশ্ন " + (index + 1) + " / 10", fontSize = 17.sp, color = Color(0xFF60758A))
            Spacer(Modifier.height(10.dp))
            Card(
                Modifier.fillMaxWidth().height(250.dp), shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1D6))
            ) {
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("এই বর্ণটি কোনটি?", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    Text(current.letter, fontSize = 105.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
                    Text("উদাহরণ: " + current.word, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
                }
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = { speak() }, enabled = ttsReady, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("🔊 উচ্চারণ শুনি", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(7.dp))
            options.forEach { option ->
                Button(
                    onClick = {
                        if (selected == null) {
                            selected = option
                            if (option == current.letter) {
                                score++
                                speak()
                            }
                        }
                    },
                    enabled = selected == null,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected != null && option == current.letter) Color(0xFF237A4B)
                        else if (selected == option) Color(0xFFC62828) else Color(0xFF1769AA)
                    )
                ) { Text(option, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold) }
            }
            if (selected != null) {
                Spacer(Modifier.height(5.dp))
                Text(
                    if (selected == current.letter) "🎉 সঠিক!" else "😊 সঠিক উত্তর: " + current.letter,
                    fontSize = 19.sp, fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Button(
                    onClick = { if (index == 9) finished = true else { index++; selected = null } },
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) { Text(if (index == 9) "ফলাফল দেখুন" else "পরের প্রশ্ন →", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            }
        }
    } else {
        if (!rewarded) {
            LaunchedEffect(Unit) {
                val stars = if (score >= 9) 3 else if (score >= 7) 2 else if (score >= 5) 1 else 0
                scope.launch { store.completeActivity("letters", stars, 10 + score * 2) }
                rewarded = true
            }
        }
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))
            Text("🎉 বাংলা অনুশীলন শেষ!", fontSize = 29.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB05A00))
            Spacer(Modifier.height(14.dp))
            Text(score.toString() + " / 10", fontSize = 62.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
            Text(
                if (score >= 9) "🌟 অসাধারণ!" else if (score >= 7) "👏 খুব ভালো!" else if (score >= 5) "👍 ভালো চেষ্টা!" else "💪 আবার চেষ্টা করি!",
                fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text("⭐ " + (if (score >= 9) 3 else if (score >= 7) 2 else if (score >= 5) 1 else 0) + "   •   +" + (10 + score * 2) + " XP", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    ids = letters.indices.shuffled().take(10).joinToString(",")
                    index = 0; score = 0; selected = null; finished = false; rewarded = false
                },
                Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(18.dp)
            ) { Text("🔄 আবার খেলি", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = onBack, Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(18.dp)) {
                Text("‹ Home", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
