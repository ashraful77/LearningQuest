package com.ashraful.learningquest.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameTopBar(
    title: String,
    coins: Int,
    streak: Int,
    onBack: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (onBack != null) {
            TextButton(onClick = onBack) { Text("←") }
        } else {
            Spacer(Modifier.width(48.dp))
        }
        Text(title, style = MaterialTheme.typography.titleLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🪙 $coins")
            Spacer(Modifier.width(8.dp))
            Text("🔥 $streak")
        }
    }
}