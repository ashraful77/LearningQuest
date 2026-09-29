package com.ashraful.learningquest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.ashraful.learningquest.data.GameDataStore

data class AnishStoreItem(val id:String,val emoji:String,val name:String,val description:String,val price:Int)

private val anishStoreItems = listOf(
    AnishStoreItem("anish_star","⭐","Star Badge","Your first collection badge",25),
    AnishStoreItem("anish_trophy","🏆","Champion Trophy","Reward for excellent learning",60),
    AnishStoreItem("anish_rocket","🚀","Rocket","Special fast-learning item",100),
    AnishStoreItem("anish_crown","👑","Golden Crown","Master learner reward",150),
    AnishStoreItem("anish_diamond","💎","Diamond Trophy","Special collectible item",250)
)

@Composable
fun AnishStoreScreen(onBack:()->Unit) {
    val context=LocalContext.current
    val store=remember { GameDataStore(context) }
    val scope=rememberCoroutineScope()
    val data by store.gameData.collectAsState(initial=null)
    val owned by store.anishOwnedItemIds().collectAsState(initial=emptySet())
    var message by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(Color(0xFFF7FAFF)).verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("🛍️ Diamond Store",fontSize=28.sp,fontWeight=FontWeight.ExtraBold,color=Color(0xFF315FBA))
                Text("Earn diamonds • Buy your favorite rewards",fontSize=13.sp,color=Color(0xFF60758A))
            }
            Surface(shape=RoundedCornerShape(16.dp),color=Color(0xFFE9F2FF)) {
                Text("💎 ${data?.diamonds ?: 0}",Modifier.padding(12.dp),fontWeight=FontWeight.ExtraBold,color=Color(0xFF315FBA))
            }
        }
        Spacer(Modifier.height(12.dp))
        if(message.isNotBlank()) Text(message,Modifier.fillMaxWidth(),textAlign=TextAlign.Center,fontWeight=FontWeight.Bold,color=Color(0xFF23754A))
        anishStoreItems.forEach { item ->
            val isOwned=item.id in owned
            Card(Modifier.fillMaxWidth().padding(vertical=5.dp),shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(Color.White),elevation=CardDefaults.cardElevation(1.dp)) {
                Row(Modifier.fillMaxWidth().padding(16.dp),verticalAlignment=Alignment.CenterVertically) {
                    Text(item.emoji,fontSize=38.sp)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.name,fontSize=18.sp,fontWeight=FontWeight.ExtraBold)
                        Text(item.description,fontSize=12.sp,color=Color(0xFF60758A))
                        Text("💎 ${item.price}",fontSize=14.sp,fontWeight=FontWeight.Bold,color=Color(0xFF315FBA))
                    }
                    Button(enabled=!isOwned && (data?.diamonds ?: 0)>=item.price,onClick={ scope.launch { val ok=store.buyAnishItem(item.id,item.price); message=if(ok) "🎉 ${item.name} purchased!" else "Earn more diamonds." } }) {
                        Text(if(isOwned) "Owned" else "Buy")
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick=onBack,Modifier.fillMaxWidth()){ Text("‹ Back") }
    }
}