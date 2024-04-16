package com.mckimquyen.atomicPeriodicTable.preferences

import android.content.Context
import android.content.SharedPreferences

class FavoriteBarPreferences(context : Context) {

    val PREFERENCE_NAME = "Molar_Preference"
    val PREFERENCE_VALUE = "Molar_Value"

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

class FavoritePhase(context : Context) {

    val PREFERENCE_NAME = "Phase_Preference"
    val PREFERENCE_VALUE = "Phase_Value"

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

class ElectronegativityPreference(context : Context) {

    val PREFERENCE_NAME = "Electronegativity_Preference"
    val PREFERENCE_VALUE = "Electronegativity_Value"

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

class DensityPreference(context : Context) {

    val PREFERENCE_NAME = "Density_Preference"
    val PREFERENCE_VALUE = "Density_Value"

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

class DegreePreference(context : Context) {

    val PREFERENCE_NAME = "Degree_Preference"
    val PREFERENCE_VALUE = "Degree_Value"

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

class BoilingPreference(context : Context) {

    val PREFERENCE_NAME = "Boiling_Preference"
    val PREFERENCE_VALUE = "Boiling_Value"

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

class MeltingPreference(context : Context) {

    val PREFERENCE_NAME = "Melting_Preference"
    val PREFERENCE_VALUE = "Melting_Value"

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

class AtomicRadiusEmpPreference(context : Context) {
    val PREFERENCE_NAME = "Radius_Emp_Preference"
    val PREFERENCE_VALUE = "Radius_Emp_Value"
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

class AtomicRadiusCalPreference(context : Context) {
    val PREFERENCE_NAME = "Radius_Cal_Preference"
    val PREFERENCE_VALUE = "Radius_Cal_Value"
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

class AtomicCovalentPreference(context : Context) {
    val PREFERENCE_NAME = "Radius_Covalent_Preference"
    val PREFERENCE_VALUE = "Radius_Covalent_Value"
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

class AtomicVanPreference(context : Context) {
    val PREFERENCE_NAME = "Radius_Van_Preference"
    val PREFERENCE_VALUE = "Radius_Van_Value"
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

class SpecificHeatPreference(context : Context) {

    val PREFERENCE_NAME = "Specific_Heat_Preference"
    val PREFERENCE_VALUE = "Specific_Heat_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue() : Int{
        return preference.getInt (PREFERENCE_VALUE, 0)
    }

    fun setValue(count:Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_VALUE, count)
        editor.apply()
    }
}

class FusionHeatPreference(context : Context) {

    val PREFERENCE_NAME = "Fusion_Heat_Preference"
    val PREFERENCE_VALUE = "Fusion_Heat_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue() : Int{
        return preference.getInt (PREFERENCE_VALUE, 0)
    }

    fun setValue(count:Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_VALUE, count)
        editor.apply()
    }
}

class VaporizationHeatPreference(context : Context) {

    val PREFERENCE_NAME = "Vaporization_Heat_Preference"
    val PREFERENCE_VALUE = "Vaporization_Heat_Value"

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getValue() : Int{
        return preference.getInt (PREFERENCE_VALUE, 0)
    }

    fun setValue(count:Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_VALUE, count)
        editor.apply()
    }
}
