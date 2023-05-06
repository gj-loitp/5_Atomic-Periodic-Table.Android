package com.roy.science.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import com.roy.science.R
import com.roy.science.activities.settings.AboutActivity
import com.roy.science.activities.settings.FavoritePageActivity
import com.roy.science.activities.settings.LicensesActivity
import com.roy.science.activities.settings.OrderActivity
import com.roy.science.activities.settings.SubmitActivity
import com.roy.science.activities.settings.UnitActivity
import com.roy.science.preferences.ThemePreference
import com.roy.science.preferences.offlinePreference
import com.roy.science.settings.ExperimentalActivity
import com.roy.science.utils.Utils
import kotlinx.android.synthetic.main.activity_settings.aboutSettings
import kotlinx.android.synthetic.main.activity_settings.advancedBox
import kotlinx.android.synthetic.main.activity_settings.backBtnSet
import kotlinx.android.synthetic.main.activity_settings.cacheLay
import kotlinx.android.synthetic.main.activity_settings.commonTitleBackSet
import kotlinx.android.synthetic.main.activity_settings.commonTitleSettingsColor
import kotlinx.android.synthetic.main.activity_settings.elementTitle
import kotlinx.android.synthetic.main.activity_settings.elementTitleDownstate
import kotlinx.android.synthetic.main.activity_settings.experimentalSettings
import kotlinx.android.synthetic.main.activity_settings.favoriteSettings
import kotlinx.android.synthetic.main.activity_settings.licensesSettings
import kotlinx.android.synthetic.main.activity_settings.offlineInternetSwitch
import kotlinx.android.synthetic.main.activity_settings.offlineSettings
import kotlinx.android.synthetic.main.activity_settings.orderSettings
import kotlinx.android.synthetic.main.activity_settings.personalizationBox
import kotlinx.android.synthetic.main.activity_settings.scrollSettings
import kotlinx.android.synthetic.main.activity_settings.submitSettings
import kotlinx.android.synthetic.main.activity_settings.themePanel
import kotlinx.android.synthetic.main.activity_settings.themesSettings
import kotlinx.android.synthetic.main.activity_settings.title_box_settings
import kotlinx.android.synthetic.main.activity_settings.unitSettings
import kotlinx.android.synthetic.main.activity_settings.view
import kotlinx.android.synthetic.main.theme_panel.cancelBtn
import kotlinx.android.synthetic.main.theme_panel.darkBtn
import kotlinx.android.synthetic.main.theme_panel.lightBtn
import kotlinx.android.synthetic.main.theme_panel.systemDefaultBtn
import kotlinx.android.synthetic.main.theme_panel.themeBackground
import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow
import kotlin.system.exitProcess

class SettingsActivity : BaseActivity() {

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
        setContentView(R.layout.activity_settings)

