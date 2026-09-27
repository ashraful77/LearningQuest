package com.ashraful.learningquest.data

/**
 * Class 3 question expansion for Arifa.
 * 48 curated questions: 16 Math, 16 English, 16 Science.
 */
object QuestionBankExpansion3 {

    val math = listOf(
        BankQuestion("MATH_C3_001","Math","Place Value",3,"What is the place value of 6 in 4,682?",listOf("6","60","600","6000"),2,"6 is in the hundreds place, so its value is 600."),
        BankQuestion("MATH_C3_002","Math","Addition",3,"What is 347 + 256?",listOf("593","603","613","623"),1,"347 + 256 = 603."),
        BankQuestion("MATH_C3_003","Math","Subtraction",3,"What is 700 - 358?",listOf("332","342","352","362"),1,"700 - 358 = 342."),
        BankQuestion("MATH_C3_004","Math","Multiplication",3,"What is 24 × 3?",listOf("62","72","82","92"),1,"24 × 3 = 72."),
        BankQuestion("MATH_C3_005","Math","Division",3,"What is 84 ÷ 4?",listOf("19","20","21","24"),2,"84 ÷ 4 = 21."),
        BankQuestion("MATH_C3_006","Math","Fractions",3,"What fraction of a set of 8 apples is 2 apples?",listOf("1/2","1/4","1/3","3/4"),1,"2 out of 8 is 2/8, which is equal to 1/4."),
        BankQuestion("MATH_C3_007","Math","Fractions",4,"Which fraction is greater?",listOf("1/4","3/4","1/2","1/3"),1,"3/4 is greater than the other fractions."),
        BankQuestion("MATH_C3_008","Math","Time",3,"How many minutes are there in 2 hours?",listOf("60","90","120","180"),2,"2 × 60 = 120 minutes."),
        BankQuestion("MATH_C3_009","Math","Time",4,"School starts at 8:30 and ends at 1:30. How long is the school day?",listOf("4 hours","5 hours","6 hours","7 hours"),1,"From 8:30 to 1:30 is 5 hours."),
        BankQuestion("MATH_C3_010","Math","Money",3,"A book costs ₹125. You pay ₹200. How much change do you get?",listOf("₹65","₹75","₹85","₹95"),1,"200 - 125 = 75."),
        BankQuestion("MATH_C3_011","Math","Measurement",3,"Which unit is best for measuring the weight of a school bag?",listOf("Kilogram","Centimetre","Litre","Metre"),0,"A school bag is commonly measured in kilograms."),
        BankQuestion("MATH_C3_012","Math","Geometry",3,"How many faces does a cube have?",listOf("4","6","8","12"),1,"A cube has 6 faces."),
        BankQuestion("MATH_C3_013","Math","Patterns",3,"What comes next: 100, 90, 80, 70, ?",listOf("50","60","65","75"),1,"The pattern subtracts 10 each time."),
        BankQuestion("MATH_C3_014","Math","Word Problems",4,"There are 6 baskets with 12 oranges in each. How many oranges are there?",listOf("62","72","82","92"),1,"6 × 12 = 72."),
        BankQuestion("MATH_C3_015","Math","Word Problems",4,"A shop had 250 pencils and sold 125. How many pencils are left?",listOf("115","125","135","145"),1,"250 - 125 = 125."),
        BankQuestion("MATH_C3_016","Math","Logic",4,"I am a number greater than 40 and less than 50. My ones digit is 7. What number am I?",listOf("43","47","57","74"),1,"The number is 47." )
    )

