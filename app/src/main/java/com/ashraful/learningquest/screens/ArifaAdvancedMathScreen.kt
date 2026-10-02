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
WordProblem("S1Q01","Arifa has 36 stickers. She gets 17 more, then gives 12 to her friend. How many now?","আরিফার কাছে ৩৬টি স্টিকার ছিল। সে আরও ১৭টি স্টিকার পেল। তারপর ১২টি স্টিকার বন্ধুকে দিয়ে দিল। এখন আরিফার কাছে মোট কতটি স্টিকার আছে?",41,"36 + 17 − 12 = 41"),
WordProblem("S1Q02","There are 5 baskets with 8 apples in each. 7 apples are eaten. How many remain?","৫টি ঝুড়িতে প্রতিটিতে ৮টি করে আপেল আছে। তার মধ্যে ৭টি আপেল খাওয়া হয়ে গেল। এখন মোট কতটি আপেল বাকি আছে?",33,"5 × 8 − 7 = 33"),
WordProblem("S1Q03","A book has 72 pages. Arifa reads 18 Monday and 24 Tuesday. How many remain?","একটি বইয়ে মোট ৭২টি পৃষ্ঠা আছে। আরিফা সোমবার ১৮টি এবং মঙ্গলবার ২৪টি পৃষ্ঠা পড়ল। এখন বইটিতে কতটি পৃষ্ঠা পড়া বাকি আছে?",30,"72 − 18 − 24 = 30"),
WordProblem("S1Q04","A toy costs ₹45. Arifa has ₹70. How much is left?","একটি খেলনার দাম ৪৫ টাকা। আরিফার কাছে ৭০ টাকা আছে। খেলনাটি কেনার পরে তার কাছে কত টাকা বাকি থাকবে?",25,"70 − 45 = 25"),
WordProblem("S1Q05","There are 4 rows with 9 chairs each. 6 more are added. How many chairs?","৪টি সারিতে প্রতিটি সারিতে ৯টি করে চেয়ার আছে। পরে আরও ৬টি চেয়ার আনা হলো। এখন মোট কতটি চেয়ার আছে?",42,"4 × 9 + 6 = 42"),
WordProblem("S1Q06","A train leaves at 9 and travels for 3 hours. What time does it arrive?","একটি ট্রেন সকাল ৯টায় যাত্রা শুরু করল। ট্রেনটি ৩ ঘণ্টা চলল। ট্রেনটি কখন গন্তব্যে পৌঁছাবে?",12,"9 + 3 = 12"),
WordProblem("S1Q07","Rina has 50 beads and makes 5 equal bracelets. How many per bracelet?","রিনার কাছে ৫০টি পুঁতি আছে। সে এগুলি দিয়ে ৫টি সমান ব্রেসলেট বানাবে। প্রতিটি ব্রেসলেটে কতটি পুঁতি লাগবে?",10,"50 ÷ 5 = 10"),
WordProblem("S1Q08","A farmer has 28 red flowers and twice as many yellow flowers. How many yellow?","একজন কৃষকের কাছে ২৮টি লাল ফুল আছে। তার হলুদ ফুলের সংখ্যা লাল ফুলের দ্বিগুণ। কৃষকের কাছে কতটি হলুদ ফুল আছে?",56,"28 × 2 = 56"),
WordProblem("S1Q09","A box has 60 pencils. 15 are red and 18 blue. The rest are green. How many green?","একটি বাক্সে মোট ৬০টি পেন্সিল আছে। তার মধ্যে ১৫টি লাল এবং ১৮টি নীল পেন্সিল। বাকি পেন্সিলগুলি সবুজ। বাক্সে কতটি সবুজ পেন্সিল আছে?",27,"60 − 15 − 18 = 27"),
WordProblem("S1Q10","A number is 9 more than 27. What is it?","একটি সংখ্যা ২৭-এর চেয়ে ৯ বেশি। সেই সংখ্যাটি কত?",36,"27 + 9 = 36")
),
listOf(
WordProblem("S2Q01","There are 48 chocolates. 9 are given away and the rest shared among 3 friends. How many each?","একটি বাক্সে ৪৮টি চকোলেট ছিল। তার মধ্যে ৯টি দেওয়া হয়ে গেল। বাকি চকোলেট ৩ জন বন্ধুর মধ্যে সমান ভাগ করে দেওয়া হলো। প্রত্যেকে কতটি চকোলেট পেল?",13,"48 − 9 = 39; 39 ÷ 3 = 13"),
WordProblem("S2Q02","A shop has 7 packets with 6 pencils each. It sells 19. How many remain?","একটি দোকানে ৭টি প্যাকেট আছে। প্রতিটি প্যাকেটে ৬টি করে পেন্সিল আছে। দোকান থেকে ১৯টি পেন্সিল বিক্রি হয়ে গেল। এখন কতটি পেন্সিল বাকি আছে?",23,"7 × 6 − 19 = 23"),
WordProblem("S2Q03","Take 4 times 7 and add 5. What is the result?","৭-এর ৪ গুণ কত? সেই সংখ্যার সঙ্গে ৫ যোগ করলে মোট কত হবে?",33,"4 × 7 + 5 = 33"),
WordProblem("S2Q04","Arifa has ₹100. She buys a ₹15 pencil and ₹38 notebook. What change?","আরিফার কাছে ১০০ টাকা ছিল। সে ১৫ টাকার একটি পেন্সিল এবং ৩৮ টাকার একটি খাতা কিনল। কেনাকাটার পরে তার কাছে কত টাকা বাকি থাকবে?",47,"100 − (15 + 38) = 47"),
WordProblem("S2Q05","A bus has 32 passengers. 14 get off and 9 get on. How many now?","একটি বাসে ৩২ জন যাত্রী ছিল। একটি স্টপে ১৪ জন নেমে গেল এবং ৯ জন নতুন করে বাসে উঠল। এখন বাসে মোট কতজন যাত্রী আছে?",27,"32 − 14 + 9 = 27"),
WordProblem("S2Q06","A ribbon is 95 cm. 28 cm and then 17 cm are cut. How much remains?","একটি ফিতা ৯৫ সেমি লম্বা। প্রথমে ২৮ সেমি এবং পরে আরও ১৭ সেমি ফিতা কেটে নেওয়া হলো। এখন কত সেমি ফিতা বাকি আছে?",50,"95 − 28 − 17 = 50"),
WordProblem("S2Q07","Three children have 14 marbles each. All are shared equally among 7 children. How many each?","৩ জন শিশুর প্রত্যেকের কাছে ১৪টি করে মার্বেল আছে। সব মার্বেল ৭ জন শিশুর মধ্যে সমান ভাগ করে দেওয়া হলো। প্রত্যেকে কতটি মার্বেল পেল?",6,"3 × 14 = 42; 42 ÷ 7 = 6"),
WordProblem("S2Q08","The pattern is 4, 8, 12, 16, __. What comes next?","একটি সংখ্যার ধারা হলো ৪, ৮, ১২, ১৬, ...। প্রতিবার ৪ করে বাড়ছে। তাহলে পরের সংখ্যাটি কত হবে?",20,"Add 4 each time"),
WordProblem("S2Q09","A class has 26 students. There are 8 more girls than boys. How many boys?","একটি শ্রেণিতে মোট ২৬ জন ছাত্রছাত্রী আছে। মেয়েদের সংখ্যা ছেলেদের চেয়ে ৮ জন বেশি। শ্রেণিতে কতজন ছেলে আছে?",9,"9 boys + 17 girls = 26"),
WordProblem("S2Q10","A game starts at 2:30 and lasts 1 hour 45 minutes. What time does it finish?","একটি খেলা দুপুর ২টা ৩০ মিনিটে শুরু হলো। খেলাটি ১ ঘণ্টা ৪৫ মিনিট চলল। খেলা কখন শেষ হবে?",415,"2:30 + 1:45 = 4:15")
),
listOf(
WordProblem("S3Q01","I double a number and add 6 to get 30. What is my number?","আমি একটি সংখ্যা ভাবলাম। সেই সংখ্যাকে ২ দিয়ে গুণ করে ৬ যোগ করলে ৩০ হয়। আমি কোন সংখ্যাটি ভেবেছিলাম?",12,"2 × number + 6 = 30; number = 12"),
WordProblem("S3Q02","There are 6 chickens and cows altogether and 16 legs. How many cows?","একটি খামারে মোট ৬টি মুরগি ও গরু আছে। সব প্রাণীর পা গুনে মোট ১৬টি পাওয়া গেল। সেখানে কতটি গরু আছে?",2,"Six chickens have 12 legs; 4 extra legs means 2 cows"),
WordProblem("S3Q03","Red beads are 3 times blue beads. Altogether there are 32. How many blue?","লাল পুঁতির সংখ্যা নীল পুঁতির ৩ গুণ। লাল ও নীল পুঁতি মিলিয়ে মোট ৩২টি। তাহলে নীল পুঁতি কতটি?",8,"4 equal parts; 32 ÷ 4 = 8"),
WordProblem("S3Q04","A number is between 40 and 60, even, divisible by 4, and its digits add to 10. What is it?","৪০ থেকে ৬০-এর মধ্যে একটি জোড় সংখ্যা আছে। সংখ্যাটি ৪ দিয়ে নিঃশেষে ভাগ যায় এবং তার দুটি অঙ্কের যোগফল ১০। সংখ্যাটি কত?",46,"46 satisfies all clues"),
WordProblem("S3Q05","24 students form teams of 3. Each team needs 2 balls. How many balls?","২৪ জন ছাত্রছাত্রীকে ৩ জন করে দলে ভাগ করা হলো। প্রতিটি দলের জন্য ২টি করে বল দরকার। সব দলের জন্য মোট কতটি বল লাগবে?",16,"24 ÷ 3 = 8; 8 × 2 = 16"),
WordProblem("S3Q06","A shopkeeper has ₹200. He buys 3 toys at ₹35 each and a ₹42 book. How much remains?","একজন দোকানদারের কাছে ২০০ টাকা ছিল। তিনি ৩৫ টাকা করে ৩টি খেলনা এবং ৪২ টাকার একটি বই কিনলেন। কেনাকাটার পরে কত টাকা বাকি থাকবে?",53,"3 × 35 + 42 = 147; 200 − 147 = 53"),
WordProblem("S3Q07","The pattern is 2, 5, 10, 17, 26, __. What comes next?","একটি সংখ্যার ধারা হলো ২, ৫, ১০, ১৭, ২৬, ...। প্রতিবার বাড়ার পরিমাণ ৩, ৫, ৭, ৯ করে বাড়ছে। তাহলে পরের সংখ্যাটি কত?",37,"Add 3, 5, 7, 9, then 11"),
WordProblem("S3Q08","A 52 cm rope is cut into 4 equal pieces. Then 7 cm is cut from one piece. How much remains?","একটি ৫২ সেমি লম্বা দড়ি ৪টি সমান টুকরো করা হলো। এরপর একটি টুকরো থেকে ৭ সেমি কেটে নেওয়া হলো। সব মিলিয়ে এখন কত সেমি দড়ি বাকি আছে?",45,"52 − 7 = 45 cm"),
WordProblem("S3Q09","Two numbers add to 50. One is 14 more than the other. What is the smaller?","দুটি সংখ্যার যোগফল ৫০। একটি সংখ্যা অন্য সংখ্যার চেয়ে ১৪ বেশি। ছোট সংখ্যাটি কত?",18,"18 + 32 = 50; 32 − 18 = 14"),
WordProblem("S3Q10","A 3-digit number has 4 in the hundreds place. Tens is 2 more than ones. Digits add to 12. What is it?","একটি তিন অঙ্কের সংখ্যার শতকের অঙ্ক ৪। দশকের অঙ্ক এককের অঙ্কের চেয়ে ২ বেশি। তিনটি অঙ্কের যোগফল ১২। সংখ্যাটি কত?",453,"4 + 5 + 3 = 12 and 5 = 3 + 2")
),
listOf(
WordProblem("S4Q01","There are 3 boxes with 12 pencils in each. 9 pencils are given away. How many remain?","৩টি বাক্সে প্রতিটি বাক্সে ১২টি করে পেন্সিল আছে। সেখান থেকে ৯টি পেন্সিল দিয়ে দেওয়া হলো। এখন মোট কতটি পেন্সিল বাকি আছে?",27,"3 × 12 − 9 = 27"),
WordProblem("S4Q02","Arifa has 84 stickers. She gives 17 each to 3 friends. How many stickers remain?","আরিফার কাছে ৮৪টি স্টিকার ছিল। সে ৩ জন বন্ধুকে প্রত্যেককে ১৭টি করে স্টিকার দিল। আরিফার কাছে এখন কতটি স্টিকার বাকি আছে?",33,"84 − (17 × 3) = 33"),
WordProblem("S4Q03","I multiply a number by 3 and add 5 to get 29. What is the number?","একটি সংখ্যাকে ৩ দিয়ে গুণ করে তার সঙ্গে ৫ যোগ করলে ২৯ হয়। সংখ্যাটি কত?",8,"3 × 8 + 5 = 29"),
WordProblem("S4Q04","The pattern is 3, 7, 13, 21, 31, __. What comes next?","একটি সংখ্যার ধারা হলো ৩, ৭, ১৩, ২১, ৩১, ...। প্রতিবার যথাক্রমে ৪, ৬, ৮, ১০ করে বাড়ছে। তাহলে পরের সংখ্যাটি কত?",43,"Add 4, 6, 8, 10, then 12"),
WordProblem("S4Q05","There are 5 rows with 7 chairs each. 8 more chairs arrive, but 4 are broken. How many usable chairs?","৫টি সারিতে প্রতিটি সারিতে ৭টি করে চেয়ার আছে। পরে আরও ৮টি চেয়ার আনা হলো। কিন্তু তার মধ্যে ৪টি চেয়ার ভেঙে গেল। এখন ব্যবহার করা যাবে এমন চেয়ার কতটি?",39,"5 × 7 + 8 − 4 = 39"),
WordProblem("S4Q06","Arifa has ₹150. She buys 2 toys at ₹37 each and a book for ₹28. How much remains?","আরিফার কাছে ১৫০ টাকা ছিল। সে ৩৭ টাকা করে ২টি খেলনা এবং ২৮ টাকার একটি বই কিনল। কেনাকাটার পরে কত টাকা বাকি থাকবে?",48,"150 − (2 × 37 + 28) = 48"),
WordProblem("S4Q07","A movie starts at 3:20 and lasts 45 minutes. What time does it finish?","একটি সিনেমা বিকেল ৩টা ২০ মিনিটে শুরু হলো। সিনেমাটি ৪৫ মিনিট চলল। সিনেমাটি কখন শেষ হবে?",405,"3:20 + 45 minutes = 4:05"),
WordProblem("S4Q08","There are 7 animals. They have 24 legs altogether. Some are cats and the rest are chickens. How many are cats?","একটি খামারে মোট ৭টি প্রাণী আছে। সব প্রাণীর পা মিলিয়ে ২৪টি। প্রাণীগুলির কিছু বিড়াল এবং বাকিগুলি মুরগি। সেখানে কতটি বিড়াল আছে?",5,"5 × 4 + 2 × 2 = 24"),
WordProblem("S4Q09","Two numbers add to 42. One number is 12 more than the other. What is the smaller number?","দুটি সংখ্যার যোগফল ৪২। একটি সংখ্যা অন্য সংখ্যার চেয়ে ১২ বেশি। ছোট সংখ্যাটি কত?",15,"15 + 27 = 42 and 27 − 15 = 12"),
WordProblem("S4Q10","A 3-digit number has 5 in the hundreds place. The tens digit is 1 less than the ones digit. The digits add to 14. What is the number?","একটি তিন অঙ্কের সংখ্যার শতকের অঙ্ক ৫। দশকের অঙ্ক এককের অঙ্কের চেয়ে ১ কম। তিনটি অঙ্কের যোগফল ১৪। সংখ্যাটি কত?",545,"5 + 4 + 5 = 14 and 4 is 1 less than 5")
),
listOf(
WordProblem("S5Q01","96 candies are shared equally among 4 children. Each child uses 7 candies. How many does each child have left?","৯৬টি ক্যান্ডি ৪ জন শিশুর মধ্যে সমান ভাগ করে দেওয়া হলো। প্রত্যেক শিশু তার পাওয়া ক্যান্ডি থেকে ৭টি ব্যবহার করল। এখন প্রত্যেক শিশুর কাছে কতটি ক্যান্ডি বাকি আছে?",17,"96 ÷ 4 = 24; 24 − 7 = 17"),
WordProblem("S5Q02","Half of a number plus 9 equals 25. What is the number?","একটি সংখ্যার অর্ধেকের সঙ্গে ৯ যোগ করলে ২৫ হয়। সংখ্যাটি কত?",32,"25 − 9 = 16; 16 × 2 = 32"),
WordProblem("S5Q03","Five consecutive numbers add up to 65. What is the middle number?","পরপর ৫টি সংখ্যার যোগফল ৬৫। এই ৫টি সংখ্যার মাঝখানের সংখ্যাটি কত?",13,"65 ÷ 5 = 13"),
WordProblem("S5Q04","Each box holds 6 red balls and 3 blue balls. There are 36 balls altogether. How many boxes are there?","প্রতিটি বাক্সে ৬টি লাল বল এবং ৩টি নীল বল আছে। সব বাক্সে মোট ৩৬টি বল আছে। তাহলে মোট কতটি বাক্স আছে?",4,"6 + 3 = 9; 36 ÷ 9 = 4"),
WordProblem("S5Q05","The pattern is 81, 27, 9, 3, __. What comes next?","একটি সংখ্যার ধারা হলো ৮১, ২৭, ৯, ৩, ...। প্রতিবার আগের সংখ্যাকে ৩ দিয়ে ভাগ করা হচ্ছে। তাহলে পরের সংখ্যাটি কত?",1,"Divide by 3 each time"),
WordProblem("S5Q06","Arifa has ₹500. She buys 4 books at ₹68 each and 2 pens at ₹27 each. How much remains?","আরিফার কাছে ৫০০ টাকা ছিল। সে ৬৮ টাকা করে ৪টি বই এবং ২৭ টাকা করে ২টি কলম কিনল। কেনাকাটার পরে তার কাছে কত টাকা বাকি থাকবে?",174,"500 − (4 × 68 + 2 × 27) = 174"),
WordProblem("S5Q07","A number is 4 more than twice another number. Their sum is 28. What is the smaller number?","একটি সংখ্যা অন্য একটি সংখ্যার দ্বিগুণের চেয়ে ৪ বেশি। এই দুটি সংখ্যার যোগফল ২৮। ছোট সংখ্যাটি কত?",8,"8 + 20 = 28 and 20 = 2 × 8 + 4"),
WordProblem("S5Q08","A class has 30 students. The number of girls is twice the number of boys. How many girls are there?","একটি শ্রেণিতে মোট ৩০ জন ছাত্রছাত্রী আছে। মেয়ের সংখ্যা ছেলের সংখ্যার দ্বিগুণ। শ্রেণিতে মোট কতজন মেয়ে আছে?",20,"3 equal parts; 30 ÷ 3 = 10; girls = 20"),
WordProblem("S5Q09","A 2-digit number has digits that add to 11. The tens digit is 3 more than the ones digit. What is the number?","একটি দুই অঙ্কের সংখ্যার দুটি অঙ্কের যোগফল ১১। দশকের অঙ্কটি এককের অঙ্কের চেয়ে ৩ বেশি। সংখ্যাটি কত?",74,"7 + 4 = 11 and 7 = 4 + 3"),
WordProblem("S5Q10","A rope is 90 cm long. First 1/3 is cut off, then 15 cm more is cut. How many cm remain?","একটি দড়ি ৯০ সেমি লম্বা। প্রথমে দড়িটির এক-তৃতীয়াংশ কেটে নেওয়া হলো। তারপর আরও ১৫ সেমি কেটে নেওয়া হলো। এখন কত সেমি দড়ি বাকি আছে?",45,"90 ÷ 3 = 30; 90 − 30 − 15 = 45")
),
listOf(
WordProblem("S6Q01","A number is multiplied by 4, then 8 is subtracted. The result is 36. What is the number?","একটি সংখ্যাকে ৪ দিয়ে গুণ করা হলো। তারপর সেই ফল থেকে ৮ বিয়োগ করলে ৩৬ হয়। শুরুতে সংখ্যাটি কত ছিল?",11,"4 × 11 − 8 = 36"),
WordProblem("S6Q02","A farmer has chickens and goats. There are 8 animals and 24 legs altogether. How many goats are there?","একজন কৃষকের কাছে মুরগি ও ছাগল মিলিয়ে মোট ৮টি প্রাণী আছে। সব প্রাণীর পা মিলিয়ে মোট ২৪টি। কৃষকের কাছে কতটি ছাগল আছে?",4,"8 chickens would have 16 legs; 8 extra legs means 4 goats"),
WordProblem("S6Q03","Three numbers are 5 apart from each other: 10, 15, 20. Their sum is 45. If each is increased by 3, what is the new total?","তিনটি সংখ্যা হলো ১০, ১৫ এবং ২০। তাদের যোগফল ৪৫। প্রতিটি সংখ্যার সঙ্গে ৩ করে যোগ করলে নতুন তিনটি সংখ্যার যোগফল কত হবে?",54,"Adding 3 to each of 3 numbers adds 9; 45 + 9 = 54"),
WordProblem("S6Q04","A shop has 120 pencils. It packs them equally into boxes of 8. Then 5 boxes are sold. How many pencils remain?","একটি দোকানে ১২০টি পেন্সিল আছে। প্রতি বাক্সে ৮টি করে পেন্সিল ভরা হলো। এরপর ৫টি বাক্স বিক্রি হয়ে গেল। এখন দোকানে কতটি পেন্সিল বাকি আছে?",80,"120 ÷ 8 = 15 boxes; 15 − 5 = 10; 10 × 8 = 80"),
WordProblem("S6Q05","The pattern is 2, 6, 12, 20, 30, __. What comes next?","একটি সংখ্যার ধারা হলো ২, ৬, ১২, ২০, ৩০, ...। প্রতিবার ৪, ৬, ৮, ১০ করে বাড়ছে। তাহলে পরের সংখ্যাটি কত?",42,"Add 4, 6, 8, 10, then 12"),
WordProblem("S6Q06","A bus has 45 passengers. At the first stop 17 get off and 9 get on. At the second stop 8 get off and 6 get on. How many passengers now?","একটি বাসে ৪৫ জন যাত্রী ছিল। প্রথম স্টপে ১৭ জন নেমে গেল এবং ৯ জন উঠল। দ্বিতীয় স্টপে ৮ জন নেমে গেল এবং ৬ জন উঠল। এখন বাসে মোট কতজন যাত্রী আছে?",35,"45 − 17 + 9 − 8 + 6 = 35"),
WordProblem("S6Q07","Two numbers have a total of 64. One number is 3 times the other. What is the smaller number?","দুটি সংখ্যার যোগফল ৬৪। একটি সংখ্যা অন্য সংখ্যার ৩ গুণ। ছোট সংখ্যাটি কত?",16,"16 + 48 = 64"),
WordProblem("S6Q08","A 3-digit number has digits adding to 15. The hundreds digit is 2 more than the tens digit, and the ones digit is 1 more than the tens digit. What is the number?","একটি তিন অঙ্কের সংখ্যার তিনটি অঙ্কের যোগফল ১৫। শতকের অঙ্কটি দশকের অঙ্কের চেয়ে ২ বেশি এবং এককের অঙ্কটি দশকের অঙ্কের চেয়ে ১ বেশি। সংখ্যাটি কত?",654,"5 + 4 + 6 = 15; 5 = 4 + 1 and 6 = 4 + 2"),
WordProblem("S6Q09","A square has 4 equal sides. If its perimeter is 36 cm, what is the length of one side?","একটি বর্গের ৪টি বাহু সমান। বর্গটির পরিসীমা ৩৬ সেমি। তাহলে একটি বাহুর দৈর্ঘ্য কত সেমি?",9,"36 ÷ 4 = 9 cm"),
WordProblem("S6Q10","Arifa thinks of a number. She adds 7, doubles the result, then subtracts 4 to get 30. What is her number?","আরিফা একটি সংখ্যা ভাবল। সেই সংখ্যার সঙ্গে ৭ যোগ করল। তারপর ফলটিকে ২ দিয়ে গুণ করল এবং ৪ বিয়োগ করল। শেষে ৩০ পেল। আরিফা কোন সংখ্যাটি ভেবেছিল?",10,"2 × (number + 7) − 4 = 30; number = 10")
)
)

@Composable
fun ArifaAdvancedMathScreen(onBack: () -> Unit) {
    var selectedSet by rememberSaveable { mutableIntStateOf(-1) }
    var index by rememberSaveable { mutableIntStateOf(0) }
    var answer by rememberSaveable { mutableStateOf("") }
    var checked by rememberSaveable { mutableStateOf(false) }
    var correct by rememberSaveable { mutableStateOf(false) }
    var bengali by rememberSaveable { mutableStateOf(true) }
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
        bengali = true
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
                        bengali = true
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
                    Text("উত্তরটি সঠিক হয়নি। আবার চেষ্টা করুন।",fontSize=17.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick={checked=false;correct=false;answer=""},modifier=Modifier.fillMaxWidth().height(52.dp)) { Text("↻ Try Again",fontWeight=FontWeight.Bold,fontSize=17.sp) }
        }
    }
}
