package com.mckimquyen.atomicPeriodicTable.pref

import android.content.Context
import android.content.SharedPreferences

class IsoPref(context: Context) {

    private val prefName = "Iso_Preference"
    private val prefValue = "Iso_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun getValue(): Int {
        return preference.getInt(prefValue, 0)
        //0 == Alphabetical
        //1 == Element Number
    }

    fun setValue(count: Int) {
        val editor = preference.edit()
        editor.putInt(prefValue, count)
        editor.apply()
    }
}

class SendIso(context: Context) {

    private val prefName = "send_Iso_pref"
    private val prefValue = "send_iso_value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun getValue(): String {
        return preference.getString(prefValue, "false")!!
        //0 == Not sent
        //1 == Sent
    }

    fun setValue(count: String) {
        val editor = preference.edit()
        editor.putString(prefValue, count)
        editor.apply()
    }
}
