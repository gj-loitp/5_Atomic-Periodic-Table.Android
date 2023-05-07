package com.roy.science.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.roy.science.R
import com.roy.science.anim.Anim
import com.roy.science.preferences.ThemePreference
import kotlinx.android.synthetic.main.activity_solubility.backBtn
import kotlinx.android.synthetic.main.activity_solubility.boxm
import kotlinx.android.synthetic.main.activity_solubility.commonTitleBackSul
import kotlinx.android.synthetic.main.activity_solubility.infoBtn
import kotlinx.android.synthetic.main.activity_solubility.infoPanel
import kotlinx.android.synthetic.main.activity_solubility.viewSub
import kotlinx.android.synthetic.main.v_panel_info.infoBackBtn
import kotlinx.android.synthetic.main.v_panel_info.infoBackground
import kotlinx.android.synthetic.main.v_panel_info.infoTitle
import kotlinx.android.synthetic.main.v_panel_info.tvInfoText

class SolubilityActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
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

    override fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int
    ) {
        val paramsO = boxm.layoutParams as ViewGroup.MarginLayoutParams
        paramsO.topMargin = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        boxm.layoutParams = paramsO

        val params2 = commonTitleBackSul.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackSul.layoutParams = params2

    }

    private fun infoPanel() {
        infoBtn.setOnClickListener {
            Anim.fadeIn(infoPanel, 300)
            infoTitle.text = resources.getString(R.string.solubility_info_t)
            tvInfoText.text = resources.getString(R.string.solubility_info_c)
        }
        infoBackBtn.setOnClickListener {
            Anim.fadeOutAnim(infoPanel, 300)
        }
        infoBackground.setOnClickListener {
            Anim.fadeOutAnim(infoPanel, 300)
        }
    }
}
