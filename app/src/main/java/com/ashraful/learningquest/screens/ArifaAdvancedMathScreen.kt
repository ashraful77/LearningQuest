package com.ashraful.learningquest.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class WordProblem(
    val id: String,
    val english: String,
    val bengali: String,
    val answer: Int,
    val explanation: String
)

private val advancedMathSets = listOf(
listOf(
WordProblem("S1Q01","Arifa has 36 stickers. She gets 17 more, then gives 12 to her friend. How many now?","আরিফার কাছে ৩৬টি স্টিকার আছে। আরও ১৭টি পেল, ১২টি বন্ধুকে দিল। এখন কতটি?",41,"36 + 17 − 12 = 41"),
WordProblem("S1Q02","There are 5 baskets with 8 apples in each. 7 apples are eaten. How many remain?","৫টি ঝুড়িতে ৮টি করে আপেল। ৭টি খাওয়া হলো। কতটি বাকি?",33,"5 × 8 − 7 = 33"),
WordProblem("S1Q03","A book has 72 pages. Arifa reads 18 Monday and 24 Tuesday. How many remain?","একটি বইয়ে ৭২টি পৃষ্ঠা। সোমবার ১৮টি ও মঙ্গলবার ২৪টি পড়ল। কতটি বাকি?",30,"72 − 18 − 24 = 30"),
WordProblem("S1Q04","A toy costs ₹45. Arifa has ₹70. How much is left?","খেলনার দাম ৪৫ টাকা। আরিফার ৭০ টাকা। কত বাকি?",25,"70 − 45 = 25"),
WordProblem("S1Q05","There are 4 rows with 9 chairs each. 6 more are added. How many chairs?","৪ সারিতে ৯টি করে চেয়ার। আরও ৬টি যোগ হলো। মোট কত?",42,"4 × 9 + 6 = 42"),
WordProblem("S1Q06","A train leaves at 9 and travels for 3 hours. What time does it arrive?","ট্রেন ৯টায় ছাড়ে এবং ৩ ঘণ্টা চলে। কখন পৌঁছাবে?",12,"9 + 3 = 12"),
WordProblem("S1Q07","Rina has 50 beads and makes 5 equal bracelets. How many per bracelet?","রিনার ৫০টি পুঁতি। ৫টি সমান ব্রেসলেট। প্রতিটিতে কতটি?",10,"50 ÷ 5 = 10"),
WordProblem("S1Q08","A farmer has 28 red flowers and twice as many yellow flowers. How many yellow?","২৮টি লাল ফুল এবং তার দ্বিগুণ হলুদ ফুল। হলুদ কতটি?",56,"28 × 2 = 56"),
WordProblem("S1Q09","A box has 60 pencils. 15 are red and 18 blue. The rest are green. How many green?","৬০টি পেন্সিলের ১৫টি লাল ও ১৮টি নীল। বাকি সবুজ। সবুজ কত?",27,"60 − 15 − 18 = 27"),
WordProblem("S1Q10","A number is 9 more than 27. What is it?","একটি সংখ্যা ২৭-এর চেয়ে ৯ বেশি। কত?",36,"27 + 9 = 36")
),
listOf(
WordProblem("S2Q01","There are 48 chocolates. 9 are given away and the rest shared among 3 friends. How many each?","৪৮টি চকোলেট। ৯টি দেওয়া হলো, বাকিগুলি ৩ বন্ধুর মধ্যে ভাগ। প্রত্যেকে কত?",13,"48 − 9 = 39; 39 ÷ 3 = 13"),
WordProblem("S2Q02","A shop has 7 packets with 6 pencils each. It sells 19. How many remain?","৭ প্যাকেটে ৬টি করে পেন্সিল। ১৯টি বিক্রি হলো। কত বাকি?",23,"7 × 6 − 19 = 23"),
WordProblem("S2Q03","Take 4 times 7 and add 5. What is the result?","৭-এর ৪ গুণ নিয়ে ৫ যোগ করলে ফল কত?",33,"4 × 7 + 5 = 33"),
WordProblem("S2Q04","Arifa has ₹100. She buys a ₹15 pencil and ₹38 notebook. What change?","আরিফার ১০০ টাকা। ১৫ টাকার পেন্সিল ও ৩৮ টাকার খাতা কিনল। ফেরত কত?",47,"100 − (15 + 38) = 47"),
WordProblem("S2Q05","A bus has 32 passengers. 14 get off and 9 get on. How many now?","বাসে ৩২ জন। ১৪ জন নামল ও ৯ জন উঠল। এখন কত?",27,"32 − 14 + 9 = 27"),
WordProblem("S2Q06","A ribbon is 95 cm. 28 cm and then 17 cm are cut. How much remains?","ফিতা ৯৫ সেমি। ২৮ সেমি ও পরে ১৭ সেমি কাটা হলো। কত বাকি?",50,"95 − 28 − 17 = 50"),
WordProblem("S2Q07","Three children have 14 marbles each. All are shared equally among 7 children. How many each?","৩ শিশুর ১৪টি করে মার্বেল। সব ৭ জনে সমান ভাগ। প্রত্যেকে কত?",6,"3 × 14 = 42; 42 ÷ 7 = 6"),
WordProblem("S2Q08","The pattern is 4, 8, 12, 16, __. What comes next?","ধারা ৪, ৮, ১২, ১৬, __। পরের সংখ্যা?",20,"Add 4 each time"),
WordProblem("S2Q09","A class has 26 students. There are 8 more girls than boys. How many boys?","ক্লাসে ২৬ জন। মেয়ে ছেলেদের চেয়ে ৮ জন বেশি। ছেলে কত?",9,"9 boys + 17 girls = 26"),
WordProblem("S2Q10","A game starts at 2:30 and lasts 1 hour 45 minutes. What time does it finish?","খেলা ২:৩০-এ শুরু হয় এবং ১ ঘণ্টা ৪৫ মিনিট চলে। কখন শেষ?",415,"2:30 + 1:45 = 4:15")
),
listOf(
WordProblem("S3Q01","I double a number and add 6 to get 30. What is my number?","একটি সংখ্যাকে দ্বিগুণ করে ৬ যোগ করলে ৩০ হয়। সংখ্যাটি কত?",12,"2 × number + 6 = 30; number = 12"),
WordProblem("S3Q02","There are 6 chickens and cows altogether and 16 legs. How many cows?","মুরগি ও গরু মিলিয়ে ৬টি প্রাণী, মোট ১৬টি পা। গরু কতটি?",2,"Six chickens have 12 legs; 4 extra legs means 2 cows"),
WordProblem("S3Q03","Red beads are 3 times blue beads. Altogether there are 32. How many blue?","লাল পুঁতি নীলের ৩ গুণ। মোট ৩২টি। নীল কত?",8,"4 equal parts; 32 ÷ 4 = 8"),
WordProblem("S3Q04","A number is between 40 and 60, even, divisible by 4, and its digits add to 10. What is it?","৪০ ও ৬০-এর মধ্যে একটি জোড় সংখ্যা, ৪ দিয়ে বিভাজ্য, অঙ্কের যোগ ১০। কত?",46,"46 satisfies all clues"),
WordProblem("S3Q05","24 students form teams of 3. Each team needs 2 balls. How many balls?","২৪ জন ৩ জন করে দলে ভাগ। প্রতিটি দলের ২টি বল। মোট বল?",16,"24 ÷ 3 = 8; 8 × 2 = 16"),
WordProblem("S3Q06","A shopkeeper has ₹200. He buys 3 toys at ₹35 each and a ₹42 book. How much remains?","দোকানদারের ২০০ টাকা। ৩টি ৩৫ টাকার খেলনা ও ৪২ টাকার বই। কত বাকি?",53,"3 × 35 + 42 = 147; 200 − 147 = 53"),
WordProblem("S3Q07","The pattern is 2, 5, 10, 17, 26, __. What comes next?","ধারা ২, ৫, ১০, ১৭, ২৬, __। পরের সংখ্যা?",37,"Add 3, 5, 7, 9, then 11"),
WordProblem("S3Q08","A 52 cm rope is cut into 4 equal pieces. Then 7 cm is cut from one piece. How much remains?","৫২ সেমি দড়ি ৪ সমান টুকরো। একটি থেকে ৭ সেমি কাটা হলো। মোট কত বাকি?",45,"52 − 7 = 45 cm"),
WordProblem("S3Q09","Two numbers add to 50. One is 14 more than the other. What is the smaller?","দুটি সংখ্যার যোগ ৫০। একটি অন্যটির চেয়ে ১৪ বেশি। ছোটটি কত?",18,"18 + 32 = 50; 32 − 18 = 14"),
WordProblem("S3Q10","A 3-digit number has 4 in the hundreds place. Tens is 2 more than ones. Digits add to 13. What is it?","৩ অঙ্কের সংখ্যায় শতকের অঙ্ক ৪। দশক এককের চেয়ে ২ বেশি। অঙ্কের যোগ ১৩। সংখ্যাটি?",463,"4 + 6 + 3 = 13 and 6 = 3 + 2")
)
)

