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

private data class AnishGiftTier(val title: String, val emoji: String, val items: List<AnishStoreItem>)

private val anishGiftTiers = listOf(
    AnishGiftTier("NORMAL GIFTS", "🎁", listOf(
        AnishStoreItem("anish_star","⭐","Star Badge","A bright starter gift",10),
        AnishStoreItem("anish_medal","🏅","Learning Medal","A reward for regular practice",20),
        AnishStoreItem("anish_balloon","🎈","Balloon","A fun collection gift",30),
        AnishStoreItem("anish_art","🎨","Art Badge","A colourful collection gift",40)
    )),
    AnishGiftTier("PREMIUM GIFTS", "💎", listOf(
        AnishStoreItem("anish_trophy","🏆","Golden Trophy","For excellent learning",60),
        AnishStoreItem("anish_rocket","🚀","Rocket","A special premium gift",100),
        AnishStoreItem("anish_gold_medal","🥇","Gold Medal","A premium achievement gift",150)
    )),
    AnishGiftTier("ULTRA PREMIUM GIFTS", "👑", listOf(
        AnishStoreItem("anish_crown","👑","Golden Crown","A rare master learner gift",200),
        AnishStoreItem("anish_diamond","💎","Diamond Trophy","A very special collectible",300),
        AnishStoreItem("anish_grand_trophy","🏆","Grand Champion Trophy","The ultimate collection gift",500)
    ))
,    AnishGiftTier("LEGENDARY GIFTS", "🌟", listOf(
        AnishStoreItem("anish_legend_star","🌟","Legendary Star","For a truly dedicated learner",1000),
        AnishStoreItem("anish_royal_trophy","🏆","Royal Champion Trophy","A major long-term achievement gift",2500),
        AnishStoreItem("anish_diamond_crown","💎","Diamond Crown","A rare high-value collection gift",5000),
        AnishStoreItem("anish_ultimate_crown","👑","Ultimate Learning Crown","The ultimate long-term gift",10000)
    ))
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
                Text("💎 Spend diamonds only • Collect special gifts",fontSize=13.sp,color=Color(0xFF60758A))
            }
            Surface(shape=RoundedCornerShape(16.dp),color=Color(0xFFE9F2FF)) {
                Text("💎 ${data?.diamonds ?: 0}",Modifier.padding(12.dp),fontWeight=FontWeight.ExtraBold,color=Color(0xFF315FBA))
            }
        }
        Spacer(Modifier.height(12.dp))
        if(message.isNotBlank()) Text(message,Modifier.fillMaxWidth(),textAlign=TextAlign.Center,fontWeight=FontWeight.Bold,color=Color(0xFF23754A))
        anishGiftTiers.forEach { tier ->
            Text("${tier.emoji}  ${tier.title}",Modifier.fillMaxWidth().padding(top=10.dp,bottom=4.dp),fontSize=20.sp,fontWeight=FontWeight.ExtraBold,color=Color(0xFF315FBA))
            tier.items.forEach { item ->
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
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick=onBack,Modifier.fillMaxWidth()){ Text("‹ Back") }
    }
}