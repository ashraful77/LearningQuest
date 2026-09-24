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
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.GameDataStore
import kotlinx.coroutines.launch
import kotlin.random.Random

private data class Level2Question(val question: String, val options: List<String>, val answer: String)

@Composable
private fun Level2Frame(title: String, emoji: String, color: Color, pale: Color, onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(pale, Color.White))).padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onBack, shape = RoundedCornerShape(18.dp)) { Text("‹ Home") }
            Spacer(Modifier.width(10.dp))
            Text(emoji + " " + title, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, color = color)
        }
        Spacer(Modifier.height(14.dp))
        content()
    }
}

@Composable
private fun Level2Quiz(title: String, emoji: String, color: Color, pale: Color, questions: List<Level2Question>, onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { GameDataStore(context) }
    val scope = rememberCoroutineScope()
    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }

    if (finished) {
        Level2Frame(title, emoji, color, pale, onBack) {
            Spacer(Modifier.height(30.dp))
            Text("🎉", fontSize = 70.sp)
            Text("Challenge Complete!", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(18.dp))
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.fillMaxWidth().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(score.toString() + " / " + questions.size.toString(), fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = color)
                    Text("⭐ Score", fontSize = 18.sp)
                    Text("Great work! Keep exploring.", fontSize = 17.sp, color = Color(0xFF60758A))
                }
            }
            Spacer(Modifier.height(22.dp))
            Button(onClick = { index = 0; score = 0; selected = null; answered = false; finished = false }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Text("🔄 Play Again", fontSize = 18.sp)
            }
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(18.dp)) { Text("‹ Home") }
        }
        return
    }

    val q = questions[index]
    Level2Frame(title, emoji, color, pale, onBack) {
        Text("Question " + (index + 1) + " / " + questions.size, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(progress = { (index + 1) / questions.size.toFloat() }, modifier = Modifier.fillMaxWidth().height(8.dp))
        Spacer(Modifier.height(18.dp))
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp)) {
            Text(q.question, Modifier.fillMaxWidth().padding(26.dp), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF243A5E), textAlign = TextAlign.Center)
        }
        Spacer(Modifier.height(14.dp))
        q.options.shuffled().forEach { option ->
            val chosen = selected == option
            Button(
                onClick = {
                    if (!answered) {
                        answered = true
                        selected = option
                        if (option == q.answer) {
                            score++
                            scope.launch { store.addReward(5, 5) }
                        }
                    }
                },
                enabled = !answered,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        !answered -> Color.White
                        chosen && option == q.answer -> Color(0xFF20B957)
                        chosen -> Color(0xFFE94055)
                        option == q.answer -> Color(0xFF20B957)
                        else -> Color.White
                    },
                    contentColor = if (answered && (chosen || option == q.answer)) Color.White else Color(0xFF243A5E)
                )
            ) { Text(option, Modifier.padding(vertical = 5.dp), fontSize = 18.sp, fontWeight = FontWeight.SemiBold) }
        }
        if (answered) {
            Spacer(Modifier.height(10.dp))
            Text(if (selected == q.answer) "🎉 Correct! +5 XP +5 Coins" else "💡 Answer: " + q.answer, fontWeight = FontWeight.Bold, color = if (selected == q.answer) Color(0xFF159447) else Color(0xFFD52E45))
            Spacer(Modifier.height(8.dp))
            Button(onClick = { if (index == questions.lastIndex) finished = true else { index++; selected = null; answered = false } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Text(if (index == questions.lastIndex) "🏆 Finish" else "Next →", fontSize = 17.sp)
            }
        }
        Spacer(Modifier.weight(1f))
        Text("Score: " + score, fontWeight = FontWeight.Bold, color = Color(0xFF60758A))
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun ArifaBrainGamesScreen(onBack: () -> Unit) {
    val q = listOf(
        Level2Question("Which number is missing? 2, 4, 6, ?", listOf("7","8","9","10"), "8"),
        Level2Question("Which one does NOT belong?", listOf("Apple","Banana","Carrot","Mango"), "Carrot"),
        Level2Question("Complete: 5, 10, 15, ?", listOf("18","20","25","30"), "20"),
        Level2Question("Which is the odd one out?", listOf("Circle","Triangle","Square","Blue"), "Blue"),
        Level2Question("If today is Monday, what is tomorrow?", listOf("Sunday","Tuesday","Friday","Saturday"), "Tuesday"),
        Level2Question("Which number comes next? 3, 6, 9, ?", listOf("10","11","12","13"), "12"),
        Level2Question("Which is different?", listOf("Dog","Cat","Cow","Rose"), "Rose"),
        Level2Question("2 + 2 × 2 = ?", listOf("6","8","4","10"), "6"),
        Level2Question("Which shape has 3 sides?", listOf("Circle","Square","Triangle","Rectangle"), "Triangle"),
        Level2Question("Complete: A, C, E, ?", listOf("F","G","H","I"), "G")
    )
    Level2Quiz("Brain Games", "🧠", Color(0xFF6A1B9A), Color(0xFFF3E5FF), q, onBack)
}

@Composable
fun ArifaReadingAdventureScreen(onBack: () -> Unit) {
    val q = listOf(
        Level2Question("Mina planted a small seed and watered it every day. What did Mina plant?", listOf("A toy","A seed","A book","A ball"), "A seed"),
        Level2Question("Rafi saw dark clouds and took his umbrella. Why?", listOf("It was sunny","He expected rain","He wanted to play","He was hungry"), "He expected rain"),
        Level2Question("A little bird collected dry grass and twigs. Where did it build its nest?", listOf("In a pond","Under a car","In a tree","On a road"), "In a tree"),
        Level2Question("Sara packed a sandwich, water and a hat before going to the park. Where was Sara going?", listOf("The park","The hospital","The library","The shop"), "The park"),
        Level2Question("Tom had three red balloons. One flew away. How many remained?", listOf("1","2","3","4"), "2")
    )
    Level2Quiz("Reading Adventure", "📖", Color(0xFF1565C0), Color(0xFFE3F2FD), q, onBack)
}

@Composable
fun ArifaWritingPracticeScreen(onBack: () -> Unit) {
    val q = listOf(
        Level2Question("Choose the correct spelling.", listOf("BEUTIFUL","BEAUTIFUL","BEUTIFULL","BEATIFUL"), "BEAUTIFUL"),
        Level2Question("Choose the correct sentence.", listOf("She are happy.","She is happy.","She am happy.","She happy is."), "She is happy."),
        Level2Question("Unscramble: CAT / THE / IS / SMALL", listOf("The cat is small.","Small cat the is.","Is the small cat.","Cat is the small."), "The cat is small."),
        Level2Question("Choose the correct word: I ___ a book.", listOf("read","reads","reading","reader"), "read"),
        Level2Question("Which word is a noun?", listOf("Run","Beautiful","School","Quickly"), "School"),
        Level2Question("Choose the plural of CHILD.", listOf("Childs","Children","Childes","Childrens"), "Children"),
        Level2Question("Choose the correct sentence.", listOf("I like apples.","I likes apples.","I liking apples.","I is like apples."), "I like apples."),
        Level2Question("Which word completes: The sun is ___?", listOf("bright","brightly","brightness","brights"), "bright"),
        Level2Question("Choose the opposite of BIG.", listOf("Tall","Small","Long","Wide"), "Small"),
        Level2Question("Which word rhymes with CAT?", listOf("Sun","Hat","Dog","Pen"), "Hat")
    )
    Level2Quiz("Writing Practice", "✍️", Color(0xFF00897B), Color(0xFFE0F7F4), q, onBack)
}

@Composable
fun ArifaAdvancedMathScreen(onBack: () -> Unit) {
    val q = remember {
        val list = mutableListOf<Level2Question>()
        repeat(3) {
            val a = Random.nextInt(2, 10); val b = Random.nextInt(2, 10); val n = a * b
            list += Level2Question(a.toString() + " × " + b + " = ?", listOf(n.toString(),(n+2).toString(),(n-2).coerceAtLeast(0).toString(),(n+5).toString()), n.toString())
        }
        repeat(2) {
            val quotient = Random.nextInt(2, 10); val divisor = Random.nextInt(2, 10); val n = quotient * divisor
            list += Level2Question(n.toString() + " ÷ " + divisor + " = ?", listOf(quotient.toString(),(quotient+1).toString(),(quotient+2).toString(),(quotient-1).coerceAtLeast(0).toString()), quotient.toString())
        }
        list += Level2Question("What is 1/2 of 10?", listOf("2","5","6","10"), "5")
        list += Level2Question("What time is 3:00?", listOf("Three o'clock","Six o'clock","Twelve o'clock","Nine o'clock"), "Three o'clock")
        list += Level2Question("You have ₹20 and spend ₹7. How much is left?", listOf("₹10","₹12","₹13","₹14"), "₹13")
        list += Level2Question("Which is greater?", listOf("3/4","1/4","2/4","1/2"), "3/4")
        list += Level2Question("A pencil costs ₹5. How much do 4 pencils cost?", listOf("₹10","₹15","₹20","₹25"), "₹20")
        list
    }
    Level2Quiz("Advanced Maths", "🔢", Color(0xFF2457A6), Color(0xFFEAF2FF), q, onBack)
}

@Composable
fun ArifaWorldExplorerScreen(onBack: () -> Unit) {
    val q = listOf(
        Level2Question("What is the capital of India?", listOf("Mumbai","New Delhi","Kolkata","Chennai"), "New Delhi"),
        Level2Question("How many continents are there?", listOf("5","6","7","8"), "7"),
        Level2Question("Which is the largest ocean?", listOf("Indian","Atlantic","Pacific","Arctic"), "Pacific"),
        Level2Question("Which planet is called the Red Planet?", listOf("Earth","Mars","Venus","Jupiter"), "Mars"),
        Level2Question("Which animal is known as the King of the Jungle?", listOf("Tiger","Lion","Elephant","Horse"), "Lion"),
        Level2Question("Which organ helps us breathe?", listOf("Heart","Lungs","Stomach","Brain"), "Lungs"),
        Level2Question("Which season is usually the coldest?", listOf("Summer","Winter","Spring","Rainy"), "Winter"),
        Level2Question("What do bees make?", listOf("Milk","Honey","Bread","Juice"), "Honey"),
        Level2Question("Which is a natural satellite of Earth?", listOf("Sun","Mars","Moon","Venus"), "Moon"),
        Level2Question("Which state is Kolkata in?", listOf("Bihar","West Bengal","Odisha","Assam"), "West Bengal")
    )
    Level2Quiz("World Explorer", "🌍", Color(0xFFE67E22), Color(0xFFFFF1DE), q, onBack)
}
