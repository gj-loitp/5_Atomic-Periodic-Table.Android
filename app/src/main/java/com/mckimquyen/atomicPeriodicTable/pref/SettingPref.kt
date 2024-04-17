package com.mckimquyen.atomicPeriodicTable.pref

import android.content.Context
import android.content.SharedPreferences

class OfflinePreference(context: Context) {
    private val prefName = "Offline_Preference"
    private val prefValue = "Offline_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun getValue(): Int {
        return preference.getInt(prefValue, 0)
    }

    fun setValue(count: Int) {
        val editor = preference.edit()
        editor.putInt(prefValue, count)
        editor.apply()
    }
}
