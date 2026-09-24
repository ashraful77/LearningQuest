package com.ashraful.learningquest.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.GameDataStore
import java.util.Locale
import kotlin.random.Random

private data class NextQuestion(val text: String, val options: List<String>, val answer: String)
private data class Achievement(val icon: String, val title: String, val description: String, val unlocked: Boolean)

@Composable
private fun NextFrame(title: String, emoji: String, color: Color, pale: Color, onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(pale, Color.White))).padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onBack, shape = RoundedCornerShape(18.dp)) { Text("‹ Home") }
            Spacer(Modifier.width(10.dp))
            Text(emoji + " " + title, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = color)
        }
        Spacer(Modifier.height(16.dp))
        content()
    }
}

@Composable
private fun NextQuiz(title: String, emoji: String, color: Color, pale: Color, questions: List<NextQuestion>, onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val scope = rememberCoroutineScope()
    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf(false) }
    var done by remember { mutableStateOf(false) }

    if (done) {
        NextFrame(title, emoji, color, pale, onBack) {
            Spacer(Modifier.height(25.dp))
            Text("🏆", fontSize = 68.sp)
            Text("Finished!", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(14.dp))
            Text(score.toString() + " / " + questions.size, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text("⭐ Score", fontSize = 18.sp)
            Spacer(Modifier.height(22.dp))
            Button(onClick = { index=0; score=0; selected=null; answered=false; done=false }, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(18.dp)) {
                Text("🔄 Play Again", fontSize=18.sp)
            }
            OutlinedButton(onClick=onBack, modifier=Modifier.fillMaxWidth().padding(top=8.dp), shape=RoundedCornerShape(18.dp)) { Text("‹ Home") }
        }
        return
    }

    val q = questions[index]
    NextFrame(title, emoji, color, pale, onBack) {
        Text("Question " + (index+1) + " / " + questions.size, fontWeight=FontWeight.Bold, color=Color(0xFF68778C))
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(progress={ (index+1)/questions.size.toFloat() }, modifier=Modifier.fillMaxWidth().height(8.dp))
        Spacer(Modifier.height(15.dp))
        Card(Modifier.fillMaxWidth(), shape=RoundedCornerShape(24.dp), colors=CardDefaults.cardColors(containerColor=Color.White)) {
            Text(q.text, Modifier.fillMaxWidth().padding(24.dp), fontSize=23.sp, fontWeight=FontWeight.Bold, textAlign=TextAlign.Center, color=Color(0xFF26354A))
        }
        Spacer(Modifier.height(12.dp))
        q.options.shuffled().forEach { option ->
            val chosen=selected==option
            Button(
                onClick={
                    if(!answered){
                        answered=true; selected=option
                        if(option==q.answer){ score++; scope.launch { store.addReward(5,5) } }
                    }
                },
                enabled=!answered,
                modifier=Modifier.fillMaxWidth().padding(vertical=4.dp),
                shape=RoundedCornerShape(17.dp),
                colors=ButtonDefaults.buttonColors(
                    containerColor=when {
                        !answered -> Color.White
                        chosen && option==q.answer -> Color(0xFF20B957)
                        chosen -> Color(0xFFE94055)
                        option==q.answer -> Color(0xFF20B957)
                        else -> Color.White
                    },
                    contentColor=if(answered && (chosen || option==q.answer)) Color.White else Color(0xFF26354A)
                )
            ){ Text(option, fontSize=18.sp, fontWeight=FontWeight.SemiBold) }
        }
        if(answered){
            Spacer(Modifier.height(8.dp))
            Text(if(selected==q.answer) "🎉 Correct! +5 XP +5 Coins" else "💡 Answer: "+q.answer, fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(7.dp))
            Button(onClick={if(index==questions.lastIndex) done=true else {index++;selected=null;answered=false}}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(18.dp)){
                Text(if(index==questions.lastIndex) "🏆 Finish" else "Next →")
            }
        }
        Spacer(Modifier.weight(1f))
        Text("Score: "+score, fontWeight=FontWeight.Bold, color=Color(0xFF68778C))
    }
}

