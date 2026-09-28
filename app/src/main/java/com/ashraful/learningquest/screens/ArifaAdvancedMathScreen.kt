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

private val wordProblems = listOf(
    WordProblem("WP001","Arifa has 24 stickers. Her teacher gives her 18 more. How many stickers does she have now?","আরিফার কাছে ২৪টি স্টিকার আছে। তার শিক্ষক তাকে আরও ১৮টি স্টিকার দিলেন। এখন তার কাছে মোট কতটি স্টিকার আছে?",42,"24 + 18 = 42"),
    WordProblem("WP002","A library has 65 storybooks. Children borrow 27 books. How many storybooks are left?","একটি লাইব্রেরিতে ৬৫টি গল্পের বই আছে। শিশুরা ২৭টি বই ধার নিল। কতগুলি গল্পের বই বাকি রইল?",38,"65 − 27 = 38"),
    WordProblem("WP003","There are 6 boxes. Each box has 7 pencils. How many pencils are there altogether?","৬টি বাক্সে প্রতিটিতে ৭টি করে পেন্সিল আছে। সব মিলিয়ে কতটি পেন্সিল আছে?",42,"6 × 7 = 42"),
    WordProblem("WP004","A teacher has 48 crayons and shares them equally among 6 children. How many crayons does each child get?","একজন শিক্ষকের কাছে ৪৮টি ক্রেয়ন আছে। তিনি ৬ জন শিশুর মধ্যে সমানভাবে ভাগ করে দিলেন। প্রত্যেক শিশু কতটি ক্রেয়ন পেল?",8,"48 ÷ 6 = 8"),
    WordProblem("WP005","A notebook costs ₹35 and a pencil costs ₹12. How much do they cost altogether?","একটি খাতার দাম ৩৫ টাকা এবং একটি পেন্সিলের দাম ১২ টাকা। সব মিলিয়ে কত টাকা লাগবে?",47,"35 + 12 = 47"),
    WordProblem("WP006","A class starts at 10 o'clock and lasts for 2 hours. At what time does it finish?","একটি ক্লাস সকাল ১০টায় শুরু হয় এবং ২ ঘণ্টা চলে। ক্লাসটি কখন শেষ হবে?",12,"10 + 2 hours = 12 o'clock"),
    WordProblem("WP007","A ribbon is 80 cm long. Riya cuts off 25 cm. How many centimetres are left?","একটি ফিতের দৈর্ঘ্য ৮০ সেমি। রিয়া ২৫ সেমি কেটে নিল। কত সেমি ফিতে বাকি রইল?",55,"80 − 25 = 55 cm"),
    WordProblem("WP008","A pizza is cut into 8 equal pieces. Arifa eats 3 pieces. How many pieces are left?","একটি পিজ্জা ৮টি সমান টুকরো করা হলো। আরিফা ৩টি টুকরো খেল। কতটি টুকরো বাকি রইল?",5,"8 − 3 = 5"),
    WordProblem("WP009","There are 18 red balls and 15 blue balls. Then 7 balls are given away. How many balls remain?","১৮টি লাল বল এবং ১৫টি নীল বল আছে। এরপর ৭টি বল দিয়ে দেওয়া হলো। কতটি বল বাকি রইল?",26,"18 + 15 − 7 = 26"),
    WordProblem("WP010","A farmer has 4 rows of plants with 9 plants in each row. He adds 5 more plants. How many plants are there now?","একজন চাষির ৪টি সারিতে প্রতি সারিতে ৯টি করে গাছ আছে। তিনি আরও ৫টি গাছ লাগালেন। এখন মোট কতটি গাছ আছে?",41,"4 × 9 + 5 = 41")
)

