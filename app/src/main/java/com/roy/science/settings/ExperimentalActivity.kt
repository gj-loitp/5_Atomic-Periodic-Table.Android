package com.roy.science.settings

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.roy.science.R
import com.roy.science.activities.BaseActivity
import com.roy.science.preferences.ThemePreference
import kotlinx.android.synthetic.main.activity_experimental_settings_page.*

class ExperimentalActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePreference = ThemePreference(this)
        var themePrefValue = themePreference.getValue()
        if (themePrefValue == 0) {
            setTheme(R.style.AppTheme)
        }
        if (themePrefValue == 1) {
            setTheme(R.style.AppThemeDark)
        }
        setContentView(R.layout.activity_experimental_settings_page) //Don't move down (Needs to be before we call our functions)

        //onClickListeners() //Disabled as a result of conflicts between ACTION_DOWN and ScrollView
        viewe.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        back_btn_exp.setOnClickListener {
            this.onBackPressed()
        }
    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        val params = common_title_back_exp.layoutParams as ViewGroup.LayoutParams
        params.height += top
        common_title_back_exp.layoutParams = params

        val params2 = general_header_exp.layoutParams as ViewGroup.MarginLayoutParams
        params2.topMargin += top
        general_header_exp.layoutParams = params2
    }


}



