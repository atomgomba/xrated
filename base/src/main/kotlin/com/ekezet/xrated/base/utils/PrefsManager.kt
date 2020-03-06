package com.ekezet.xrated.base.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.ekezet.xrated.base.di.APP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author kiri
 */
@Singleton
class PrefsManager @Inject constructor(@Named(APP) context: Context) : CoroutineScope {
    override val coroutineContext = Dispatchers.Main

    private val prefs = context.getSharedPreferences("prefs", MODE_PRIVATE)

    val changesChannel = ConflatedBroadcastChannel<String>()

    var baseCurrency: String
        get() = prefs.getString(PREF_BASE_CURRENCY, "EUR")!!
        set(value) {
            prefs.edit { putString(PREF_BASE_CURRENCY, value) }
            publishChange(PREF_BASE_CURRENCY)
        }

    var baseAmount: Float
        get() = prefs.getFloat(PREF_BASE_AMOUNT, 1F)
        set(value) {
            prefs.edit { putFloat(PREF_BASE_AMOUNT, value) }
            publishChange(PREF_BASE_AMOUNT)
        }

    var favorites: Set<String>
        get() = prefs.getStringSet(PREF_FAVORITES, emptySet())!!
        set(value) {
            prefs.edit { putStringSet(PREF_FAVORITES, value) }
            publishChange(PREF_FAVORITES)
        }

    private fun publishChange(key: String) {
        launch {
            changesChannel.send(key)
        }
    }

    companion object {
        const val PREF_BASE_CURRENCY = "baseCurrency"
        const val PREF_BASE_AMOUNT = "baseAmount"
        const val PREF_FAVORITES = "favorites"
    }
}