@Composable
fun ArifaVisualPuzzlesScreen(onBack: () -> Unit) {
    val q = listOf(
        NextQuestion("Which shape comes next? 🔵 🔴 🔵 🔴 ?", listOf("🔵","🟢","🟡","🟣"), "🔵"),
        NextQuestion("Complete: 1, 3, 5, 7, ?", listOf("8","9","10","11"), "9"),
        NextQuestion("Which is the mirror pair?", listOf("AB","BA","AC","AD"), "BA"),
        NextQuestion("Which number is different?", listOf("2","4","6","9"), "9"),
        NextQuestion("Complete: A, B, D, E, G, ?", listOf("H","I","J","K"), "H"),
        NextQuestion("Which comes next? ⭐ ⭐ ❤️ ⭐ ⭐ ❤️ ?", listOf("⭐","❤️","🔵","🟢"), "⭐"),
        NextQuestion("Which word does not belong?", listOf("Run","Jump","Swim","Apple"), "Apple"),
        NextQuestion("If ▲ = 3 sides and ■ = 4 sides, ▲ + ■ = ?", listOf("5","6","7","8"), "7"),
        NextQuestion("Which is the smallest?", listOf("12","21","9","15"), "9"),
        NextQuestion("Complete: 10, 20, 30, ?", listOf("35","40","45","50"), "40")
    )
    NextQuiz("Visual Puzzles","🧩",Color(0xFF9A5A00),Color(0xFFFFF1DE),q,onBack)
}

@Composable
fun ArifaEnglishSpeakingScreen(onBack: () -> Unit) {
    val context=LocalContext.current
    var ready by remember { mutableStateOf(false) }
    val tts=remember(context){
        TextToSpeech(context){ status -> if(status==TextToSpeech.SUCCESS) ready=true }.apply { language=Locale.US }
    }
    DisposableEffect(Unit){ onDispose { tts.stop(); tts.shutdown() } }
    val phrases=listOf(
        "Hello! My name is Arifa.",
        "Good morning!",
        "How are you today?",
        "I like reading books.",
        "I can count from one to ten.",
        "This is my school.",
        "I love learning new things.",
        "Thank you. Have a nice day!"
    )
    var index by remember { mutableIntStateOf(0) }
    NextFrame("English Speaking","🗣️",Color(0xFF7043A8),Color(0xFFF3ECFF),onBack){
        Text("Listen, then say it aloud.",fontSize=18.sp,fontWeight=FontWeight.Bold)
        Spacer(Modifier.height(20.dp))
        Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(28.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){
            Column(Modifier.fillMaxWidth().padding(28.dp),horizontalAlignment=Alignment.CenterHorizontally){
                Text(phrases[index],fontSize=27.sp,fontWeight=FontWeight.ExtraBold,textAlign=TextAlign.Center,color=Color(0xFF4C2E72))
                Spacer(Modifier.height(20.dp))
                Button(onClick={if(ready) tts.speak(phrases[index],TextToSpeech.QUEUE_FLUSH,null,"phrase")},enabled=ready,shape=RoundedCornerShape(18.dp)){
                    Text("🔊 Listen",fontSize=18.sp)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)){
            OutlinedButton(onClick={if(index>0) index--},enabled=index>0,modifier=Modifier.weight(1f)){Text("← Previous")}
            Button(onClick={if(index<phrases.lastIndex) index++ else index=0},modifier=Modifier.weight(1f)){Text("Next →")}
        }
        Spacer(Modifier.height(20.dp))
        Text("💬 Tip: speak slowly and clearly.",fontWeight=FontWeight.Bold,color=Color(0xFF65738A))
    }
}

@Composable
fun ArifaDailyChallengeScreen(onBack: () -> Unit) {
    val q=listOf(
        NextQuestion("12 + 8 = ?",listOf("18","20","22","24"),"20"),
        NextQuestion("Which word is a noun?",listOf("Happy","School","Quickly","Run"),"School"),
        NextQuestion("Which planet is our home?",listOf("Mars","Earth","Venus","Jupiter"),"Earth"),
        NextQuestion("Which shape has 4 equal sides?",listOf("Circle","Triangle","Square","Oval"),"Square"),
        NextQuestion("5 × 2 = ?",listOf("7","8","10","12"),"10"),
        NextQuestion("Opposite of HOT?",listOf("Warm","Cold","Dry","Fast"),"Cold"),
        NextQuestion("How many days are in a week?",listOf("5","6","7","8"),"7"),
        NextQuestion("Which animal gives us milk?",listOf("Cow","Lion","Tiger","Fox"),"Cow"),
        NextQuestion("What comes after 99?",listOf("98","100","101","109"),"100"),
        NextQuestion("Which is a source of light?",listOf("Moon","Sun","Rock","Tree"),"Sun")
    )
    NextQuiz("Daily Challenge","🔥",Color(0xFFD35400),Color(0xFFFFEEDB),q,onBack)
}

@Composable
fun ArifaAchievements2Screen(onBack: () -> Unit) {
    val context=LocalContext.current
    val data by remember { GameDataStore(context).gameData }.collectAsState(initial=null)
    val d=data
    val achievements=listOf(
        Achievement("🌟","First Steps","Earn 50 XP", (d?.xp ?: 0)>=50),
        Achievement("🔥","Streak Starter","Reach a 3-day streak", (d?.streak ?: 0)>=3),
        Achievement("🪙","Coin Collector","Earn 100 coins", (d?.coins ?: 0)>=100),
        Achievement("➗","Math Explorer","Score 10+ in maths", (d?.mathScore ?: 0)>=10),
        Achievement("🔤","English Explorer","Score 10+ in English", (d?.englishScore ?: 0)>=10),
        Achievement("🔬","Science Explorer","Score 10+ in science", (d?.scienceScore ?: 0)>=10),
        Achievement("🧩","Puzzle Explorer","Score 10+ in puzzles", (d?.puzzleScore ?: 0)>=10),
        Achievement("🚀","Level Up","Reach level 5", (d?.level ?: 1)>=5)
    )
    NextFrame("Achievements 2.0","🏆",Color(0xFFB77900),Color(0xFFFFF5D9),onBack){
        achievements.forEach { a ->
            Card(Modifier.fillMaxWidth().padding(vertical=4.dp),shape=RoundedCornerShape(18.dp),colors=CardDefaults.cardColors(containerColor=if(a.unlocked) Color(0xFFE9F9EE) else Color.White)){
                Row(Modifier.fillMaxWidth().padding(14.dp),verticalAlignment=Alignment.CenterVertically){
                    Text(a.icon,fontSize=28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)){Text(a.title,fontWeight=FontWeight.Bold,fontSize=17.sp);Text(a.third.let{if(it) "Unlocked!" else "Keep learning to unlock"},fontSize=12.sp,color=Color(0xFF71809A))}
                    Text(if(a.third) "✓" else "🔒",fontSize=22.sp)
                }
            }
        }
    }
}

