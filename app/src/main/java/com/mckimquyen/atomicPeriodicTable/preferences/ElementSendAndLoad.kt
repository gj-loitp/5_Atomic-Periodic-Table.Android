package com.mckimquyen.atomicPeriodicTable.preferences

import android.content.Context
import android.content.SharedPreferences

class ElementSendAndLoad(context: Context) {

    val PREFERENCE_NAME = "ElementSendAndLoad"
    val PREFERENCE_VALUE = "ElementSendAndLoadValue"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue() : String?{
        return preference.getString(PREFERENCE_VALUE, "hydrogen")
    }

    fun setValue(string: String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_VALUE, string)
        editor.apply()
    }
}
