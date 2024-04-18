package com.mckimquyen.atomicPeriodicTable.activitie.table

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activitie.BaseAct
import com.mckimquyen.atomicPeriodicTable.model.Indicator
import com.mckimquyen.atomicPeriodicTable.model.IndicatorModel
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import kotlinx.android.synthetic.main.a_ph.acidInfo
import kotlinx.android.synthetic.main.a_ph.alkalineInfo
import kotlinx.android.synthetic.main.a_ph.backBtnPh
import kotlinx.android.synthetic.main.a_ph.center
import kotlinx.android.synthetic.main.a_ph.commonTitleBackPh
import kotlinx.android.synthetic.main.a_ph.left
import kotlinx.android.synthetic.main.a_ph.neutralInfo
import kotlinx.android.synthetic.main.a_ph.phScroll
import kotlinx.android.synthetic.main.a_ph.right
import kotlinx.android.synthetic.main.a_ph.viewPh
import kotlinx.android.synthetic.main.view_bar_ph_chips.bromothymolBlueBtn
import kotlinx.android.synthetic.main.view_bar_ph_chips.congoRedBtn
import kotlinx.android.synthetic.main.view_bar_ph_chips.methylOrangeBtn
import kotlinx.android.synthetic.main.view_bar_ph_chips.phenolphthaleinBtn

class PHAct : BaseAct() {
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
        setContentView(R.layout.a_ph) //REMEMBER: Never move any function calls above this

        indicatorListener()
        viewPh.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //Title Controller

        backBtnPh.setOnClickListener {
            this.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun indicatorListener() {
        val indicatorList = ArrayList<Indicator>()
        IndicatorModel.getList(indicatorList)
        val acidText = "[H+]>[OH-] pH<"
        val neutralText = "[H+]=[OH-] pH="
        val alkalineText = "[H+]<[OH-] pH>"

        val item = indicatorList[0]
        acidInfo.text = acidText + item.acid
        neutralInfo.text = neutralText + item.neutral
        alkalineInfo.text = alkalineText + item.alkali
        updatePhColor(item)
        updateButtonColor("bromothymol_blue_btn")

        bromothymolBlueBtn.setOnClickListener {
            val item = indicatorList[0]
            acidInfo.text = acidText + item.acid
            neutralInfo.text = neutralText + item.neutral
            alkalineInfo.text = alkalineText + item.alkali
            updatePhColor(item)
            updateButtonColor("bromothymol_blue_btn")
        }
        methylOrangeBtn.setOnClickListener {
            val item = indicatorList[1]
            acidInfo.text = acidText + item.acid
            neutralInfo.text = neutralText + item.neutral
            alkalineInfo.text = alkalineText + item.alkali
            updatePhColor(item)
            updateButtonColor("methyl_orange_btn")
        }
        congoRedBtn.setOnClickListener {
            val item = indicatorList[2]
            acidInfo.text = acidText + item.acid
            neutralInfo.text = neutralText + item.neutral
            alkalineInfo.text = alkalineText + item.alkali
            updatePhColor(item)
            updateButtonColor("congo_red_btn")
        }
        phenolphthaleinBtn.setOnClickListener {
            val item = indicatorList[3]
            acidInfo.text = acidText + item.acid
            neutralInfo.text = neutralText + item.neutral
            alkalineInfo.text = alkalineText + item.alkali
            updatePhColor(item)
            updateButtonColor("phenolphthalein_btn")
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun updatePhColor(item: Indicator) {
        try {
            val leftColor = resources.getIdentifier(item.acidColor, "color", packageName)
            val centerColor = resources.getIdentifier(item.neutralColor, "color", packageName)
            val rightColor = resources.getIdentifier(item.alkaliColor, "color", packageName)

            left.setColorFilter(
                ContextCompat.getColor(this, leftColor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            center.setColorFilter(
                ContextCompat.getColor(this, centerColor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            right.setColorFilter(
                ContextCompat.getColor(this, rightColor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "DiscouragedApi")
    private fun updateButtonColor(btn: String) {
        methylOrangeBtn.background = getDrawable(R.drawable.shape_chip)
        bromothymolBlueBtn.background = getDrawable(R.drawable.shape_chip)
        congoRedBtn.background = getDrawable(R.drawable.shape_chip)
        phenolphthaleinBtn.background = getDrawable(R.drawable.shape_chip)

        val delay = Handler(Looper.getMainLooper())
        delay.postDelayed({
            val resIDB = resources.getIdentifier(btn, "id", packageName)
            val button = findViewById<Button>(resIDB)
            button?.background = getDrawable(R.drawable.shape_chip_active)
        }, 1)
    }

    override fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int,
    ) {
        val paramsTitle = commonTitleBackPh.layoutParams as ViewGroup.LayoutParams
        paramsTitle.height = top + resources.getDimensionPixelSize(R.dimen.title_bar_ph)
        commonTitleBackPh.layoutParams = paramsTitle

        val pScroll = phScroll.layoutParams as ViewGroup.MarginLayoutParams
        pScroll.topMargin = top + resources.getDimensionPixelSize(R.dimen.title_bar_ph)
        phScroll.layoutParams = pScroll
    }
}
