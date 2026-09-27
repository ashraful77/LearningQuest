package com.ashraful.learningquest.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun AbidBengaliLettersScreen(onBack: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }
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
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = setLanguage(Locale("bn", "IN"))
                ttsReady = result == TextToSpeech.LANG_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE
            } else {
                ttsReady = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { tts.shutdown() }
    }

    val current = letters[index]

    fun speakCurrent() {
        if (ttsReady) {
            tts.speak(current.second, TextToSpeech.QUEUE_FLUSH, null, "bengali_letter_" + index)
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
                    index = (index - 1 + letters.size) % letters.size
                    speakCurrent()
                },
                modifier = Modifier.weight(1f).height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) { Text("← আগের", fontSize = 18.sp) }

            Button(
                onClick = {
                    index = (index + 1) % letters.size
                    speakCurrent()
                },
                modifier = Modifier.weight(1f).height(58.dp),
                shape = RoundedCornerShape(18.dp)
            ) { Text(if (index == letters.lastIndex) "🔄 আবার" else "পরের →", fontSize = 18.sp) }
        }
    }
}