@Composable
fun ArifaAdvancedMathScreen(onBack: () -> Unit) {
    var index by rememberSaveable { mutableIntStateOf(0) }
    var answer by rememberSaveable { mutableStateOf("") }
    var checked by rememberSaveable { mutableStateOf(false) }
    var correct by rememberSaveable { mutableStateOf(false) }
    var bengali by rememberSaveable { mutableStateOf(false) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var finished by rememberSaveable { mutableStateOf(false) }
    val q = wordProblems[index]

    if (finished) {
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("🎉🎊🥳", fontSize = 56.sp)
            Text("Advanced Maths Complete!", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
            Text("${score} / ${wordProblems.size} correct", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(18.dp))
            Button(onClick = { index=0; score=0; answer=""; checked=false; correct=false; finished=false }, modifier=Modifier.fillMaxWidth()) { Text("Try Again") }
            OutlinedButton(onClick=onBack, modifier=Modifier.fillMaxWidth()) { Text("Back") }
        }
        return
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), horizontalAlignment=Alignment.CenterHorizontally) {
        Text("🔢 Advanced Maths", fontSize=28.sp, fontWeight=FontWeight.ExtraBold, color=Color(0xFF2457A6))
        Text("Word Problems • Write Your Answer", fontSize=14.sp, color=Color(0xFF60758A))
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
            Text("Problem ${index+1} / ${wordProblems.size}", fontWeight=FontWeight.Bold)
            Text("⭐ ${score}", fontWeight=FontWeight.Bold)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick={bengali=!bengali}) { Text(if(bengali) "English দেখুন" else "বাংলা দেখুন") }

        Card(Modifier.fillMaxWidth().padding(top=10.dp), colors=CardDefaults.cardColors(Color(0xFFF3F7FF))) {
            Text(if(bengali) q.bengali else q.english, Modifier.padding(20.dp), fontSize=20.sp, fontWeight=FontWeight.Bold, lineHeight=30.sp)
        }

        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value=answer,
            onValueChange={ if(!checked) answer=it.filter(Char::isDigit) },
            enabled=!checked,
            modifier=Modifier.fillMaxWidth(),
            label={Text(if(bengali) "উত্তর লিখুন" else "Write your answer")},
            placeholder={Text("e.g. 42")},
            keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Number),
            singleLine=true,
            textStyle=LocalTextStyle.current.copy(fontSize=24.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
        )
        Spacer(Modifier.height(12.dp))

        if(!checked) {
            Button(
                onClick={ correct=answer.toIntOrNull()==q.answer; checked=true; if(correct) score++ },
                enabled=answer.isNotBlank(),
                modifier=Modifier.fillMaxWidth().height(54.dp)
            ) { Text(if(bengali) "✓ উত্তর যাচাই করুন" else "✓ Check Answer", fontSize=17.sp, fontWeight=FontWeight.Bold) }
        } else {
            if(correct) {
                Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(Color(0xFFFFF3D9))) {
                    Column(Modifier.fillMaxWidth().padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally) {
                        Text("🎉 🎊 🥳",fontSize=36.sp)
                        Text(if(bengali) "অভিনন্দন!" else "CONGRATULATIONS!",fontSize=22.sp,fontWeight=FontWeight.ExtraBold)
                        Text(if(bengali) "দারুণ! সঠিক উত্তর!" else "Great job! You got it right!",fontWeight=FontWeight.Bold)
                        Text(q.explanation,Modifier.padding(top=6.dp))
                    }
                }
                Spacer(Modifier.height(12.dp))
                Button(onClick={
                    if(index==wordProblems.lastIndex) finished=true
                    else { index++; answer=""; checked=false; correct=false; bengali=false }
                },modifier=Modifier.fillMaxWidth().height(52.dp)) {
                    Text(if(index==wordProblems.lastIndex) "🏆 Finish" else "Next Problem →",fontWeight=FontWeight.Bold,fontSize=17.sp)
                }
            } else {
                Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(Color(0xFFFFE8E8))) {
                    Column(Modifier.fillMaxWidth().padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally) {
                        Text("❌",fontSize=32.sp)
                        Text(if(bengali) "ভুল উত্তর। আবার চেষ্টা করো!" else "Wrong answer. Try again!",fontSize=19.sp,fontWeight=FontWeight.ExtraBold,color=Color(0xFFC62828))
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick={checked=false;correct=false;answer=""},modifier=Modifier.fillMaxWidth().height(52.dp)) {
                    Text("↻ Try Again",fontWeight=FontWeight.Bold,fontSize=17.sp)
                }
            }
        }
    }
}
