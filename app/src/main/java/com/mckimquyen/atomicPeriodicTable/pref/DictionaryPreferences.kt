package com.mckimquyen.atomicPeriodicTable.pref

import android.content.Context
import android.content.SharedPreferences

class DictionaryPreferences(context: Context) {

    private val PREFERENCE_NAME = "Dictionary_Preference"
    private val PREFERENCE_VALUE = "Dictionary_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue(): String {
        return preference.getString(PREFERENCE_VALUE, "chemistry") ?: ""

    }

    fun setValue(string: String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_VALUE, string)
        editor.apply()
    }
}
