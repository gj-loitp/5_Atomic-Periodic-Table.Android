package com.mckimquyen.atomicPeriodicTable.pref

import android.content.Context
import android.content.SharedPreferences

class OfflinePreference(context: Context) {
    val PREFERENCE_NAME = "Offline_Preference"
    val PREFERENCE_VALUE = "Offline_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue(): Int {
        return preference.getInt(PREFERENCE_VALUE, 0)
    }

    fun setValue(count: Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_VALUE, count)
        editor.apply()
    }
}