        if (themePrefValue == 100) {
            systemDefaultBtn.setCompoundDrawablesWithIntrinsicBounds(
                /* left = */ R.drawable.ic_radio_checked,
                /* top = */ 0,
                /* right = */ 0,
                /* bottom = */ 0
            )
            lightBtn.setCompoundDrawablesWithIntrinsicBounds(
                /* left = */ R.drawable.ic_radio_unchecked,
                /* top = */ 0,
                /* right = */ 0,
                /* bottom = */ 0
            )
            darkBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_unchecked, 0, 0, 0)
        }
        if (themePrefValue == 0) {
            systemDefaultBtn.setCompoundDrawablesWithIntrinsicBounds(
                /* left = */ R.drawable.ic_radio_unchecked,
                /* top = */ 0,
                /* right = */ 0,
                /* bottom = */ 0
            )
            lightBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_checked, 0, 0, 0)
            darkBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_unchecked, 0, 0, 0)
        }
        if (themePrefValue == 1) {
            systemDefaultBtn.setCompoundDrawablesWithIntrinsicBounds(
                /* left = */ R.drawable.ic_radio_unchecked,
                /* top = */ 0,
                /* right = */ 0,
                /* bottom = */ 0
            )
            lightBtn.setCompoundDrawablesWithIntrinsicBounds(
                /* left = */ R.drawable.ic_radio_unchecked,
                /* top = */ 0,
                /* right = */ 0,
                /* bottom = */ 0
            )
            darkBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_checked, 0, 0, 0)
        }

        openPages()
        themeSettings()
        initializeCache()
        cacheSettings()
        initOfflineSwitches()

        offlineSettings.setOnClickListener {
            offlineInternetSwitch.toggle()
        }

        view.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        //Title Controller
        commonTitleSettingsColor.visibility = View.INVISIBLE
        elementTitle.visibility = View.INVISIBLE
        commonTitleBackSet.elevation = (resources.getDimension(R.dimen.zero_elevation))
        scrollSettings.viewTreeObserver
            .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                var y = 300f
                override fun onScrollChanged() {
                    if (scrollSettings.scrollY > 150) {
                        commonTitleSettingsColor.visibility = View.VISIBLE
                        elementTitle.visibility = View.VISIBLE
                        elementTitleDownstate.visibility = View.INVISIBLE
                        commonTitleBackSet.elevation =
                            (resources.getDimension(R.dimen.one_elevation))
                    } else {
                        commonTitleSettingsColor.visibility = View.INVISIBLE
                        elementTitle.visibility = View.INVISIBLE
                        elementTitleDownstate.visibility = View.VISIBLE
                        commonTitleBackSet.elevation =
                            (resources.getDimension(R.dimen.zero_elevation))
                    }
                    y = scrollSettings.scrollY.toFloat()
                }
            })

        aboutSettings.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
        backBtnSet.setOnClickListener {
            this.onBackPressed()
        }
        submitSettings.setOnClickListener {
            val intent = Intent(this, SubmitActivity::class.java)
            startActivity(intent)
        }
        licensesSettings.setOnClickListener {
            val intent = Intent(this, LicensesActivity::class.java)
            startActivity(intent)
        }
        unitSettings.setOnClickListener {
            val intent = Intent(this, UnitActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int
    ) {
        val params = commonTitleBackSet.layoutParams as ViewGroup.LayoutParams
        params.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackSet.layoutParams = params

        val titleParam = title_box_settings.layoutParams as ViewGroup.MarginLayoutParams
        titleParam.rightMargin = right
        titleParam.leftMargin = left
        title_box_settings.layoutParams = titleParam

        val params2 = elementTitleDownstate.layoutParams as ViewGroup.MarginLayoutParams
        params2.topMargin =
            top + resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(
                R.dimen.header_down_margin
            )
        elementTitleDownstate.layoutParams = params2

        personalizationBox.setPadding(left, 0, right, 0)
        advancedBox.setPadding(left, 0, right, 0)

    }

    override fun onBackPressed() {
        if (themePanel.visibility == View.VISIBLE) {
            Utils.fadeOutAnim(themePanel, 300) //Start Close Animation
            return
        } else {
            super.onBackPressed()
        }
    }

    private fun initOfflineSwitches() {
        val offlinePreferences = offlinePreference(this)
        val offlinePrefValue = offlinePreferences.getValue()
        offlineInternetSwitch.isChecked = offlinePrefValue == 1

        offlineInternetSwitch.setOnCheckedChangeListener { _, _ ->
            if (offlineInternetSwitch.isChecked) {
                val offlinePreference = offlinePreference(this)
                offlinePreference.setValue(1)
            } else {
                val offlinePreference = offlinePreference(this)
                offlinePreference.setValue(0)
            }
        }
    }

    private fun openPages() {
        favoriteSettings.setOnClickListener {
            val intent = Intent(this, FavoritePageActivity::class.java)
            startActivity(intent)
        }
        orderSettings.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }
        experimentalSettings.setOnClickListener {
            val intent = Intent(this, ExperimentalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cacheSettings() {
        cacheLay.setOnClickListener {
            this.cacheDir.deleteRecursively()
            initializeCache()
        }
    }

    private fun initializeCache() {
        var size: Long = 0
        size += getDirSize(this.cacheDir)
        size += getDirSize(this.externalCacheDir)
        (findViewById<View>(R.id.clear_cache_content) as TextView).text = readableFileSize(size)
    }

    private fun getDirSize(dir: File?): Long {
        var size: Long = 0
        dir?.listFiles()?.let {
            for (file in it) {
                if (file != null && file.isDirectory) {
                    size += getDirSize(file)
                } else if (file != null && file.isFile) {
                    size += file.length()
                }
            }
        }
        return size
    }

    private fun readableFileSize(size: Long): String {
        if (size <= 0) return "0 Bytes"
        val units = arrayOf("Bytes", "kB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble()))
            .toString() + " " + units[digitGroups]
    }

    private fun themeSettings() {
        systemDefaultBtn.setOnClickListener {
            val themePreference = ThemePreference(this)
            themePreference.setValue(100)
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    setTheme(R.style.AppTheme)
                } // Night mode is not active, we're using the light theme
                Configuration.UI_MODE_NIGHT_YES -> {
                    setTheme(R.style.AppThemeDark)
                } // Night mode is active, we're using dark theme
            }
            Utils.fadeOutAnim(themePanel, 300)
            val delayChange = Handler(Looper.getMainLooper())
            delayChange.postDelayed({
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                SettingsActivity().finish()
                exitProcess(0)
                overridePendingTransition(0, 0)
            }, 302)
        }
        lightBtn.setOnClickListener {
            val themePreference = ThemePreference(this)
            themePreference.setValue(0)
            setTheme(R.style.AppTheme)
            Utils.fadeOutAnim(themePanel, 300)

            val delayChange = Handler(Looper.getMainLooper())
            delayChange.postDelayed({
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                SettingsActivity().finish()
                exitProcess(0)
                overridePendingTransition(0, 0)
            }, 302)
        }
        darkBtn.setOnClickListener {
            val themePreference = ThemePreference(this)
            themePreference.setValue(1)
            setTheme(R.style.AppThemeDark)
            Utils.fadeOutAnim(themePanel, 300)

            val delayChange = Handler(Looper.getMainLooper())
            delayChange.postDelayed({
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                SettingsActivity().finish()
                exitProcess(0)
                overridePendingTransition(0, 0)
            }, 302)
        }
        themesSettings.setOnClickListener {
            Utils.fadeInAnim(view = themePanel, time = 300)
        }
        themeBackground.setOnClickListener {
            Utils.fadeOutAnim(themePanel, 300)
        }
        cancelBtn.setOnClickListener {
            Utils.fadeOutAnim(themePanel, 300)
        }
    }
}
