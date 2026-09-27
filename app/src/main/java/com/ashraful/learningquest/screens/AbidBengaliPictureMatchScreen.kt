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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.AbidProgressStore
import kotlinx.coroutines.launch
import java.util.Locale

private data class BengaliMatchItem(val letter: String, val word: String, val emoji: String)

@Composable
fun AbidBengaliPictureMatchScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember(context) { AbidProgressStore(context) }
    val scope = rememberCoroutineScope()

    val items = remember {
        listOf(
            BengaliMatchItem("অ", "অজগর", "🪱"),
            BengaliMatchItem("আ", "আম", "🥭"),
            BengaliMatchItem("ই", "ইলিশ", "🐟"),
            BengaliMatchItem("উ", "উট", "🐪"),
            BengaliMatchItem("এ", "এক", "1️⃣"),
            BengaliMatchItem("ও", "ওল", "🍠"),
            BengaliMatchItem("ক", "কলা", "🍌"),
            BengaliMatchItem("গ", "গরু", "🐄"),
            BengaliMatchItem("জ", "জল", "💧"),
            BengaliMatchItem("চ", "চাঁদ", "🌙"),
            BengaliMatchItem("ছ", "ছাতা", "☂️"),
            BengaliMatchItem("ট", "টিয়া", "🦜"),
            BengaliMatchItem("ড", "ডাব", "🥥"),
            BengaliMatchItem("ত", "তাল", "🥭"),
            BengaliMatchItem("দ", "দই", "🥣"),
            BengaliMatchItem("ন", "নদী", "🏞️"),
            BengaliMatchItem("প", "পাখি", "🕊️"),
            BengaliMatchItem("ফ", "ফুল", "🌸"),
            BengaliMatchItem("ব", "বই", "📖"),
            BengaliMatchItem("ম", "মাছ", "🐠")
        )
    }

    var ids by rememberSaveable {
        mutableStateOf(items.indices.shuffled().take(10).joinToString(","))
    }
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
        val current = items[questionIds[index]]
        val options = remember(index, ids) {
            (listOf(current) + items.filter { it.letter != current.letter }
                .shuffled().take(3)).shuffled()
        }

        fun speak() {
            if (ttsReady) {
                tts.speak(
                    current.letter + " — " + current.word,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "bn_picture_match"
                )
            }
        }

        Column(
            Modifier.fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFEFFFF7), Color.White, Color(0xFFFFF4E5))))
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) {
                    Text("‹ Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.weight(1f))
                Text(
                    "বর্ণ → ছবি",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF23754A)
                )
            }

            Text("প্রশ্ন ${index + 1} / 10", fontSize = 17.sp, color = Color(0xFF60758A))
            Spacer(Modifier.height(10.dp))

            Card(
                Modifier.fillMaxWidth().height(245.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F8EF))
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("এই বর্ণ দিয়ে কোনটি শুরু হয়?", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(current.letter, fontSize = 105.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF23754A))
                    Text("উদাহরণ: ${current.word}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
                }
            }

            Spacer(Modifier.height(9.dp))
            OutlinedButton(
                onClick = { speak() },
                enabled = ttsReady,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("🔊 উচ্চারণ শুনি", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(7.dp))
            options.forEach { option ->
                Button(
                    onClick = {
                        if (selected == null) {
                            selected = option.letter
                            if (option.letter == current.letter) {
                                score++
                                speak()
                            }
                        }
                    },
                    enabled = selected == null,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected != null && option.letter == current.letter) Color(0xFF237A4B)
                        else if (selected == option.letter) Color(0xFFC62828)
                        else Color(0xFF23754A)
                    )
                ) {
                    Text(
                        "${option.emoji}  ${option.word}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (selected != null) {
                Spacer(Modifier.height(5.dp))
                Text(
                    if (selected == current.letter) "🎉 সঠিক!" else "😊 সঠিক উত্তর: ${current.word}",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = {
                        if (index == 9) finished = true
                        else {
                            index++
                            selected = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(if (index == 9) "ফলাফল দেখুন" else "পরের প্রশ্ন →", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
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
            Modifier.fillMaxSize().padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(70.dp))
            Text("🎉 বাংলা ছবি মিলানো শেষ!", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF23754A))
            Spacer(Modifier.height(14.dp))
            Text("$score / 10", fontSize = 62.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1769AA))
            Text(
                if (score >= 9) "🌟 অসাধারণ!" else if (score >= 7) "👏 খুব ভালো!" else if (score >= 5) "👍 ভালো চেষ্টা!" else "💪 আবার চেষ্টা করি!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "⭐ ${if (score >= 9) 3 else if (score >= 7) 2 else if (score >= 5) 1 else 0}   •   +${10 + score * 2} XP",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    ids = items.indices.shuffled().take(10).joinToString(",")
                    index = 0
                    score = 0
                    selected = null
                    finished = false
                    rewarded = false
                },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("🔄 আবার খেলি", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("‹ Home", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
