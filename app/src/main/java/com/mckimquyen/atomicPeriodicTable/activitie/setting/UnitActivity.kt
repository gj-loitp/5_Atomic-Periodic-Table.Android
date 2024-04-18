package com.mckimquyen.atomicPeriodicTable.activitie.setting

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activitie.BaseActivity
import com.mckimquyen.atomicPeriodicTable.pref.TemperatureUnits
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import kotlinx.android.synthetic.main.a_unit.backBtnUnit
import kotlinx.android.synthetic.main.a_unit.celsiusBtn
import kotlinx.android.synthetic.main.a_unit.commonTitleBackunit
import kotlinx.android.synthetic.main.a_unit.commonTitleBackUnitColor
import kotlinx.android.synthetic.main.a_unit.fahrenheitbtn
import kotlinx.android.synthetic.main.a_unit.kelvinBtn
import kotlinx.android.synthetic.main.a_unit.unitScroll
import kotlinx.android.synthetic.main.a_unit.unitTitle
import kotlinx.android.synthetic.main.a_unit.unitTitleDownstate
import kotlinx.android.synthetic.main.a_unit.viewUnit

class UnitActivity : BaseActivity() {
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

        setContentView(R.layout.a_unit) //REMEMBER: Never move any function calls above this
        viewUnit.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        //Title Controller
        commonTitleBackUnitColor.visibility = View.INVISIBLE
        unitTitle.visibility = View.INVISIBLE
        commonTitleBackunit.elevation = (resources.getDimension(R.dimen.zero_elevation))
        unitScroll.viewTreeObserver
            .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                var y = 300f
                override fun onScrollChanged() {
                    if (unitScroll.scrollY > 150) {
                        commonTitleBackUnitColor.visibility = View.VISIBLE
                        unitTitle.visibility = View.VISIBLE
                        unitTitleDownstate.visibility = View.INVISIBLE
                        commonTitleBackunit.elevation =
                            (resources.getDimension(R.dimen.one_elevation))
                    } else {
                        commonTitleBackUnitColor.visibility = View.INVISIBLE
                        unitTitle.visibility = View.INVISIBLE
                        unitTitleDownstate.visibility = View.VISIBLE
                        commonTitleBackunit.elevation =
                            (resources.getDimension(R.dimen.zero_elevation))
                    }
                    y = unitScroll.scrollY.toFloat()
                }
            })

        tempUnits()

        backBtnUnit.setOnClickListener {
            this.onBackPressed()
        }
    }

    private fun tempUnits() {
        val tempPreference = TemperatureUnits(this)
        val tempPrefValue = tempPreference.getValue()
        if (tempPrefValue == "kelvin") {
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
        }
        if (tempPrefValue == "celsius") {
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
        }
        if (tempPrefValue == "fahrenheit") {
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
        }
        kelvinBtn.setOnClickListener {
            tempPreference.setValue("kelvin")
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
        }
        celsiusBtn.setOnClickListener {
            tempPreference.setValue("celsius")
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
        }
        fahrenheitbtn.setOnClickListener {
            tempPreference.setValue("fahrenheit")
            kelvinBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            celsiusBtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_outline)
            fahrenheitbtn.background = ContextCompat.getDrawable(this, R.drawable.shape_chip_active)
        }
    }

    override fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int
    ) {
        val paramsTitle = commonTitleBackunit.layoutParams as ViewGroup.LayoutParams
        paramsTitle.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackunit.layoutParams = paramsTitle

        val paramsLin = unitTitleDownstate.layoutParams as ViewGroup.MarginLayoutParams
        paramsLin.topMargin =
            top + resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(
                R.dimen.header_down_margin
            )
        unitTitleDownstate.layoutParams = paramsLin
    }
}
