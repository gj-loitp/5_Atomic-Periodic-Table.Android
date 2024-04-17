package com.mckimquyen.atomicPeriodicTable.activities.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activities.BaseActivity
import com.mckimquyen.atomicPeriodicTable.pref.AtomicCovalentPreference
import com.mckimquyen.atomicPeriodicTable.pref.AtomicRadiusCalPreference
import com.mckimquyen.atomicPeriodicTable.pref.AtomicRadiusEmpPreference
import com.mckimquyen.atomicPeriodicTable.pref.AtomicVanPreference
import com.mckimquyen.atomicPeriodicTable.pref.BoilingPreference
import com.mckimquyen.atomicPeriodicTable.pref.DegreePreference
import com.mckimquyen.atomicPeriodicTable.pref.DensityPreference
import com.mckimquyen.atomicPeriodicTable.pref.ElectronegativityPreference
import com.mckimquyen.atomicPeriodicTable.pref.FavoriteBarPreferences
import com.mckimquyen.atomicPeriodicTable.pref.FavoritePhase
import com.mckimquyen.atomicPeriodicTable.pref.FusionHeatPreference
import com.mckimquyen.atomicPeriodicTable.pref.MeltingPreference
import com.mckimquyen.atomicPeriodicTable.pref.SpecificHeatPreference
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import com.mckimquyen.atomicPeriodicTable.pref.VaporizationHeatPreference
import kotlinx.android.synthetic.main.a_favorite_settings_page.*
class FavoritePageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        val themePref = ThemePref(this)
        val themePrefValue = themePref.getValue()

        if (themePrefValue == 100) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    setTheme(R.style.AppTheme)
                }

                Configuration.UI_MODE_NIGHT_YES -> {
                    setTheme(R.style.AppThemeDark)
                }
            }
        }
        if (themePrefValue == 0) {
            setTheme(R.style.AppTheme)
        }
        if (themePrefValue == 1) {
            setTheme(R.style.AppThemeDark)
        }
        setContentView(R.layout.a_favorite_settings_page)

        val molarPreference = FavoriteBarPreferences(this)
        val molarPrefValue = molarPreference.getValue()
        if (molarPrefValue == 1) {
            molarMassCheck.isChecked = true
        }
        if (molarPrefValue == 0) {
            molarMassCheck.isChecked = false
        }

        val phasePreferences = FavoritePhase(this)
        val phasePrefValue = phasePreferences.getValue()
        if (phasePrefValue == 1) {
            phaseCheck.isChecked = true
        }
        if (phasePrefValue == 0) {
            phaseCheck.isChecked = false
        }

        val electronegativityPreferences = ElectronegativityPreference(this)
        val electronegativityPrefValue = electronegativityPreferences.getValue()
        if (electronegativityPrefValue == 1) {
            electronegativityCheck.isChecked = true
        }
        if (electronegativityPrefValue == 0) {
            electronegativityCheck.isChecked = false
        }

        //Density
        val densityPreference = DensityPreference(this)
        val densityPrefValue = densityPreference.getValue()
        if (densityPrefValue == 1) {
            densityCheck.isChecked = true
        }
        if (densityPrefValue == 0) {
            densityCheck.isChecked = false
        }

        //Degree
        val degreePreference = DegreePreference(this)
        val degreePrefValue = degreePreference.getValue()
        if (degreePrefValue == 0) {
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
        }
        if (degreePrefValue == 1) {
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
        }
        if (degreePrefValue == 2) {
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
        }

        //Boiling Point
        val boilingPreference = BoilingPreference(this)
        val boilingPrefValue = boilingPreference.getValue()
        if (boilingPrefValue == 1) {
            boilingCheck.isChecked = true
        }
        if (boilingPrefValue == 0) {
            boilingCheck.isChecked = false
        }

        //Melting Point
        val meltingPreference = MeltingPreference(this)
        val meltingPrefValue = meltingPreference.getValue()
        if (meltingPrefValue == 1) {
            meltingCheck.isChecked = true
        }
        if (meltingPrefValue == 0) {
            meltingCheck.isChecked = false
        }

        //Atomic Radius Emp Point
        val atomicEmpPreference = AtomicRadiusEmpPreference(this)
        val atomicEmpPrefValue = atomicEmpPreference.getValue()
        if (atomicEmpPrefValue == 1) {
            atomicRadiusEmpiricalCheck.isChecked = true
        }
        if (atomicEmpPrefValue == 0) {
            atomicRadiusEmpiricalCheck.isChecked = false
        }

        //Atomic Radius Cal Point
        val atomicCalPreference = AtomicRadiusCalPreference(this)
        val atomicCalPrefValue = atomicCalPreference.getValue()
        if (atomicCalPrefValue == 1) {
            atomicRadiusCalculatedCheck.isChecked = true
        }
        if (atomicCalPrefValue == 0) {
            atomicRadiusCalculatedCheck.isChecked = false
        }

        //Covalent Radius Point
        val covalentPreference = AtomicCovalentPreference(this)
        val atomicCovalentPrefValue = covalentPreference.getValue()
        if (atomicCovalentPrefValue == 1) {
            covalentRadiusCheck.isChecked = true
        }
        if (atomicCovalentPrefValue == 0) {
            covalentRadiusCheck.isChecked = false
        }

        //Covalent Radius Point
        val vanPreference = AtomicVanPreference(this)
        val vanprefValue = vanPreference.getValue()
        if (vanprefValue == 1) {
            vanDerWaalsRadiusCheck.isChecked = true
        }
        if (vanprefValue == 0) {
            vanDerWaalsRadiusCheck.isChecked = false
        }

        //Specific Heat Capacity
        val specificHeatPreference = SpecificHeatPreference(this)
        val specificHeatValue = specificHeatPreference.getValue()
        if (specificHeatValue == 1) {
            specificHeatCheck.isChecked = true
        }
        if (specificHeatValue == 0) {
            specificHeatCheck.isChecked = false
        }

        //Fusion Heat
        val fusionheatPreference = FusionHeatPreference(this)
        val fusionHeatValue = fusionheatPreference.getValue()
        if (fusionHeatValue == 1) {
            fusionHeatCheck.isChecked = true
        }
        if (fusionHeatValue == 0) {
            fusionHeatCheck.isChecked = false
        }

        //Vaporization heat
        val vaporizationHeatPreference = VaporizationHeatPreference(this)
        val vaporizationHeatValue = vaporizationHeatPreference.getValue()
        if (vaporizationHeatValue == 1) {
            vaporizationHeatCheck.isChecked = true
        }
        if (vaporizationHeatValue == 0) {
            vaporizationHeatCheck.isChecked = false
        }
        onCheckboxClicked()
        viewf.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        //Title Controller
        commonTitleBackFavColor.visibility = View.INVISIBLE
        favoriteSetTitle.visibility = View.INVISIBLE
        commonTitleBackFav.elevation = (resources.getDimension(R.dimen.zero_elevation))
        favSetScroll.viewTreeObserver
            .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                var y = 300f
                override fun onScrollChanged() {
                    if (favSetScroll.scrollY > 150) {
                        commonTitleBackFavColor.visibility = View.VISIBLE
                        favoriteSetTitle.visibility = View.VISIBLE
                        favoriteSetTitleDownstate.visibility = View.INVISIBLE
                        commonTitleBackFav.elevation =
                            (resources.getDimension(R.dimen.one_elevation))
                    } else {
                        commonTitleBackFavColor.visibility = View.INVISIBLE
                        favoriteSetTitle.visibility = View.INVISIBLE
                        favoriteSetTitleDownstate.visibility = View.VISIBLE
                        commonTitleBackFav.elevation =
                            (resources.getDimension(R.dimen.zero_elevation))
                    }
                    y = favSetScroll.scrollY.toFloat()
                }
            })

        backBtnFav.setOnClickListener {
            this.onBackPressed()
        }
    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        val params = commonTitleBackFav.layoutParams as ViewGroup.LayoutParams
        params.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackFav.layoutParams = params

        val params2 = favoriteSetTitleDownstate.layoutParams as ViewGroup.MarginLayoutParams
        params2.topMargin =
            top + resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(
                R.dimen.header_down_margin
            )
        favoriteSetTitleDownstate.layoutParams = params2
    }

    private fun onCheckboxClicked() {
        //Molar Mass
        val molarPreference = FavoriteBarPreferences(this)
        var molarPrefValue = molarPreference.getValue()
        val checkBox: CheckBox = molarMassCheck
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                molarPreference.setValue(1)
            } else {
                molarPreference.setValue(0)
            }
        }

        kelvinBtn.setOnClickListener {
            val degreePreference = DegreePreference(this)
            var degreePrefValue = degreePreference.getValue()

            degreePreference.setValue(0)
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
        }
        celsiusBtn.setOnClickListener {
            val degreePreference = DegreePreference(this)
            var degreePrefValue = degreePreference.getValue()

            degreePreference.setValue(1)
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
        }
        fahrenheitbtn.setOnClickListener {
            val degreePreference = DegreePreference(this)
            var degreePrefValue = degreePreference.getValue()

            degreePreference.setValue(2)
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
        }

        //STP Phase
        val phasePreference = FavoritePhase(this)
        var phasePrefValue = phasePreference.getValue()
        val phaseCheckBox: CheckBox = phaseCheck
        phaseCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                phasePreference.setValue(1)
            } else {
                phasePreference.setValue(0)
            }
        }

        //Electronegativity
        val electronegativityPreference = ElectronegativityPreference(this)
        var electronegativityPrefValue = electronegativityPreference.getValue()
        val electronegativityCheckBox: CheckBox = electronegativityCheck
        electronegativityCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                electronegativityPreference.setValue(1)
            } else {
                electronegativityPreference.setValue(0)
            }
        }

        //Density
        val densityPreference = DensityPreference(this)
        var densityPrefValue = densityPreference.getValue()
        val densityCheckBox: CheckBox = densityCheck
        densityCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                densityPreference.setValue(1)
            } else {
                densityPreference.setValue(0)
            }

        }

        //Boiling Point
        val boilingPreference = BoilingPreference(this)
        var boilingPrefValue = boilingPreference.getValue()
        val boilingCheckBox: CheckBox = boilingCheck
        boilingCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                boilingPreference.setValue(1)
            } else {
                boilingPreference.setValue(0)
            }

        }

        //Melting Point
        val meltingPreference = MeltingPreference(this)
        val meltingCheckBox: CheckBox = meltingCheck
        meltingCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                meltingPreference.setValue(1)
            } else {
                meltingPreference.setValue(0)
            }
        }

        //Atomic Radius Empirical Point
        val atomicEmpiricalPreference = AtomicRadiusEmpPreference(this)
        val atomicEmpiricalBox: CheckBox = atomicRadiusEmpiricalCheck
        atomicEmpiricalBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                atomicEmpiricalPreference.setValue(1)
            } else {
                atomicEmpiricalPreference.setValue(0)
            }
        }

        //Atomic Radius Calculated Point
        val atomicCalculatedPreference = AtomicRadiusCalPreference(this)
        val atomicCalculatedBox: CheckBox = atomicRadiusCalculatedCheck
        atomicCalculatedBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                atomicCalculatedPreference.setValue(1)
            } else {
                atomicCalculatedPreference.setValue(0)
            }
        }

        //Covalent Radius Point
        val covalentPreference = AtomicCovalentPreference(this)
        val covalentCheckBox: CheckBox = covalentRadiusCheck
        covalentCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                covalentPreference.setValue(1)
            } else {
                covalentPreference.setValue(0)
            }
        }

        //Van Der Waals Radius Point
        val vanPreference = AtomicVanPreference(this)
        val vanCheckBox: CheckBox = vanDerWaalsRadiusCheck
        vanCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vanPreference.setValue(1)
            } else {
                vanPreference.setValue(0)
            }
        }

        //Specific Heat Point
        val specificHeatPreference = SpecificHeatPreference(this)
        var specificHeatValue = specificHeatPreference.getValue()
        val specificCheckBox: CheckBox = specificHeatCheck
        specificCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                specificHeatPreference.setValue(1)
            } else {
                specificHeatPreference.setValue(0)
            }

        }

        //Fusion Heat
        val fusionHeatPreference = FusionHeatPreference(this)
        var fusionHeatValue = fusionHeatPreference.getValue()
        val fusionCheckBox: CheckBox = fusionHeatCheck
        fusionCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fusionHeatPreference.setValue(1)
            } else {
                fusionHeatPreference.setValue(0)
            }

        }

        //Vapor Heat
        val vaporizationHeatPreference = VaporizationHeatPreference(this)
        var vaporizationHeatValue = vaporizationHeatPreference.getValue()
        val vaporizationCheckBox: CheckBox = vaporizationHeatCheck
        vaporizationCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vaporizationHeatPreference.setValue(1)
            } else {
                vaporizationHeatPreference.setValue(0)
            }

        }

    }

}
