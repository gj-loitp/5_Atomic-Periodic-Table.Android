package com.mckimquyen.atomicPeriodicTable.setting

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activitie.BaseActivity
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import kotlinx.android.synthetic.main.a_experimental_settings_page.backBtnExp
import kotlinx.android.synthetic.main.a_experimental_settings_page.commonTitleBackExp
import kotlinx.android.synthetic.main.a_experimental_settings_page.generalHeaderExp
import kotlinx.android.synthetic.main.a_experimental_settings_page.viewe

class ExperimentalActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        val themePref = ThemePref(this)
        val themePrefValue = themePref.getValue()
        if (themePrefValue == 0) {
            setTheme(R.style.AppTheme)
        }
        if (themePrefValue == 1) {
            setTheme(R.style.AppThemeDark)
        }
        setContentView(R.layout.a_experimental_settings_page) //Don't move down (Needs to be before we call our functions)

        //onClickListeners() //Disabled as a result of conflicts between ACTION_DOWN and ScrollView
        viewe.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        backBtnExp.setOnClickListener {
            this.onBackPressed()
        }
    }

    override fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int,
    ) {
        val params = commonTitleBackExp.layoutParams as ViewGroup.LayoutParams
        params.height += top
        commonTitleBackExp.layoutParams = params

        val params2 = generalHeaderExp.layoutParams as ViewGroup.MarginLayoutParams
        params2.topMargin += top
        generalHeaderExp.layoutParams = params2
    }

}
