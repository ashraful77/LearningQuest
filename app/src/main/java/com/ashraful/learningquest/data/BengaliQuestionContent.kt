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
    )

    fun apply(question: BankQuestion): BankQuestion {
        val localized = content[question.id] ?: return question
        return question.copy(
            bengaliPrompt = localized.first,
            bengaliOptions = localized.second
        )
    }
}
