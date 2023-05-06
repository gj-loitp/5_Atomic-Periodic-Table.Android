package com.roy.science.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.roy.science.R
import com.roy.science.animations.Anim
import com.roy.science.preferences.ThemePreference
import kotlinx.android.synthetic.main.activity_solubility.backBtn
import kotlinx.android.synthetic.main.activity_solubility.boxm
import kotlinx.android.synthetic.main.activity_solubility.common_title_back_sul
import kotlinx.android.synthetic.main.activity_solubility.info_btn
import kotlinx.android.synthetic.main.activity_solubility.info_panel
import kotlinx.android.synthetic.main.activity_solubility.viewSub
import kotlinx.android.synthetic.main.panel_info.info_back_btn
import kotlinx.android.synthetic.main.panel_info.info_background
import kotlinx.android.synthetic.main.panel_info.info_text
import kotlinx.android.synthetic.main.panel_info.info_title

class SolubilityActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePreference = ThemePreference(this)
        val themePrefValue = themePreference.getValue()

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
        setContentView(R.layout.activity_solubility) //Don't move down (Needs to be before we call our functions)
        viewSub.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        infoPanel()

        backBtn.setOnClickListener {
            this.onBackPressed()
        }
    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        val paramsO = boxm.layoutParams as ViewGroup.MarginLayoutParams
        paramsO.topMargin = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        boxm.layoutParams = paramsO

        val params2 = common_title_back_sul.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        common_title_back_sul.layoutParams = params2

    }

    private fun infoPanel() {
        info_btn.setOnClickListener {
            Anim.fadeIn(info_panel, 300)
            info_title.text = resources.getString(R.string.solubility_info_t)
            info_text.text = resources.getString(R.string.solubility_info_c)
        }
        info_back_btn.setOnClickListener {
            Anim.fadeOutAnim(info_panel, 300)
        }
        info_background.setOnClickListener {
            Anim.fadeOutAnim(info_panel, 300)
        }
    }
}



