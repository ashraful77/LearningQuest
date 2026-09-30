package com.ashraful.learningquest.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.abidRewardStore by preferencesDataStore(name = "abid_reward_store")

data class AbidRewardProgress(val chips: Int = 0, val owned: Set<Int> = emptySet(), val equipped: Set<Int> = emptySet())

data class AbidRewardItem(val id: Int, val name: String, val icon: String, val price: Int, val kind: String)

val abidRewardCatalog = listOf(
    AbidRewardItem(1, "Gift Box", "🎁", 5, "gift"),
    AbidRewardItem(2, "Toy Car", "🚗", 8, "toy"),
    AbidRewardItem(3, "Rocket", "🚀", 10, "toy"),
    AbidRewardItem(4, "Teddy Bear", "🧸", 12, "toy"),
    AbidRewardItem(5, "Ball", "⚽", 6, "toy"),
    AbidRewardItem(6, "Balloons", "🎈", 7, "gift"),
    AbidRewardItem(7, "Magic Wand", "🪄", 15, "magic"),
    AbidRewardItem(8, "Trophy", "🏆", 20, "award")
)

class AbidRewardStore(private val context: Context) {
    private object Keys {
        val CHIPS = intPreferencesKey("blue_chips")
        fun owned(id: Int) = intPreferencesKey("owned_$id")
        fun equipped(id: Int) = intPreferencesKey("equipped_$id")
    }

    val progress: Flow<AbidRewardProgress> = context.abidRewardStore.data.map { p ->
        val owned = abidRewardCatalog.filter { (p[Keys.owned(it.id)] ?: 0) == 1 }.map { it.id }.toSet()
        val equipped = abidRewardCatalog.filter { (p[Keys.equipped(it.id)] ?: 0) == 1 }.map { it.id }.toSet()
        AbidRewardProgress(p[Keys.CHIPS] ?: 0, owned, equipped)
    }

    suspend fun addChip() {
        context.abidRewardStore.edit { p -> p[Keys.CHIPS] = (p[Keys.CHIPS] ?: 0) + 1 }
    }

    suspend fun addChips(amount: Int) {
        if (amount <= 0) return
        context.abidRewardStore.edit { p -> p[Keys.CHIPS] = (p[Keys.CHIPS] ?: 0) + amount }
    }

    suspend fun buy(item: AbidRewardItem): Boolean {
        var bought = false
        context.abidRewardStore.edit { p ->
            val chips = p[Keys.CHIPS] ?: 0
            if ((p[Keys.owned(item.id)] ?: 0) == 0 && chips >= item.price) {
                p[Keys.CHIPS] = chips - item.price
                p[Keys.owned(item.id)] = 1
                bought = true
            }
        }
        return bought
    }

    suspend fun toggleEquipped(item: AbidRewardItem) {
        context.abidRewardStore.edit { p ->
            if ((p[Keys.owned(item.id)] ?: 0) == 1) {
                p[Keys.equipped(item.id)] = if ((p[Keys.equipped(item.id)] ?: 0) == 1) 0 else 1
            }
        }
    }
}