    val english = listOf(
        BankQuestion("ENG_C3_001","English","Grammar",3,"Choose the correct sentence.",listOf("She have two cats.","She has two cats.","She having two cats.","She are two cats."),1,"She has two cats is correct."),
        BankQuestion("ENG_C3_002","English","Grammar",3,"Choose the correct plural of 'leaf'.",listOf("Leafs","Leaves","Leafes","Leavs"),1,"The plural of leaf is leaves."),
        BankQuestion("ENG_C3_003","English","Grammar",3,"Which word is a pronoun?",listOf("They","Garden","Beautiful","Jump"),0,"They is a pronoun."),
        BankQuestion("ENG_C3_004","English","Grammar",3,"Which word is an adjective in 'The red ball bounced.'?",listOf("The","red","ball","bounced"),1,"Red describes the ball."),
        BankQuestion("ENG_C3_005","English","Grammar",4,"Which word is a verb in 'Arifa reads a book.'?",listOf("Arifa","reads","book","a"),1,"Reads tells what Arifa does."),
        BankQuestion("ENG_C3_006","English","Grammar",4,"Choose the correct preposition: The cat is ___ the table.",listOf("under","quickly","happy","jump"),0,"Under shows the position of the cat."),
        BankQuestion("ENG_C3_007","English","Grammar",4,"Which punctuation mark should end a question?",listOf(".","!","?",","),2,"A question ends with a question mark."),
        BankQuestion("ENG_C3_008","English","Tenses",3,"What is the past tense of 'play'?",listOf("plays","playing","played","play"),2,"The past tense is played."),
        BankQuestion("ENG_C3_009","English","Tenses",4,"Choose the correct word: Yesterday, I ___ to the park.",listOf("go","goes","went","going"),2,"The past tense of go is went."),
        BankQuestion("ENG_C3_010","English","Vocabulary",3,"Which word means the opposite of 'careful'?",listOf("Careless","Kind","Quiet","Helpful"),0,"Careless is the opposite of careful."),
        BankQuestion("ENG_C3_011","English","Vocabulary",3,"What does 'enormous' mean?",listOf("Very small","Very large","Very slow","Very old"),1,"Enormous means very large."),
        BankQuestion("ENG_C3_012","English","Reading",3,"Rina put on her woollen sweater before going outside. What was the weather probably like?",listOf("Cold","Very hot","Rainy only","Sunny and hot"),0,"A woollen sweater is used in cold weather."),
        BankQuestion("ENG_C3_013","English","Reading",4,"Aman saved ₹20 each week for three weeks. How much did he save?",listOf("₹40","₹50","₹60","₹80"),2,"20 × 3 = 60."),
        BankQuestion("ENG_C3_014","English","Spelling",3,"Which spelling is correct?",listOf("Tomorrow","Tomorow","Tommorow","Tomarrow"),0,"Tomorrow is the correct spelling."),
        BankQuestion("ENG_C3_015","English","Sentence Building",4,"Which sentence is in the correct order?",listOf("Every Arifa reads night.","Arifa reads every night.","Reads Arifa every night.","Every night reads Arifa."),1,"Arifa reads every night is the natural sentence."),
        BankQuestion("ENG_C3_016","English","Vocabulary",4,"Which word is a synonym of 'brave'?",listOf("Afraid","Courageous","Weak","Quiet"),1,"Courageous means brave.")
    )

    val science = listOf(
        BankQuestion("SCI_C3_001","Science","Plants",3,"Which part of a plant carries water from the roots to the leaves?",listOf("Stem","Flower","Fruit","Seed"),0,"The stem carries water and supports the plant."),
        BankQuestion("SCI_C3_002","Science","Plants",3,"What do green leaves use to make food?",listOf("Sunlight","Plastic","Sand","Stones"),0,"Green leaves use sunlight to make food."),
        BankQuestion("SCI_C3_003","Science","Animals",3,"Which animal is a herbivore?",listOf("Lion","Cow","Tiger","Eagle"),1,"A cow mainly eats plants."),
        BankQuestion("SCI_C3_004","Science","Animals",3,"Which animal lays eggs?",listOf("Hen","Cow","Dog","Cat"),0,"A hen lays eggs."),
        BankQuestion("SCI_C3_005","Science","Human Body",3,"Which organ helps us hear sounds?",listOf("Ear","Eye","Nose","Tongue"),0,"The ears help us hear."),
        BankQuestion("SCI_C3_006","Science","Human Body",3,"Which organ helps digest food?",listOf("Stomach","Brain","Heart","Eye"),0,"The stomach helps digest food."),
        BankQuestion("SCI_C3_007","Science","Human Body",4,"Why do we need bones?",listOf("To support the body","To taste food","To hear sounds","To pump blood"),0,"Bones support and give shape to the body."),
        BankQuestion("SCI_C3_008","Science","Light and Sound",3,"Which object gives its own light?",listOf("Sun","Moon","Mirror","Book"),0,"The Sun produces its own light."),
        BankQuestion("SCI_C3_009","Science","Light and Sound",4,"Which material lets most light pass through it?",listOf("Clear glass","Wood","Cardboard","Stone"),0,"Clear glass is transparent."),
        BankQuestion("SCI_C3_010","Science","Force",3,"What happens when you push a stationary toy car?",listOf("It may move","It becomes invisible","It becomes water","Nothing can happen"),0,"A push can make a stationary object move."),
        BankQuestion("SCI_C3_011","Science","Matter",3,"Which change happens when water is put in a freezer?",listOf("It freezes","It boils","It becomes gas immediately","It becomes soil"),0,"Water freezes into ice."),
        BankQuestion("SCI_C3_012","Science","Weather",3,"Which instrument is used to measure temperature?",listOf("Thermometer","Rain gauge","Compass","Ruler"),0,"A thermometer measures temperature."),
        BankQuestion("SCI_C3_013","Science","Environment",3,"Which action saves water?",listOf("Turning off the tap","Leaving the tap open","Wasting water","Playing with running water"),0,"Turning off the tap prevents unnecessary water waste."),
        BankQuestion("SCI_C3_014","Science","Earth",4,"Which layer of soil is usually richest in humus?",listOf("Topsoil","Deep rock","Bedrock","Clay only"),0,"Topsoil usually contains more humus and supports plant growth."),
        BankQuestion("SCI_C3_015","Science","Energy",4,"Which source gives us renewable energy?",listOf("Sun","Coal","Petrol","Diesel"),0,"Sunlight is a renewable source of energy."),
        BankQuestion("SCI_C3_016","Science","Materials",4,"Which material is attracted to a magnet?",listOf("Iron","Wood","Paper","Plastic"),0,"Iron is attracted to magnets.")
    )
}