@Composable
fun ArifaAdvancedMathScreen(onBack: () -> Unit) {
    var selectedSet by rememberSaveable { mutableIntStateOf(-1) }
    var index by rememberSaveable { mutableIntStateOf(0) }
    var answer by rememberSaveable { mutableStateOf("") }
    var checked by rememberSaveable { mutableStateOf(false) }
    var correct by rememberSaveable { mutableStateOf(false) }
    var bengali by rememberSaveable { mutableStateOf(false) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var finished by rememberSaveable { mutableStateOf(false) }

    if (selectedSet == -1) {
        Column(Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center) {
            Text("🧠 Advanced Maths", fontSize=30.sp, fontWeight=FontWeight.ExtraBold, color=Color(0xFF2457A6))
            Text("Build thinking agility!", fontSize=18.sp, fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(22.dp))
            listOf("SET 1" to "Warm-up Thinking", "SET 2" to "Multi-Step Thinking", "SET 3" to "Challenge Thinking").forEachIndexed { i, pair ->
                Button(onClick={selectedSet=i; index=0; score=0; answer=""; checked=false; correct=false; bengali=false; finished=false}, modifier=Modifier.fillMaxWidth().height(68.dp).padding(vertical=5.dp)) {
                    Column(horizontalAlignment=Alignment.CenterHorizontally) {
                        Text(pair.first, fontSize=20.sp, fontWeight=FontWeight.ExtraBold)
                        Text(pair.second + " • 10 Questions", fontSize=12.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick=onBack, modifier=Modifier.fillMaxWidth()) { Text("Back") }
        }
        return
    }

    val questions = advancedMathSets[selectedSet]
    val q = questions[index]

    if (finished) {
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center) {
            Text("🎉🎊🥳", fontSize=56.sp)
            Text("SET " + (selectedSet + 1) + " Complete!", fontSize=27.sp, fontWeight=FontWeight.ExtraBold)
            Text(score.toString() + " / 10 correct", fontSize=22.sp, fontWeight=FontWeight.Bold)
            Text(if(score >= 8) "Excellent thinking! 🧠" else "Good effort! Try again and beat your score!", fontSize=17.sp, fontWeight=FontWeight.Bold, textAlign=TextAlign.Center)
            Spacer(Modifier.height(18.dp))
            Button(onClick={index=0; score=0; answer=""; checked=false; correct=false; finished=false}, modifier=Modifier.fillMaxWidth()) { Text("Try Set Again") }
            OutlinedButton(onClick={selectedSet=-1; finished=false}, modifier=Modifier.fillMaxWidth()) { Text("Choose Another Set") }
            OutlinedButton(onClick=onBack, modifier=Modifier.fillMaxWidth()) { Text("Back") }
        }
        return
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), horizontalAlignment=Alignment.CenterHorizontally) {
        Text("🧠 Advanced Maths", fontSize=28.sp, fontWeight=FontWeight.ExtraBold, color=Color(0xFF2457A6))
        Text("Set " + (selectedSet + 1) + " • Thinking Agility", fontSize=15.sp, color=Color(0xFF60758A), fontWeight=FontWeight.Bold)
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
            Text("Question " + (index+1) + " / 10", fontWeight=FontWeight.Bold)
            Text("⭐ " + score, fontWeight=FontWeight.Bold)
        }
        OutlinedButton(onClick={bengali=!bengali}) { Text(if(bengali) "English দেখুন" else "বাংলা দেখুন") }
        Card(Modifier.fillMaxWidth().padding(top=8.dp), colors=CardDefaults.cardColors(Color(0xFFF3F7FF))) {
            Text(if(bengali) q.bengali else q.english, Modifier.padding(20.dp), fontSize=20.sp, fontWeight=FontWeight.Bold, lineHeight=30.sp)
        }
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(value=answer, onValueChange={if(!checked) answer=it.filter(Char::isDigit)}, enabled=!checked, modifier=Modifier.fillMaxWidth(), label={Text(if(bengali) "উত্তর লিখুন" else "Write your answer")}, placeholder={Text("e.g. 42")}, keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Number), singleLine=true, textStyle=LocalTextStyle.current.copy(fontSize=24.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center))
        Spacer(Modifier.height(12.dp))
        if(!checked) {
            Button(onClick={correct=answer.toIntOrNull()==q.answer; checked=true; if(correct) score++}, enabled=answer.isNotBlank(), modifier=Modifier.fillMaxWidth().height(54.dp)) { Text(if(bengali) "✓ উত্তর যাচাই করুন" else "✓ Check Answer", fontSize=17.sp, fontWeight=FontWeight.Bold) }
        } else if(correct) {
            Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(Color(0xFFE8F8EE))) {
                Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment=Alignment.CenterHorizontally) {
                    Text("🎉",fontSize=40.sp)
                    Text(if(bengali) "অভিনন্দন! সঠিক উত্তর!" else "🎉 CORRECT!",fontSize=22.sp,fontWeight=FontWeight.ExtraBold,color=Color(0xFF187A45))
                    Text(q.explanation,Modifier.padding(top=6.dp),fontWeight=FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick={if(index==questions.lastIndex) finished=true else {index++;answer="";checked=false;correct=false;bengali=false}}, modifier=Modifier.fillMaxWidth().height(52.dp)) { Text(if(index==questions.lastIndex) "🏆 Finish Set" else "Next Question →",fontWeight=FontWeight.Bold,fontSize=17.sp) }
        } else {
            Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(Color(0xFFFFE8E8))) {
                Column(Modifier.fillMaxWidth().padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally) {
                    Text("❌ WRONG ANSWER",fontSize=22.sp,fontWeight=FontWeight.ExtraBold,color=Color(0xFFC62828))
                    Text(if(bengali) "সঠিক উত্তর: " + q.answer else "Correct answer: " + q.answer,fontSize=18.sp,fontWeight=FontWeight.Bold,color=Color(0xFF23754A))
                    Text(q.explanation,Modifier.padding(top=6.dp),fontWeight=FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick={checked=false;correct=false;answer=""},modifier=Modifier.fillMaxWidth().height(52.dp)) { Text("↻ Try Again",fontWeight=FontWeight.Bold,fontSize=17.sp) }
        }
    }
}
