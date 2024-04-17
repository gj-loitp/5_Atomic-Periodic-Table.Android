package com.mckimquyen.atomicPeriodicTable.pref

import android.content.Context
import android.content.SharedPreferences

class FavoriteBarPref(context: Context) {

    private val PREFERENCE_NAME = "Molar_Preference"
    private val PREFERENCE_VALUE = "Molar_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue(): Int {
        return preference.getInt(PREFERENCE_VALUE, 1)
    }

    fun setValue(count: Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_VALUE, count)
        editor.apply()
    }
}

class FavoritePhase(context: Context) {

    private val PREFERENCE_NAME = "Phase_Preference"
    private val PREFERENCE_VALUE = "Phase_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue(): Int {
        return preference.getInt(PREFERENCE_VALUE, 1)
    }

    fun setValue(count: Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_VALUE, count)
        editor.apply()
    }
}

class ElectronegativityPref(context: Context) {

    private  val PREFERENCE_NAME = "Electronegativity_Preference"
    private  val PREFERENCE_VALUE = "Electronegativity_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue(): Int {
        return preference.getInt(PREFERENCE_VALUE, 1)
    }

    fun setValue(count: Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_VALUE, count)
        editor.apply()
    }
}

class DensityPref(context: Context) {

    private val PREFERENCE_NAME = "Density_Preference"
    private val PREFERENCE_VALUE = "Density_Value"

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

class DegreePref(context: Context) {

    private val PREFERENCE_NAME = "Degree_Preference"
    private val PREFERENCE_VALUE = "Degree_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue(): Int {
        return preference.getInt(PREFERENCE_VALUE, 0) //0 = Kelving, 1 == Celsius, 2 = Fahrenheit
    }

    fun setValue(count: Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_VALUE, count)
        editor.apply()
    }
}

class BoilingPref(context: Context) {

    private val PREFERENCE_NAME = "Boiling_Preference"
    private val PREFERENCE_VALUE = "Boiling_Value"

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

class MeltingPref(context: Context) {

    private val PREFERENCE_NAME = "Melting_Preference"
    private val PREFERENCE_VALUE = "Melting_Value"

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

class AtomicRadiusEmpPref(context: Context) {
    private val PREFERENCE_NAME = "Radius_Emp_Preference"
    private val PREFERENCE_VALUE = "Radius_Emp_Value"
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

class AtomicRadiusCalPref(context: Context) {
    private val PREFERENCE_NAME = "Radius_Cal_Preference"
    private val PREFERENCE_VALUE = "Radius_Cal_Value"
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

class AtomicCovalentPref(context: Context) {
    private val PREFERENCE_NAME = "Radius_Covalent_Preference"
    private val PREFERENCE_VALUE = "Radius_Covalent_Value"
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

class AtomicVanPref(context: Context) {
    private val PREFERENCE_NAME = "Radius_Van_Preference"
    private val PREFERENCE_VALUE = "Radius_Van_Value"
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

class SpecificHeatPref(context: Context) {

    private val PREFERENCE_NAME = "Specific_Heat_Preference"
    private val PREFERENCE_VALUE = "Specific_Heat_Value"

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

class FusionHeatPref(context: Context) {

    private val PREFERENCE_NAME = "Fusion_Heat_Preference"
    private val PREFERENCE_VALUE = "Fusion_Heat_Value"

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

class VaporizationHeatPref(context: Context) {

    private val PREFERENCE_NAME = "Vaporization_Heat_Preference"
    private val PREFERENCE_VALUE = "Vaporization_Heat_Value"

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
