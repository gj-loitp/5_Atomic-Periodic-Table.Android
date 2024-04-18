package com.mckimquyen.atomicPeriodicTable.act.setting

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.mckimquyen.atomicPeriodicTable.BuildConfig
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.act.BaseAct
import com.mckimquyen.atomicPeriodicTable.ext.openUrlInBrowser
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import kotlinx.android.synthetic.main.a_info.backBtn
import kotlinx.android.synthetic.main.a_info.btGithub
import kotlinx.android.synthetic.main.a_info.btGithubForked
import kotlinx.android.synthetic.main.a_info.commonTitleBackInfo
import kotlinx.android.synthetic.main.a_info.imageView3
import kotlinx.android.synthetic.main.a_info.titleBoxInfo
import kotlinx.android.synthetic.main.a_info.versionNumber
import kotlinx.android.synthetic.main.a_info.viewInfo

class AboutAct : BaseAct() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    @SuppressLint("SetTextI18n")
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
        setContentView(R.layout.a_info)

        viewInfo.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        versionNumber.text = "Version ${BuildConfig.VERSION_NAME}"

        backBtn.setOnClickListener {
            this.onBackPressed()
        }
        btGithubForked.setOnClickListener {
            openUrlInBrowser("https://github.com/gj-loitp/Atomic-Periodic-Table.Android")
        }
        btGithub.setOnClickListener {
            openUrlInBrowser("https://github.com/JLindemann42/Atomic-Periodic-Table.Android")
        }
    }

    override fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int,
    ) {
        val params = commonTitleBackInfo.layoutParams as ViewGroup.LayoutParams
        params.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackInfo.layoutParams = params

        val params2 = imageView3.layoutParams as ViewGroup.MarginLayoutParams
        params2.topMargin += top
        imageView3.layoutParams = params2

        val titleParam = titleBoxInfo.layoutParams as ViewGroup.MarginLayoutParams
        titleParam.rightMargin = right
        titleParam.leftMargin = left
        titleBoxInfo.layoutParams = titleParam

    }
}
