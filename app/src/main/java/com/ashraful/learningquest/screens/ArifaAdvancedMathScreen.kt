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
import androidx.compose.ui.platform.LocalContext
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
WordProblem("S3Q10","A 3-digit number has 4 in the hundreds place. Tens is 2 more than ones. Digits add to 12. What is it?","৩ অঙ্কের সংখ্যায় শতকের অঙ্ক ৪। দশক এককের চেয়ে ২ বেশি। অঙ্কের যোগ ১২। সংখ্যাটি?",453,"4 + 5 + 3 = 12 and 5 = 3 + 2")
),
listOf(
WordProblem("S4Q01","There are 3 boxes with 12 pencils in each. 9 pencils are given away. How many remain?","৩টি বাক্সে ১২টি করে পেন্সিল। ৯টি দেওয়া হলো। কতটি বাকি?",27,"3 × 12 − 9 = 27"),
WordProblem("S4Q02","Arifa has 84 stickers. She gives 17 each to 3 friends. How many stickers remain?","আরিফার ৮৪টি স্টিকার। ৩ বন্ধুকে ১৭টি করে দিল। কতটি বাকি?",33,"84 − (17 × 3) = 33"),
WordProblem("S4Q03","I multiply a number by 3 and add 5 to get 29. What is the number?","একটি সংখ্যাকে ৩ দিয়ে গুণ করে ৫ যোগ করলে ২৯ হয়। সংখ্যাটি কত?",8,"3 × 8 + 5 = 29"),
WordProblem("S4Q04","The pattern is 3, 7, 13, 21, 31, __. What comes next?","ধারা ৩, ৭, ১৩, ২১, ৩১, __। পরের সংখ্যা?",43,"Add 4, 6, 8, 10, then 12"),
WordProblem("S4Q05","There are 5 rows with 7 chairs each. 8 more chairs arrive, but 4 are broken. How many usable chairs?","৫ সারিতে ৭টি করে চেয়ার। আরও ৮টি এল, কিন্তু ৪টি ভেঙে গেল। ব্যবহারযোগ্য কত?",39,"5 × 7 + 8 − 4 = 39"),
WordProblem("S4Q06","Arifa has ₹150. She buys 2 toys at ₹37 each and a book for ₹28. How much remains?","আরিফার ১৫০ টাকা। ৩৭ টাকা করে ২টি খেলনা ও ২৮ টাকার বই কিনল। কত বাকি?",48,"150 − (2 × 37 + 28) = 48"),
WordProblem("S4Q07","A movie starts at 3:20 and lasts 45 minutes. What time does it finish?","সিনেমা ৩:২০-এ শুরু হয় এবং ৪৫ মিনিট চলে। কখন শেষ হবে?",405,"3:20 + 45 minutes = 4:05"),
WordProblem("S4Q08","There are 7 animals. They have 24 legs altogether. Some are cats and the rest are chickens. How many are cats?","৭টি প্রাণীর মোট ২৪টি পা। কিছু বিড়াল এবং বাকিগুলি মুরগি। বিড়াল কতটি?",5,"5 × 4 + 2 × 2 = 24"),
WordProblem("S4Q09","Two numbers add to 42. One number is 12 more than the other. What is the smaller number?","দুটি সংখ্যার যোগ ৪২। একটি অন্যটির চেয়ে ১২ বেশি। ছোট সংখ্যা কত?",15,"15 + 27 = 42 and 27 − 15 = 12"),
WordProblem("S4Q10","A 3-digit number has 5 in the hundreds place. The tens digit is 1 less than the ones digit. The digits add to 14. What is the number?","৩ অঙ্কের সংখ্যায় শতকের অঙ্ক ৫। দশকের অঙ্ক এককের চেয়ে ১ কম। অঙ্কগুলির যোগ ১৪। সংখ্যাটি কত?",545,"5 + 4 + 5 = 14 and 4 is 1 less than 5")
),
listOf(
WordProblem("S5Q01","96 candies are shared equally among 4 children. Each child uses 7 candies. How many does each child have left?","৯৬টি ক্যান্ডি ৪ শিশুর মধ্যে সমান ভাগ। প্রত্যেকে ৭টি ব্যবহার করল। প্রত্যেকের কতটি বাকি?",17,"96 ÷ 4 = 24; 24 − 7 = 17"),
WordProblem("S5Q02","Half of a number plus 9 equals 25. What is the number?","একটি সংখ্যার অর্ধেকের সঙ্গে ৯ যোগ করলে ২৫ হয়। সংখ্যাটি কত?",32,"25 − 9 = 16; 16 × 2 = 32"),
WordProblem("S5Q03","Five consecutive numbers add up to 65. What is the middle number?","পরপর ৫টি সংখ্যার যোগফল ৬৫। মাঝের সংখ্যা কত?",13,"65 ÷ 5 = 13"),
WordProblem("S5Q04","Each box holds 6 red balls and 3 blue balls. There are 36 balls altogether. How many boxes are there?","প্রতিটি বাক্সে ৬টি লাল ও ৩টি নীল বল। মোট ৩৬টি বল। বাক্স কতটি?",4,"6 + 3 = 9; 36 ÷ 9 = 4"),
WordProblem("S5Q05","The pattern is 81, 27, 9, 3, __. What comes next?","ধারা ৮১, ২৭, ৯, ৩, __। পরের সংখ্যা?",1,"Divide by 3 each time"),
WordProblem("S5Q06","Arifa has ₹500. She buys 4 books at ₹68 each and 2 pens at ₹27 each. How much remains?","আরিফার ৫০০ টাকা। ৬৮ টাকা করে ৪টি বই ও ২৭ টাকা করে ২টি কলম কিনল। কত বাকি?",174,"500 − (4 × 68 + 2 × 27) = 174"),
WordProblem("S5Q07","A number is 4 more than twice another number. Their sum is 28. What is the smaller number?","একটি সংখ্যা অন্যটির দ্বিগুণের চেয়ে ৪ বেশি। তাদের যোগফল ২৮। ছোট সংখ্যা কত?",8,"8 + 20 = 28 and 20 = 2 × 8 + 4"),
WordProblem("S5Q08","A class has 30 students. The number of girls is twice the number of boys. How many girls are there?","ক্লাসে ৩০ জন। মেয়ের সংখ্যা ছেলেদের দ্বিগুণ। মেয়ে কতজন?",20,"3 equal parts; 30 ÷ 3 = 10; girls = 20"),
WordProblem("S5Q09","A 2-digit number has digits that add to 11. The tens digit is 3 more than the ones digit. What is the number?","২ অঙ্কের সংখ্যার অঙ্কগুলির যোগ ১১। দশকের অঙ্ক এককের চেয়ে ৩ বেশি। সংখ্যাটি কত?",74,"7 + 4 = 11 and 7 = 4 + 3"),
WordProblem("S5Q10","A rope is 90 cm long. First 1/3 is cut off, then 15 cm more is cut. How many cm remain?","৯০ সেমি দড়ির প্রথমে এক-তৃতীয়াংশ কাটা হলো, তারপর আরও ১৫ সেমি কাটা হলো। কত সেমি বাকি?",45,"90 ÷ 3 = 30; 90 − 30 − 15 = 45")
),
listOf(
WordProblem("S6Q01","A number is multiplied by 4, then 8 is subtracted. The result is 36. What is the number?","একটি সংখ্যাকে ৪ দিয়ে গুণ করে ৮ বিয়োগ করলে ৩৬ হয়। সংখ্যাটি কত?",11,"4 × 11 − 8 = 36"),
WordProblem("S6Q02","A farmer has chickens and goats. There are 8 animals and 24 legs altogether. How many goats are there?","একজন কৃষকের মুরগি ও ছাগল মিলিয়ে ৮টি প্রাণী। মোট ২৪টি পা। ছাগল কতটি?",4,"8 chickens would have 16 legs; 8 extra legs means 4 goats"),
WordProblem("S6Q03","Three numbers are 5 apart from each other: 10, 15, 20. Their sum is 45. If each is increased by 3, what is the new total?","তিনটি সংখ্যা ৫ করে বাড়ে: ১০, ১৫, ২০। যোগ ৪৫। প্রতিটিতে ৩ যোগ করলে নতুন যোগফল কত?",54,"Adding 3 to each of 3 numbers adds 9; 45 + 9 = 54"),
WordProblem("S6Q04","A shop has 120 pencils. It packs them equally into boxes of 8. Then 5 boxes are sold. How many pencils remain?","দোকানে ১২০টি পেন্সিল। ৮টি করে বাক্সে ভরা হলো। ৫টি বাক্স বিক্রি হলো। কত পেন্সিল বাকি?",80,"120 ÷ 8 = 15 boxes; 15 − 5 = 10; 10 × 8 = 80"),
WordProblem("S6Q05","The pattern is 2, 6, 12, 20, 30, __. What comes next?","ধারা ২, ৬, ১২, ২০, ৩০, __। পরের সংখ্যা?",42,"Add 4, 6, 8, 10, then 12"),
WordProblem("S6Q06","A bus has 45 passengers. At the first stop 17 get off and 9 get on. At the second stop 8 get off and 6 get on. How many passengers now?","বাসে ৪৫ জন। প্রথম স্টপে ১৭ জন নামল ও ৯ জন উঠল। দ্বিতীয় স্টপে ৮ জন নামল ও ৬ জন উঠল। এখন কতজন?",35,"45 − 17 + 9 − 8 + 6 = 35"),
WordProblem("S6Q07","Two numbers have a total of 64. One number is 3 times the other. What is the smaller number?","দুটি সংখ্যার যোগ ৬৪। একটি অন্যটির ৩ গুণ। ছোট সংখ্যা কত?",16,"16 + 48 = 64"),
WordProblem("S6Q08","A 3-digit number has digits adding to 15. The hundreds digit is 2 more than the tens digit, and the ones digit is 1 more than the tens digit. What is the number?","৩ অঙ্কের সংখ্যার অঙ্কগুলির যোগ ১৫। শতকের অঙ্ক দশকের চেয়ে ২ বেশি এবং এককের অঙ্ক দশকের চেয়ে ১ বেশি। সংখ্যাটি কত?",654,"5 + 4 + 6 = 15; 5 = 4 + 1 and 6 = 4 + 2"),
WordProblem("S6Q09","A square has 4 equal sides. If its perimeter is 36 cm, what is the length of one side?","একটি বর্গের ৪টি সমান বাহু। পরিসীমা ৩৬ সেমি হলে এক বাহুর দৈর্ঘ্য কত?",9,"36 ÷ 4 = 9 cm"),
WordProblem("S6Q10","Arifa thinks of a number. She adds 7, doubles the result, then subtracts 4 to get 30. What is her number?","আরিফা একটি সংখ্যা ভাবল। ৭ যোগ করে ফলকে দ্বিগুণ করল, তারপর ৪ বিয়োগ করে ৩০ পেল। সংখ্যাটি কত?",10,"2 × (number + 7) − 4 = 30; number = 10")
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
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("advanced_math_progress", 0) }

    fun isAttempted(setIndex: Int): Boolean =
        prefs.getBoolean("set_${setIndex + 1}_attempted", false)

    fun latestScore(setIndex: Int): Int =
        prefs.getInt("set_${setIndex + 1}_score", 0)

    fun isUnlocked(setIndex: Int): Boolean =
        setIndex == 0 || isAttempted(setIndex - 1)

    fun startSet(setIndex: Int) {
        val questions = advancedMathSets[setIndex]
        val savedIndex = prefs.getInt("set_${setIndex + 1}_progress", 0)
            .coerceIn(0, questions.lastIndex)
        val savedScore = prefs.getInt("set_${setIndex + 1}_progress_score", 0)
            .coerceAtLeast(0)

        selectedSet = setIndex
        index = savedIndex
        score = savedScore
        answer = ""
        checked = false
        correct = false
        bengali = false
        finished = false
    }

    fun saveProgress(nextIndex: Int, currentScore: Int) {
        prefs.edit()
            .putInt("set_${selectedSet + 1}_progress", nextIndex)
            .putInt("set_${selectedSet + 1}_progress_score", currentScore)
            .apply()
    }

    fun completeSet() {
        prefs.edit()
            .putBoolean("set_${selectedSet + 1}_attempted", true)
            .putInt("set_${selectedSet + 1}_score", score)
            .remove("set_${selectedSet + 1}_progress")
            .remove("set_${selectedSet + 1}_progress_score")
            .apply()
        finished=true
    }

    if (selectedSet == -1) {
        Column(Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center) {
            Text("🧠 Advanced Maths", fontSize=30.sp, fontWeight=FontWeight.ExtraBold, color=Color(0xFF2457A6))
            Text("Build thinking agility!", fontSize=18.sp, fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(22.dp))
            listOf(
                "SET 1" to "Warm-up Thinking",
                "SET 2" to "Multi-Step Thinking",
                "SET 3" to "Challenge Thinking",
                "SET 4" to "Logic & Multi-Step",
                "SET 5" to "Reasoning Challenge",
                "SET 6" to "Brain Challenge"
            ).forEachIndexed { i, pair ->
                val attempted = isAttempted(i)
                val unlocked = isUnlocked(i)
                val title = when {
                    !unlocked -> "🔒 " + pair.first
                    attempted -> "✅ " + pair.first
                    else -> "▶ " + pair.first
                }
                Button(
                    onClick={if(unlocked) startSet(i)},
                    enabled=unlocked,
                    modifier=Modifier.fillMaxWidth().height(78.dp).padding(vertical=5.dp)
                ) {
                    Column(horizontalAlignment=Alignment.CenterHorizontally) {
                        Text(title, fontSize=20.sp, fontWeight=FontWeight.ExtraBold)
                        Text(
                            if(!unlocked) "Complete previous set first"
                            else pair.second + " • 10 Questions" +
                                if (prefs.contains("set_${i + 1}_progress"))
                                    " • Resume Q" + (prefs.getInt("set_${i + 1}_progress", 0) + 1)
                                else if (attempted)
                                    " • Score " + latestScore(i) + "/10"
                                else "",
                            fontSize=12.sp
                        )
                        if(attempted) Text("↻ Retest available", fontSize=12.sp, fontWeight=FontWeight.Bold)
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

    DisposableEffect(selectedSet, index) {
        onDispose {
            if (!finished) {
                saveProgress(index, score)
            }
        }
    }

    if (finished) {
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center) {
            Text("🎉🎊🥳", fontSize=56.sp)
            Text("SET " + (selectedSet + 1) + " Complete!", fontSize=27.sp, fontWeight=FontWeight.ExtraBold)
            Text(score.toString() + " / 10 correct", fontSize=22.sp, fontWeight=FontWeight.Bold)
            Text(if(score >= 8) "Excellent thinking! 🧠" else "Good effort! Try again and beat your score!", fontSize=17.sp, fontWeight=FontWeight.Bold, textAlign=TextAlign.Center)
            Spacer(Modifier.height(18.dp))
            Button(onClick={startSet(selectedSet)}, modifier=Modifier.fillMaxWidth()) { Text("↻ Retest Set", fontWeight=FontWeight.Bold) }
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
        OutlinedTextField(value=answer, onValueChange={if(!checked) answer=it.filter(Char::isDigit)}, enabled=!checked, modifier=Modifier.fillMaxWidth(), label={Text(if(bengali) "উত্তর লিখুন" else "Write your answer")}, keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Number), singleLine=true, textStyle=LocalTextStyle.current.copy(fontSize=24.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center))
        Spacer(Modifier.height(12.dp))
        if(!checked) {
            Button(
                onClick = {
                    correct = answer.toIntOrNull() == q.answer
                    checked = true
                    if (correct) {
                        score++
                        saveProgress(index, score)
                    }
                },
                enabled=answer.isNotBlank(),
                modifier=Modifier.fillMaxWidth().height(54.dp)
            ) {
                Text(
                    if(bengali) "✓ উত্তর যাচাই করুন" else "✓ Check Answer",
                    fontSize=17.sp,
                    fontWeight=FontWeight.Bold
                )
            }
        } else if(correct) {
            Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(Color(0xFFE8F8EE))) {
                Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment=Alignment.CenterHorizontally) {
                    Text("🎉",fontSize=40.sp)
                    Text(if(bengali) "অভিনন্দন! সঠিক উত্তর!" else "🎉 CORRECT!",fontSize=22.sp,fontWeight=FontWeight.ExtraBold,color=Color(0xFF187A45))
                    Text(q.explanation,Modifier.padding(top=6.dp),fontWeight=FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    if (index == questions.lastIndex) {
                        completeSet()
                    } else {
                        val nextIndex = index + 1
                        saveProgress(nextIndex, score)
                        index = nextIndex
                        answer = ""
                        checked = false
                        correct = false
                        bengali = false
                    }
                },
                modifier=Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(
                    if(index==questions.lastIndex) "🏆 Finish Set" else "Next Question →",
                    fontWeight=FontWeight.Bold,
                    fontSize=17.sp
                )
            }
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