@Composable
fun ArifaLearningPathScreen(onBack: () -> Unit) {
    val context=LocalContext.current
    val data by remember { GameDataStore(context).gameData }.collectAsState(initial=null)
    val level=data?.level ?: 1
    val steps=listOf("🌱 Foundation","📖 Reading & English","➗ Maths Skills","🧠 Logic & Puzzles","🌍 World Explorer","🚀 Master Learner")
    NextFrame("Learning Path","🛤️",Color(0xFF247A57),Color(0xFFE8F8EF),onBack){
        Text("Level "+level,fontSize=22.sp,fontWeight=FontWeight.ExtraBold,color=Color(0xFF247A57))
        Text("Follow your learning journey step by step.",color=Color(0xFF68778C),textAlign=TextAlign.Center)
        Spacer(Modifier.height(14.dp))
        steps.forEachIndexed { i, step ->
            val unlocked=i <= (level-1).coerceAtMost(steps.lastIndex)
            Card(Modifier.fillMaxWidth().padding(vertical=4.dp),shape=RoundedCornerShape(18.dp),colors=CardDefaults.cardColors(containerColor=if(unlocked) Color(0xFFE9F9EE) else Color.White)){
                Row(Modifier.fillMaxWidth().padding(15.dp),verticalAlignment=Alignment.CenterVertically){
                    Text(if(unlocked) "✅" else "🔒",fontSize=22.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(step,Modifier.weight(1f),fontSize=17.sp,fontWeight=FontWeight.Bold,color=if(unlocked) Color(0xFF247A57) else Color(0xFF7A8798))
                    Text("Step "+(i+1),fontSize=11.sp,color=Color(0xFF7A8798))
                }
            }
        }
    }
}
