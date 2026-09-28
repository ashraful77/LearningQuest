package com.ashraful.learningquest.data

data class Gift(
    val id: String,
    val name: String,
    val emoji: String,
    val category: GiftCategory,
    val price: Int,
    val description: String
)

enum class GiftCategory(val label: String) {
    CHARACTERS("🌟 Characters"),
    FUN("🚀 Fun"),
    THEMES("🎨 Themes"),
    SPECIAL("🏆 Special")
}

val giftCatalog = listOf(
    Gift("unicorn", "Unicorn", "🦄", GiftCategory.CHARACTERS, 50, "A magical learning friend"),
    Gift("kitty", "Kitty", "🐱", GiftCategory.CHARACTERS, 75, "A cute study buddy"),
    Gift("panda", "Panda", "🐼", GiftCategory.CHARACTERS, 90, "A calm learning companion"),
    Gift("bunny", "Bunny", "🐰", GiftCategory.CHARACTERS, 60, "A cheerful little friend"),
    Gift("fox", "Fox", "🦊", GiftCategory.CHARACTERS, 110, "A clever learning friend"),
    Gift("koala", "Koala", "🐨", GiftCategory.CHARACTERS, 125, "A friendly study buddy"),
    Gift("rocket", "Rocket", "🚀", GiftCategory.FUN, 100, "Blast into new ideas"),
    Gift("ufo", "UFO", "🛸", GiftCategory.FUN, 130, "Explore beyond the classroom"),
    Gift("balloon", "Balloon", "🎈", GiftCategory.FUN, 25, "A little celebration"),
    Gift("teddy", "Teddy", "🧸", GiftCategory.FUN, 70, "A cozy learning friend"),
    Gift("guitar", "Guitar", "🎸", GiftCategory.FUN, 85, "Make learning musical"),
    Gift("skateboard", "Skateboard", "🛹", GiftCategory.FUN, 95, "Keep your learning moving"),
    Gift("rainbow", "Rainbow", "🌈", GiftCategory.THEMES, 80, "Brighten your learning world"),
    Gift("space", "Space", "🌌", GiftCategory.THEMES, 150, "A cosmic learning world"),
    Gift("ocean", "Ocean", "🌊", GiftCategory.THEMES, 120, "Dive into discovery"),
    Gift("forest", "Forest", "🌳", GiftCategory.THEMES, 100, "Explore a green world"),
    Gift("sky", "Sky", "☁️", GiftCategory.THEMES, 90, "A peaceful learning sky"),
    Gift("garden", "Garden", "🌸", GiftCategory.THEMES, 105, "A colourful learning garden"),
    Gift("star", "Star", "⭐", GiftCategory.SPECIAL, 40, "A little sign of achievement"),
    Gift("crown", "Crown", "👑", GiftCategory.SPECIAL, 150, "A reward for a learning champion"),
    Gift("diamond", "Diamond", "💎", GiftCategory.SPECIAL, 200, "A precious learning treasure"),
    Gift("trophy", "Trophy", "🏆", GiftCategory.SPECIAL, 175, "Celebrate your progress"),
    Gift("fire", "Fire", "🔥", GiftCategory.SPECIAL, 125, "For a learning streak"),
    Gift("lightning", "Lightning", "⚡", GiftCategory.SPECIAL, 250, "The ultimate energy reward")
)
