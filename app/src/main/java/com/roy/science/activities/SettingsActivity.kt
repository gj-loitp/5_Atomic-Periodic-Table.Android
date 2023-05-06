package com.roy.science.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import com.roy.science.R
import com.roy.science.activities.settings.*
import com.roy.science.preferences.ThemePreference
import com.roy.science.preferences.offlinePreference
import com.roy.science.settings.ExperimentalActivity
import com.roy.science.utils.Utils
import kotlinx.android.synthetic.main.activity_element_info.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.element_title
import kotlinx.android.synthetic.main.activity_settings.view
import kotlinx.android.synthetic.main.theme_panel.*
import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePreference = ThemePreference(this)
        val themePrefValue = themePreference.getValue()

        if (themePrefValue == 100) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> { setTheme(R.style.AppTheme) }
                Configuration.UI_MODE_NIGHT_YES -> { setTheme(R.style.AppThemeDark) }
            }
        }
        if (themePrefValue == 0) { setTheme(R.style.AppTheme) }
        if (themePrefValue == 1) { setTheme(R.style.AppThemeDark) }
        setContentView(R.layout.activity_settings)

        if (themePrefValue == 100) {
            system_default_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_checked, 0, 0, 0)
            light_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_unchecked, 0, 0, 0)
            dark_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_unchecked, 0, 0, 0)
        }
        if (themePrefValue == 0) {
            system_default_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_unchecked, 0, 0, 0)
            light_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_checked, 0, 0, 0)
            dark_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_unchecked, 0, 0, 0)
        }
        if (themePrefValue == 1) {
            system_default_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_unchecked, 0, 0, 0)
            light_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_unchecked, 0, 0, 0)
            dark_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_checked, 0, 0, 0)
        }

        openPages()
        themeSettings()
        initializeCache()
        cacheSettings()
        initOfflineSwitches()

        offline_settings.setOnClickListener {
            offline_internet_switch.toggle()
        }

        view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        //Title Controller
        common_title_settings_color.visibility = View.INVISIBLE
        element_title.visibility = View.INVISIBLE
        common_title_back_set.elevation = (resources.getDimension(R.dimen.zero_elevation))
        scroll_settings.getViewTreeObserver()
            .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                var y = 300f
                override fun onScrollChanged() {
                    if (scroll_settings.getScrollY() > 150) {
                        common_title_settings_color.visibility = View.VISIBLE
                        element_title.visibility = View.VISIBLE
                        element_title_downstate.visibility = View.INVISIBLE
                        common_title_back_set.elevation = (resources.getDimension(R.dimen.one_elevation))
                    } else {
                        common_title_settings_color.visibility = View.INVISIBLE
                        element_title.visibility = View.INVISIBLE
                        element_title_downstate.visibility = View.VISIBLE
                        common_title_back_set.elevation = (resources.getDimension(R.dimen.zero_elevation))
                    }
                    y = scroll_settings.getScrollY().toFloat()
                }
            })

        about_settings.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
        back_btn_set.setOnClickListener {
            this.onBackPressed()
        }
        submit_settings.setOnClickListener {
            val intent = Intent(this, SubmitActivity::class.java)
            startActivity(intent)
        }
        licenses_settings.setOnClickListener {
            val intent = Intent(this, LicensesActivity::class.java)
            startActivity(intent)
        }
        unit_settings.setOnClickListener {
            val intent = Intent(this, UnitActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        val params = common_title_back_set.layoutParams as ViewGroup.LayoutParams
        params.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        common_title_back_set.layoutParams = params

        val titleParam = title_box_settings.layoutParams as ViewGroup.MarginLayoutParams
        titleParam.rightMargin = right
        titleParam.leftMargin = left
        title_box_settings.layoutParams = titleParam

        val params2 = element_title_downstate.layoutParams as ViewGroup.MarginLayoutParams
        params2.topMargin = top + resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(R.dimen.header_down_margin)
        element_title_downstate.layoutParams = params2

        personalization_box.setPadding(left, 0, right, 0)
        advanced_box.setPadding(left, 0, right, 0)

    }

    override fun onBackPressed() {
        if (theme_panel.visibility == View.VISIBLE) {
            Utils.fadeOutAnim(theme_panel, 300) //Start Close Animation
            return
        }
        else {
            super.onBackPressed()
        }
    }

    private fun initOfflineSwitches() {
        val offlinePreferences = offlinePreference(this)
        val offlinePrefValue = offlinePreferences.getValue()
        offline_internet_switch.isChecked = offlinePrefValue == 1

        offline_internet_switch.setOnCheckedChangeListener { compoundButton, b ->
            if (offline_internet_switch.isChecked) {
                val offlinePreference = offlinePreference(this)
                offlinePreference.setValue(1)
            } else {
                val offlinePreference = offlinePreference(this)
                offlinePreference.setValue(0)
            }
        }
    }

    private fun openPages() {
        favorite_settings.setOnClickListener {
            val intent = Intent(this, FavoritePageActivity::class.java)
            startActivity(intent)
        }
        order_settings.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }
        experimental_settings.setOnClickListener {
            val intent = Intent(this, ExperimentalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cacheSettings() {
        cache_lay.setOnClickListener {
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
        if (dir != null) {
            for (file in dir.listFiles()) {
                if (file != null && file.isDirectory) {
                    size += getDirSize(file)
                } else if (file != null && file.isFile) {
                    size += file.length()
                }
            }
        }
        return size
    }

    private fun readableFileSize(size: Long): String? {
        if (size <= 0) return "0 Bytes"
        val units = arrayOf("Bytes", "kB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())).toString() + " " + units[digitGroups]
    }

    private fun themeSettings() {
        system_default_btn.setOnClickListener {
            val themePreference = ThemePreference(this)
            themePreference.setValue(100)
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    setTheme(R.style.AppTheme)
                } // Night mode is not active, we're using the light theme
                Configuration.UI_MODE_NIGHT_YES -> {
                    setTheme(R.style.AppThemeDark)
                } // Night mode is active, we're using dark theme
            }
            Utils.fadeOutAnim(theme_panel, 300)
            val delayChange = Handler()
            delayChange.postDelayed({
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                SettingsActivity().finish()
                System.exit(0)
                overridePendingTransition(0, 0)
            }, 302)
        }
        light_btn.setOnClickListener {
            val themePreference = ThemePreference(this)
            themePreference.setValue(0)
            setTheme(R.style.AppTheme)
            Utils.fadeOutAnim(theme_panel, 300)

            val delayChange = Handler()
            delayChange.postDelayed({
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                SettingsActivity().finish()
                System.exit(0)
                overridePendingTransition(0, 0)
            }, 302)
        }
        dark_btn.setOnClickListener {
            val themePreference = ThemePreference(this)
            themePreference.setValue(1)
            setTheme(R.style.AppThemeDark)
            Utils.fadeOutAnim(theme_panel, 300)

            val delayChange = Handler()
            delayChange.postDelayed({
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                SettingsActivity().finish()
                System.exit(0)
                overridePendingTransition(0, 0)
            }, 302)
        }
        themes_settings.setOnClickListener {
            Utils.fadeInAnim(theme_panel, 300)
        }
        theme_background.setOnClickListener {
            Utils.fadeOutAnim(theme_panel, 300)
        }
        cancel_btn.setOnClickListener {
            Utils.fadeOutAnim(theme_panel, 300)
        }
    }
}



