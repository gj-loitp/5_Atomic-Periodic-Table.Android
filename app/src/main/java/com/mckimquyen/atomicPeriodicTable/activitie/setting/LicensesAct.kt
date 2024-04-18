package com.mckimquyen.atomicPeriodicTable.activitie.setting

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activitie.BaseAct
import com.mckimquyen.atomicPeriodicTable.anim.Anim
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import kotlinx.android.synthetic.main.a_settings_licenses.backBtn
import kotlinx.android.synthetic.main.a_settings_licenses.commonTitleBackLic
import kotlinx.android.synthetic.main.a_settings_licenses.commonTitleBackLicColor
import kotlinx.android.synthetic.main.a_settings_licenses.lInc
import kotlinx.android.synthetic.main.a_settings_licenses.lSothreeBtn
import kotlinx.android.synthetic.main.a_settings_licenses.lWikiBtn
import kotlinx.android.synthetic.main.a_settings_licenses.licenseScroll
import kotlinx.android.synthetic.main.a_settings_licenses.licenseTitle
import kotlinx.android.synthetic.main.a_settings_licenses.licenseTitleDownstate
import kotlinx.android.synthetic.main.a_settings_licenses.viewLic
import kotlinx.android.synthetic.main.view_license_info.lBackBtn
import kotlinx.android.synthetic.main.view_license_info.lBackground3
import kotlinx.android.synthetic.main.view_license_info.lText
import kotlinx.android.synthetic.main.view_license_info.lTitle

class LicensesAct : BaseAct() {
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
        setContentView(R.layout.a_settings_licenses) //REMEMBER: Never move any function calls above this

        viewLic.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        //Title Controller
        commonTitleBackLicColor.visibility = View.INVISIBLE
        licenseTitle.visibility = View.INVISIBLE
        commonTitleBackLic.elevation = (resources.getDimension(R.dimen.zero_elevation))
        licenseScroll.viewTreeObserver
            .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                var y = 300f
                override fun onScrollChanged() {
                    if (licenseScroll.scrollY > 150) {
                        commonTitleBackLicColor.visibility = View.VISIBLE
                        licenseTitle.visibility = View.VISIBLE
                        licenseTitleDownstate.visibility = View.INVISIBLE
                        commonTitleBackLic.elevation =
                            (resources.getDimension(R.dimen.one_elevation))
                    } else {
                        commonTitleBackLicColor.visibility = View.INVISIBLE
                        licenseTitle.visibility = View.INVISIBLE
                        licenseTitleDownstate.visibility = View.VISIBLE
                        commonTitleBackLic.elevation =
                            (resources.getDimension(R.dimen.zero_elevation))
                    }
                    y = licenseScroll.scrollY.toFloat()
                }
            })

        listeners()
        backBtn.setOnClickListener {
            this.onBackPressed()
        }
    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        val params2 = commonTitleBackLic.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackLic.layoutParams = params2

        val params3 = licenseTitleDownstate.layoutParams as ViewGroup.MarginLayoutParams
        params3.topMargin =
            top + resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(
                R.dimen.header_down_margin
            )
        licenseTitleDownstate.layoutParams = params3
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (lInc.visibility == View.VISIBLE) {
            hideInfoPanel()
            return
        } else {
            super.onBackPressed()
        }
    }

    private fun listeners() {
        lWikiBtn.setOnClickListener {
            val title = resources.getString(R.string.wikipedia_license)
            val text = resources.getString(R.string.wikipedia_license_text)
            showInfoPanel(title, text)
        }
        lSothreeBtn.setOnClickListener {
            val title = resources.getString(R.string.sothree_license)
            val text = resources.getString(R.string.sothree_license_text)
            showInfoPanel(title, text)
        }
        lBackBtn.setOnClickListener { hideInfoPanel() }
        lBackground3.setOnClickListener { hideInfoPanel() }
    }

    private fun showInfoPanel(title: String, text: String) {
        Anim.fadeIn(lInc, 150)

        lTitle.text = title
        lText.text = text
    }

    private fun hideInfoPanel() {
        Anim.fadeOutAnim(view = lInc, time = 150)
    }
}
