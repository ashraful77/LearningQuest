package com.ashraful.learningquest.data

/**
 * Hand-written Bengali versions for curated questions.
 *
 * The English question remains the source question. Bengali content is
 * stored locally so switching language is instant and works offline.
 */
object BengaliQuestionContent {

    private val content = mapOf(
        "MATH_WORD_001" to Pair(
            "আরিফার কাছে ২৪টি স্টিকার ছিল এবং সে আরও ১৮টি পেল। এখন তার কাছে মোট কতটি স্টিকার আছে?",
            listOf("৩২", "৪০", "৪২", "৪৪")
        ),
        "MATH_WORD_002" to Pair(
            "একটি তাকের উপর ৫০টি বই আছে। ১৭টি বই সরিয়ে নেওয়া হলো। কতটি বই বাকি রইল?",
            listOf("২৩", "৩৩", "৩৭", "৪৩")
        ),
        "MATH_WORD_003" to Pair(
            "৬টি বাক্সে প্রতিটিতে ৪টি করে পেন্সিল আছে। মোট কতটি পেন্সিল আছে?",
            listOf("১০", "২০", "২৪", "২৮")
        ),
        "MATH_WORD_004" to Pair(
            "২৪টি টফি ৬টি শিশুর মধ্যে সমানভাবে ভাগ করা হলো। প্রত্যেক শিশু কতটি টফি পাবে?",
            listOf("৩", "৪", "৫", "৬")
        ),
        "MATH_GEOM_001" to Pair(
            "একটি ত্রিভুজের কয়টি বাহু আছে?",
            listOf("২", "৩", "৪", "৫")
        ),
        "MATH_GEOM_002" to Pair(
            "একটি আয়তের কয়টি বাহু আছে?",
            listOf("৩", "৪", "৫", "৬")
        ),
        "MATH_MEASURE_001" to Pair(
            "একটি পেন্সিলের দৈর্ঘ্য মাপতে সাধারণত কোন একক ব্যবহার করা হয়?",
            listOf("কিলোমিটার", "মিটার", "সেন্টিমিটার", "লিটার")
        ),
        "MATH_MONEY_001" to Pair(
            "একটি পেন্সিলের দাম ₹৮। ৩টি পেন্সিলের দাম কত?",
            listOf("₹১৬", "₹২০", "₹২৪", "₹৩২")
        ),
        "MATH_TIME_001" to Pair(
            "এক ঘণ্টায় কত মিনিট থাকে?",
            listOf("৩০", "৪৫", "৬০", "১০০")
        ),
        "ENG_VOCAB_001" to Pair(
            "'Happy' শব্দের বিপরীত অর্থ কোনটি?",
            listOf("উজ্জ্বল", "দুঃখী", "দ্রুত", "দয়ালু")
        ),
        "ENG_GRAMMAR_001" to Pair(
            "সঠিক বাক্যটি বেছে নাও।",
            listOf("She are happy.", "She am happy.", "She is happy.", "She be happy.")
        ),
        "SCI_PLANT_001" to Pair(
            "গাছের কোন অংশ সাধারণত মাটি থেকে জল শোষণ করে?",
            listOf("ফুল", "শিকড়", "ফল", "পাতা")
        ),
        "SCI_BODY_001" to Pair(
            "কোন অঙ্গ সারা শরীরে রক্ত পাম্প করে?",
            listOf("মস্তিষ্ক", "ফুসফুস", "হৃদপিণ্ড", "পাকস্থলী")
        ),
        "MATH_FRAC_001" to Pair(
            "কোন ভগ্নাংশটি এক-দ্বিতীয়াংশের সমান?",
            listOf("১/৩", "২/৪", "২/৩", "৩/৪")
        ),
        "MATH_TIME_002" to Pair(
            "একটি ক্লাস সকাল ৯টায় শুরু হয় এবং ১ ঘণ্টা চলে। ক্লাসটি কখন শেষ হবে?",
            listOf("৯:৩০", "১০:০০", "১০:৩০", "১১:০০")
        ),
        "ENG_GRAMMAR_006" to Pair(
            "সঠিক article বেছে নাও: I saw ___ elephant.",
            listOf("a", "an", "thee", "no")
        ),
        "ENG_GRAMMAR_007" to Pair(
            "'go'-এর past tense কোনটি?",
            listOf("goed", "going", "went", "goes")
        ),
        "SCI_SPACE_001" to Pair(
            "কোন গ্রহকে লাল গ্রহ বলা হয়?",
            listOf("পৃথিবী", "মঙ্গল", "শুক্র", "বৃহস্পতি")
        ),
        "SCI_SPACE_002" to Pair(
            "পৃথিবীর প্রাকৃতিক উপগ্রহ কোনটি?",
            listOf("সূর্য", "মঙ্গল", "চাঁদ", "শুক্র")
        ),
        "SCI_MATTER_001" to Pair(
            "পদার্থের কোন অবস্থার নির্দিষ্ট আকার থাকে?",
            listOf("কঠিন", "তরল", "গ্যাস", "শুধু বাষ্প")
        )
        "MATH_WORD_009" to Pair("একটি লাইব্রেরিতে ২৮টি নতুন বই এল এবং পরে আরও ১৬টি এল। মোট কতটি নতুন বই এল?", listOf("৩৪", "৪৪", "৫৪", "৬৪")),
        "MATH_WORD_010" to Pair("একটি পার্কে ৭৫টি পাখি ছিল। ২৮টি উড়ে গেল। কতটি পাখি বাকি রইল?", listOf("৩৭", "৪৭", "৫৭", "৬৭")),
        "MATH_WORD_011" to Pair("৮টি ব্যাগে প্রতিটিতে ৬টি করে মার্বেল আছে। মোট কতটি মার্বেল আছে?", listOf("৪২", "৪৮", "৫৪", "৫৬")),
        "MATH_WORD_012" to Pair("৪৫টি বিস্কুট ৫টি শিশুর মধ্যে সমানভাবে ভাগ করা হলো। প্রত্যেকে কতটি পাবে?", listOf("৭", "৮", "৯", "১০")),
        "MATH_WORD_013" to Pair("একটি শ্রেণিতে ৩২ জন ছাত্রছাত্রী আছে। ৭ জন অনুপস্থিত এবং পরে ৫ জন নতুন ছাত্রছাত্রী এল। তখন মোট কতজন উপস্থিত?", listOf("২৫", "৩০", "৩৫", "৪৪")),
        "MATH_WORD_014" to Pair("একজন কৃষক সকালে ৪৬টি এবং বিকেলে ৩৯টি ডিম সংগ্রহ করলেন। মোট কতটি ডিম সংগ্রহ করলেন?", listOf("৭৫", "৮৫", "৯৫", "১০৫")),
        "MATH_WORD_015" to Pair("একটি দোকানে ১০০টি খাতা ছিল এবং ৩৬টি বিক্রি হয়ে গেল। কতটি খাতা বাকি রইল?", listOf("৫৪", "৬৪", "৭৪", "৮৪")),
        "MATH_WORD_016" to Pair("একজন শিক্ষক ৯ জন ছাত্রছাত্রীকে প্রত্যেককে ৪টি করে পেন্সিল দিলেন। মোট কতটি পেন্সিল লাগবে?", listOf("১৩", "২৭", "৩৬", "৪৫")),
        "MATH_GEOM_007" to Pair("একটি পঞ্চভুজের কয়টি বাহু আছে?", listOf("৪", "৫", "৬", "৭")),
        "MATH_GEOM_008" to Pair("একটি ষড়ভুজের কয়টি বাহু আছে?", listOf("৫", "৬", "৭", "৮")),
        "MATH_GEOM_009" to Pair("কোন আকৃতির চারটি সমান বাহু এবং চারটি সমকোণ আছে?", listOf("ত্রিভুজ", "বৃত্ত", "বর্গ", "ডিম্বাকৃতি")),
        "MATH_GEOM_010" to Pair("একটি আয়তের কয়টি সমকোণ আছে?", listOf("১", "২", "৩", "৪")),
        "MATH_MEASURE_005" to Pair("দুটি শহরের মধ্যের দূরত্ব মাপতে কোন এককটি সবচেয়ে উপযুক্ত?", listOf("মিলিমিটার", "সেন্টিমিটার", "মিটার", "কিলোমিটার")),
        "MATH_MEASURE_006" to Pair("কোনটি বেশি ভারী?", listOf("১ কেজি", "৫০০ গ্রাম", "২৫০ গ্রাম", "১০০ গ্রাম")),
        "MATH_MEASURE_007" to Pair("১ কিলোগ্রামে কত গ্রাম থাকে?", listOf("১০", "১০০", "১০০০", "১০,০০০")),
        "MATH_MONEY_005" to Pair("একটি বইয়ের দাম ₹৪৫ এবং একটি কলমের দাম ₹১৫। মোট দাম কত?", listOf("₹৫০", "₹৫৫", "₹৬০", "₹৬৫")),
        "MATH_MONEY_006" to Pair("তোমার কাছে ₹১০০ আছে এবং তুমি ₹৬৮ দিয়ে একটি খেলনা কিনলে। কত টাকা ফেরত পাবে?", listOf("₹২২", "₹৩২", "₹৩৮", "₹৪২")),
        "MATH_MONEY_007" to Pair("চারটি পেন্সিলের প্রতিটির দাম ₹৭। মোট দাম কত?", listOf("₹২১", "₹২৪", "₹২৮", "₹৩৫")),
        "MATH_TIME_005" to Pair("আধ ঘণ্টায় কত মিনিট থাকে?", listOf("১৫", "২০", "৩০", "৪৫")),
        "MATH_TIME_006" to Pair("দুপুর ২টা থেকে ৪৫ মিনিট পরে কয়টা বাজবে?", listOf("২:১৫", "২:৩০", "২:৪৫", "৩:৪৫")),
        "MATH_TIME_007" to Pair("একটি সিনেমা ৫:৩০-এ শুরু হয় এবং ১ ঘণ্টা চলে। কখন শেষ হবে?", listOf("৬:০০", "৬:৩০", "৭:০০", "৭:৩০")),
        "MATH_FRAC_005" to Pair("কোন ভগ্নাংশটি একটি সম্পূর্ণকে বোঝায়?", listOf("১/২", "২/৩", "৩/৩", "১/৪")),
        "MATH_FRAC_006" to Pair("কোনটি সবচেয়ে ছোট?", listOf("৩/৪", "১/৪", "২/৪", "৪/৪")),
        "MATH_FRAC_007" to Pair("১/২ + ১/২ কত?", listOf("১/২", "১", "২", "৩/২")),
        "MATH_LOGIC_005" to Pair("পরের সংখ্যাটি কী হবে: ৩, ৬, ৯, ১২, ?", listOf("১৪", "১৫", "১৬", "১৮")),
        "MATH_LOGIC_006" to Pair("পরের সংখ্যাটি কী হবে: ২০, ১৮, ১৬, ১৪, ?", listOf("১০", "১১", "১২", "১৩")),
        "MATH_LOGIC_007" to Pair("একটি বাক্সে ৬টি বল রাখা যায়। ৫টি বাক্সে মোট কতটি বল রাখা যাবে?", listOf("১১", "২৪", "৩০", "৩৬")),
        "MATH_COMPARE_003" to Pair("কোন সংখ্যাটি ১০০-এর সবচেয়ে কাছাকাছি?", listOf("৮৯", "৭২", "৬৫", "৫৪")),
        "MATH_COMPARE_004" to Pair("কোন সংখ্যাটি ৪০ ও ৫০-এর মধ্যে?", listOf("৩৫", "৪২", "৫২", "৬০")),
        "MATH_COMPARE_005" to Pair("কোনটি বড়?", listOf("৩ দশক", "২৫ একক", "২ দশক", "১৯ একক")),
        "MATH_PATTERN_001" to Pair("ধারাটি সম্পূর্ণ করো: ১০, ২০, ৩০, ৪০, ?", listOf("৪৫", "৫০", "৫৫", "৬০")),
        "MATH_PATTERN_002" to Pair("ধারাটি সম্পূর্ণ করো: ২, ৫, ৮, ১১, ?", listOf("১২", "১৩", "১৪", "১৫")),
        "MATH_PLACE_001" to Pair("৫৭২ সংখ্যায় ৭-এর স্থানীয় মান কত?", listOf("৭", "৭০", "৭০০", "৫০০")),
        "MATH_PLACE_002" to Pair("কোন সংখ্যায় ৬ শতক, ৩ দশক এবং ৪ একক আছে?", listOf("৩৪৬", "৬৩৪", "৬৪৩", "৩৬৪")),

        "ENG_GRAMMAR_019" to Pair("সঠিক শব্দটি বেছে নাও: I ___ happy.", listOf("am", "is", "are", "be")),
        "ENG_GRAMMAR_020" to Pair("সঠিক শব্দটি বেছে নাও: The boys ___ running.", listOf("is", "am", "are", "was")),
        "ENG_GRAMMAR_021" to Pair("'mouse' শব্দটির সঠিক বহুবচন কোনটি?", listOf("Mouses", "Mice", "Mousees", "Mices")),
        "ENG_GRAMMAR_022" to Pair("'tooth' শব্দটির সঠিক বহুবচন কোনটি?", listOf("Tooths", "Teeth", "Toothes", "Teeths")),
        "ENG_GRAMMAR_023" to Pair("কোন শব্দটি pronoun?", listOf("She", "Garden", "Run", "Green")),
        "ENG_GRAMMAR_024" to Pair("কোন শব্দটি adjective?", listOf("Beautiful", "Run", "Quickly", "Teacher")),
        "ENG_GRAMMAR_025" to Pair("কোন শব্দটি verb?", listOf("Jump", "Blue", "Happy", "School")),
        "ENG_GRAMMAR_026" to Pair("কোন বাক্যটি সঠিক?", listOf("He have a pen.", "He has a pen.", "He having a pen.", "He are a pen.")),
        "ENG_GRAMMAR_027" to Pair("সঠিক article বেছে নাও: She ate ___ apple.", listOf("a", "an", "thee", "no")),
        "ENG_GRAMMAR_028" to Pair("সঠিক article বেছে নাও: He has ___ blue bag.", listOf("a", "an", "are", "am")),
        "ENG_GRAMMAR_029" to Pair("কোন punctuation mark উত্তেজনা বা প্রবল অনুভূতি বোঝায়?", listOf(".", "?", "! ", ",")),
        "ENG_GRAMMAR_030" to Pair("কোন শব্দটি preposition?", listOf("Under", "Happy", "Jump", "Quickly")),
        "ENG_GRAMMAR_031" to Pair("'eat'-এর past tense কোনটি?", listOf("Eated", "Ate", "Eating", "Eats")),
        "ENG_GRAMMAR_032" to Pair("'see'-এর past tense কোনটি?", listOf("Seed", "Saw", "Seeing", "Sees")),
        "ENG_VOCAB_013" to Pair("'tall'-এর বিপরীত অর্থ কোনটি?", listOf("লম্বা", "খাটো", "দীর্ঘ", "বড়")),
        "ENG_VOCAB_014" to Pair("'angry'-এর সমার্থক শব্দ কোনটি?", listOf("Furious", "Quiet", "Sleepy", "Tiny")),
        "ENG_VOCAB_015" to Pair("'empty'-এর বিপরীত অর্থ কোনটি?", listOf("পূর্ণ", "ছোট", "খোলা", "হালকা")),
        "ENG_VOCAB_016" to Pair("'clever'-এর সমার্থক শব্দ কোনটি?", listOf("Smart", "Slow", "Weak", "Noisy")),
        "ENG_VOCAB_017" to Pair("'silent'-এর সমার্থক শব্দ কোনটি?", listOf("Noisy", "Quiet", "Fast", "Bright")),
        "ENG_VOCAB_018" to Pair("'large'-এর সমার্থক শব্দ কোনটি?", listOf("Huge", "Tiny", "Narrow", "Short")),
        "ENG_VOCAB_019" to Pair("কোন শব্দটি 'sun'-এর সঙ্গে rhyme করে?", listOf("Run", "Tree", "Book", "Cat")),
        "ENG_VOCAB_020" to Pair("কোন শব্দটি 'cake'-এর সঙ্গে rhyme করে?", listOf("Bike", "Lake", "Book", "Rain")),
        "ENG_READING_005" to Pair("টম ছাতা নিল কারণ আকাশে কালো মেঘ ছিল। সে কী আশা করেছিল?", listOf("বৃষ্টি", "গ্রীষ্মে তুষার", "রোদ", "জন্মদিন")),
        "ENG_READING_006" to Pair("লিনা বাড়ি থেকে বেরোনোর আগে স্কুলের ব্যাগে একটি বই রাখল। সে সম্ভবত কোথায় যাচ্ছিল?", listOf("স্কুলে", "সাঁতার কাটতে", "ঘুমাতে", "রান্না করতে")),
        "ENG_READING_007" to Pair("একটি গাছ শুকিয়ে গিয়েছিল, তাই রবি তাতে জল দিল। সম্ভবত কী ফল হলো?", listOf("গাছটি জল পেল", "গাছটি বই হয়ে গেল", "গাছটি পাথর হয়ে গেল", "গাছটি অদৃশ্য হয়ে গেল")),
        "ENG_READING_008" to Pair("মিনার কাছে ১২টি ক্রেয়ন ছিল এবং সে ৪টি বন্ধুকে দিল। কতটি ক্রেয়ন বাকি রইল?", listOf("৬", "৭", "৮", "৯")),
        "ENG_WORDS_001" to Pair("সঠিক শব্দক্রমের বাক্যটি বেছে নাও।", listOf("Park the children play.", "The children play in the park.", "Children the park in play.", "Play the in children park.")),
        "ENG_WORDS_002" to Pair("সঠিক বাক্যটি বেছে নাও।", listOf("Is blue the sky.", "The sky is blue.", "Blue sky the is.", "Sky the blue is.")),
        "ENG_SPELLING_001" to Pair("সঠিক বানানটি কোনটি?", listOf("Becaus", "Because", "Becouse", "Beacause")),
        "ENG_SPELLING_002" to Pair("সঠিক বানানটি কোনটি?", listOf("Beautiful", "Beutiful", "Beautifull", "Butiful")),
        "ENG_LANGUAGE_001" to Pair("যে শব্দগুলির সমষ্টি সম্পূর্ণ অর্থ প্রকাশ করে, তাকে কী বলে?", listOf("Sentence", "Letter", "Number", "Shape")),
        "ENG_LANGUAGE_002" to Pair("'before'-এর বিপরীত অর্থ কোনটি?", listOf("After", "Early", "First", "Soon")),
        "ENG_LANGUAGE_003" to Pair("কোন শব্দটি কেউ কীভাবে দৌড়ায় তা বোঝাতে পারে?", listOf("Quickly", "Blue", "Chair", "Happy")),

        "SCI_PLANT_007" to Pair("গাছ খাদ্য তৈরি করতে সূর্যের কাছ থেকে কী পায়?", listOf("সূর্যের আলো", "প্লাস্টিক", "বালি", "ধাতু")),
        "SCI_PLANT_008" to Pair("গাছকে সোজা রাখে এবং পাতাগুলিকে ধরে রাখে কোন অংশ?", listOf("কাণ্ড", "শিকড়", "বীজ", "ফল")),
        "SCI_PLANT_009" to Pair("বীজের অঙ্কুরোদ্গম শুরু হওয়ার জন্য কী প্রয়োজন?", listOf("উপযুক্ত জল ও পরিবেশ", "শুধু পাথর", "শুধু অন্ধকার", "প্লাস্টিক")),
        "SCI_ANIMAL_004" to Pair("কোন প্রাণী মাংসাশী?", listOf("গরু", "হরিণ", "সিংহ", "ছাগল")),
        "SCI_ANIMAL_005" to Pair("কোন প্রাণী জল ও স্থল—দুই জায়গাতেই থাকতে পারে?", listOf("ব্যাঙ", "গরু", "ঈগল", "উট")),
        "SCI_ANIMAL_006" to Pair("কোন প্রাণীর পালক আছে?", listOf("কুকুর", "মুরগি", "বিড়াল", "গরু")),
        "SCI_BODY_007" to Pair("শরীরের কোন অঙ্গ আমাদের দেখতে সাহায্য করে?", listOf("চোখ", "কান", "নাক", "পা")),
        "SCI_BODY_008" to Pair("শরীরের কোন অঙ্গ আমাদের গন্ধ পেতে সাহায্য করে?", listOf("চোখ", "কান", "নাক", "হাত")),
        "SCI_BODY_009" to Pair("কোন অঙ্গ রক্ত পাম্প করে?", listOf("হৃদপিণ্ড", "ফুসফুস", "মস্তিষ্ক", "পাকস্থলী")),
        "SCI_BODY_010" to Pair("শরীরের কোন অংশ খাবারের স্বাদ বুঝতে সাহায্য করে?", listOf("জিহ্বা", "কান", "চোখ", "কনুই")),
        "SCI_SPACE_005" to Pair("দিনের বেলা পৃথিবীকে আলো দেয় কোনটি?", listOf("সূর্য", "চাঁদ", "মঙ্গল", "মেঘ")),
        "SCI_SPACE_006" to Pair("কোন গ্রহটি তার বলয়ের জন্য বিখ্যাত?", listOf("বুধ", "শনি", "পৃথিবী", "মঙ্গল")),
        "SCI_SPACE_007" to Pair("চাঁদ কি নিজের আলো তৈরি করে?", listOf("হ্যাঁ", "না", "শুধু দুপুরে", "শুধু শীতকালে")),
        "SCI_EARTH_004" to Pair("পৃথিবীকে ঘিরে থাকা বায়ুর স্তরকে কী বলে?", listOf("বায়ুমণ্ডল", "সমুদ্র", "মাটি", "পাথর")),
        "SCI_EARTH_005" to Pair("মেঘ থেকে জল পড়াকে কী বলে?", listOf("বৃষ্টি", "ধোঁয়া", "ধুলো", "বাতাস")),
        "SCI_EARTH_006" to Pair("কোনটি একটি প্রাকৃতিক সম্পদ?", listOf("জল", "প্লাস্টিকের বোতল", "খেলনা", "কম্পিউটার")),
        "SCI_MATTER_005" to Pair("পদার্থের কোন অবস্থার নির্দিষ্ট আকার নেই এবং এটি পাত্রটি পূর্ণ করে?", listOf("কঠিন", "গ্যাস", "পাথর", "বরফ")),
        "SCI_MATTER_006" to Pair("জল ফুটলে কী ঘটে?", listOf("জল জলীয় বাষ্পে পরিণত হতে পারে", "জল পাথর হয়ে যায়", "জল বরফ হয়ে যায়", "পদার্থ হিসেবে জল অদৃশ্য হয়ে যায়")),
        "SCI_MATTER_007" to Pair("কোনটি কঠিন পদার্থ?", listOf("দুধ", "বায়ু", "কাঠ", "বাষ্প")),
        "SCI_ENV_004" to Pair("পরিবেশ পরিষ্কার রাখতে কোন কাজটি সাহায্য করতে পারে?", listOf("গাছ লাগানো", "রাস্তায় আবর্জনা ফেলা", "জলের অপচয় করা", "সব জায়গায় প্লাস্টিক পোড়ানো")),
        "SCI_ENV_005" to Pair("আবর্জনা ডাস্টবিনে ফেলা উচিত কেন?", listOf("জায়গা পরিষ্কার রাখতে", "আরও আবর্জনা তৈরি করতে", "সম্পদের অপচয় করতে", "নালা বন্ধ করতে")),
        "SCI_ENV_006" to Pair("কোন জিনিসটি ফেলে না দিয়ে আবার ব্যবহার করা যায়?", listOf("পুনর্ব্যবহারযোগ্য বোতল", "ধোঁয়া", "আকাশের বৃষ্টির জল", "সূর্যের আলো")),
        "SCI_ENERGY_003" to Pair("গাছ খাদ্য তৈরি করার জন্য কোন শক্তি ব্যবহার করে?", listOf("সূর্যের আলো", "প্লাস্টিক", "কাচ", "বালি")),
        "SCI_ENERGY_004" to Pair("কোন যন্ত্র বৈদ্যুতিক শক্তিকে আলোক শক্তিতে পরিবর্তন করে?", listOf("বাল্ব", "চেয়ার", "চামচ", "বই")),
        "SCI_ENERGY_005" to Pair("কোন শক্তির উৎসটি পুনর্নবীকরণযোগ্য?", listOf("সূর্যের আলো", "কয়লা", "পেট্রোল", "ডিজেল")),
        "SCI_WEATHER_003" to Pair("ভারতের অনেক অঞ্চলে সাধারণত কোন ঋতু সবচেয়ে গরম?", listOf("গ্রীষ্ম", "শীত", "শরৎ", "বসন্ত")),
        "SCI_WEATHER_004" to Pair("বাতাসের দিক জানার জন্য কোন যন্ত্র ব্যবহার করা হয়?", listOf("উইন্ড ভেন", "থার্মোমিটার", "স্কেল", "ঘড়ি")),
        "SCI_WEATHER_005" to Pair("বৃষ্টিপাত মাপার জন্য কোন যন্ত্র ব্যবহার করা হয়?", listOf("রেন গেজ", "থার্মোমিটার", "কম্পাস", "স্কেল")),
        "SCI_MATERIALS_002" to Pair("কোন উপাদানটি সাধারণত জলরোধী?", listOf("প্লাস্টিক", "কাগজের তোয়ালে", "সুতির কাপড়", "কার্ডবোর্ড")),
        "SCI_MATERIALS_003" to Pair("কোন উপাদানটি স্বচ্ছ?", listOf("স্বচ্ছ কাচ", "কাঠ", "ইট", "ধাতুর পাত")),
        "SCI_MATERIALS_004" to Pair("কোন উপাদানটি চুম্বকের দ্বারা আকৃষ্ট হয়?", listOf("লোহা", "কাঠ", "প্লাস্টিক", "কাচ")),
        "SCI_FORCE_001" to Pair("কোনো বস্তুকে ঠেলা বা টানলে কী হতে পারে?", listOf("তার গতি পরিবর্তিত হতে পারে", "তা জলে পরিণত হতে পারে", "তা অদৃশ্য হয়ে যেতে পারে", "তা গাছে পরিণত হতে পারে")),
        "SCI_FORCE_002" to Pair("কোনটি ঠেলার উদাহরণ?", listOf("ড্রয়ার ঠেলে খোলা", "দড়ি টানা", "ব্যাগ তুলে নেওয়া", "খেলনাকে নিজের দিকে টেনে আনা"))
    )

    fun apply(question: BankQuestion): BankQuestion {
        val localized = content[question.id] ?: return question
        return question.copy(
            bengaliPrompt = localized.first,
            bengaliOptions = localized.second
        )
    }
}
