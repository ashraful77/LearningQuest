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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashraful.learningquest.data.GameDataStore

@Composable
fun HomeScreen() {

    var screen by remember {
        mutableStateOf("home")
    }

    when (screen) {

        "home" -> HomeContent {
            screen = it
        }

        "math" -> MathScreen {
            screen = "home"
        }

        "english" -> EnglishScreen {
            screen = "home"
        }

        "science" -> ScienceScreen {
            screen = "home"
        }

        "puzzle" -> PuzzleScreen {
            screen = "home"
        }
    }
}

@Composable
private fun HomeContent(
    onNavigate: (String) -> Unit
) {

    val context = LocalContext.current

    val dataStore = remember {
        GameDataStore(context)
    }

    val gameData by dataStore.gameData.collectAsState(
        initial = null
    )

    val data = gameData

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFEAF4FF),
                        Color(0xFFF8FBFF)
                    )
                )
            )
            .padding(18.dp)
    ) {

        Spacer(Modifier.height(8.dp))

        Text(
            text = "LEARNING QUEST",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF173C8C)
        )

        Text(
            text = "Learn • Play • Grow",
            fontSize = 16.sp,
            color = Color(0xFF55708F)
        )

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF315DA8)
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            "LEVEL",
                            fontSize = 13.sp,
                            color = Color(0xFFDDEAFF),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "${data?.level ?: 1}",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Column(
                        horizontalAlignment =
                            Alignment.End
                    ) {

                        Text(
                            "XP",
                            fontSize = 13.sp,
                            color = Color(0xFFDDEAFF),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "${data?.xp ?: 0}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(15.dp))

                LinearProgressIndicator(
                    progress = {
                        data?.xpProgress ?: 0f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp),
                    color = Color.White,
                    trackColor = Color(0xFF7091C8)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "${data?.xp ?: 0} XP earned",
                    fontSize = 13.sp,
                    color = Color(0xFFDDEAFF)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            StatCard(
                title = "COINS",
                value = "${data?.coins ?: 0}",
                modifier = Modifier.weight(1f),
                color = Color(0xFFFFF3D6),
                valueColor = Color(0xFF9A6500)
            )

            StatCard(
                title = "STREAK",
                value = "${data?.streak ?: 0} days",
                modifier = Modifier.weight(1f),
                color = Color(0xFFE5F7ED),
                valueColor = Color(0xFF177245)
            )
        }

        Spacer(Modifier.height(22.dp))

        Text(
            "CHOOSE YOUR CHALLENGE",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF55708F)
        )

        Spacer(Modifier.height(10.dp))

        SubjectCard(
            title = "Quick Math",
            subtitle = "Numbers and problem solving",
            color = Color(0xFFEAF3FF),
            titleColor = Color(0xFF2457A6)
        ) {
            onNavigate("math")
        }

        SubjectCard(
            title = "English",
            subtitle = "Words, grammar and language",
            color = Color(0xFFF3ECFF),
            titleColor = Color(0xFF7043A8)
        ) {
            onNavigate("english")
        }

        SubjectCard(
            title = "Science",
            subtitle = "Explore the world around you",
            color = Color(0xFFE8F8EF),
            titleColor = Color(0xFF23754A)
        ) {
            onNavigate("science")
        }

        SubjectCard(
            title = "Puzzles",
            subtitle = "Think, solve and discover",
            color = Color(0xFFFFF1DE),
            titleColor = Color(0xFF9A5A00)
        ) {
            onNavigate("puzzle")
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "Complete challenges to earn Coins and XP",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp,
            color = Color(0xFF71849A),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier,
    color: Color,
    valueColor: Color
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp)
        ) {

            Text(
                title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF65778B)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}

@Composable
private fun SubjectCard(
    title: String,
    subtitle: String,
    color: Color,
    titleColor: Color,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 15.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )

                Text(
                    subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF65778B)
                )
            }

            Text(
                ">",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
        }
    }
}