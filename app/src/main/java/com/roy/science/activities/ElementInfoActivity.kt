package com.roy.science.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import com.roy.science.R
import com.roy.science.activities.settings.FavoritePageActivity
import com.roy.science.activities.settings.SubmitActivity
import com.roy.science.extensions.InfoExtension
import com.roy.science.model.Element
import com.roy.science.model.ElementModel
import com.roy.science.preferences.ElementSendAndLoad
import com.roy.science.preferences.ThemePreference
import com.roy.science.preferences.OfflinePreference
import com.roy.science.utils.Utils
import kotlinx.android.synthetic.main.activity_element_info.backBtn
import kotlinx.android.synthetic.main.activity_element_info.commonTitleBack
import kotlinx.android.synthetic.main.activity_element_info.detailEmission
import kotlinx.android.synthetic.main.activity_element_info.detailEmissionBackground
import kotlinx.android.synthetic.main.activity_element_info.frame
import kotlinx.android.synthetic.main.activity_element_info.iBtn
import kotlinx.android.synthetic.main.activity_element_info.nextBtn
import kotlinx.android.synthetic.main.activity_element_info.offlineSpace
import kotlinx.android.synthetic.main.activity_element_info.overviewInc
import kotlinx.android.synthetic.main.activity_element_info.previousBtn
import kotlinx.android.synthetic.main.activity_element_info.propertiesInc
import kotlinx.android.synthetic.main.activity_element_info.scr_view
import kotlinx.android.synthetic.main.activity_element_info.shell
import kotlinx.android.synthetic.main.activity_element_info.shellBackground
import kotlinx.android.synthetic.main.activity_element_info.view
import kotlinx.android.synthetic.main.d_properties.electronView
import kotlinx.android.synthetic.main.d_properties.spImg
import kotlinx.android.synthetic.main.d_properties.spOffline
import kotlinx.android.synthetic.main.detail_emission.closeEmissionBtn
import kotlinx.android.synthetic.main.favorite_bar.editFavBtn
import kotlinx.android.synthetic.main.shell_view.closeShellBtn
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class ElementInfoActivity : InfoExtension() {

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
        val elementSendAndLoadPreference = ElementSendAndLoad(this)
        var elementSendAndLoadValue = elementSendAndLoadPreference.getValue()
        setContentView(R.layout.activity_element_info)
        Utils.fadeInAnim(scr_view, 300)
        readJson()
        shell.visibility = View.GONE
        detailEmission.visibility = View.GONE
        detailViews()
        offlineCheck()
        nextPrev()
        favoriteBarSetup()
        elementAnim(overviewInc, propertiesInc)
        view.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        backBtn.setOnClickListener {
            super.onBackPressed()
        }
        editFavBtn.setOnClickListener {
            val intent = Intent(this, FavoritePageActivity::class.java)
            startActivity(intent)
        }
        iBtn.setOnClickListener {
            val intent = Intent(this, SubmitActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (shellBackground.visibility == View.VISIBLE) {
            Utils.fadeOutAnim(shell, 300)
            Utils.fadeOutAnim(shellBackground, 300)
            return
        }
        if (detailEmission.visibility == View.VISIBLE) {
            Utils.fadeOutAnim(detailEmission, 300)
            Utils.fadeOutAnim(detailEmissionBackground, 300)
            return
        } else {
            super.onBackPressed()
        }
    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        val params = frame.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        frame.layoutParams = params

        val paramsO = offlineSpace.layoutParams as ViewGroup.MarginLayoutParams
        paramsO.topMargin += top
        offlineSpace.layoutParams = paramsO

        val params2 = commonTitleBack.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBack.layoutParams = params2
    }

    @SuppressLint("SetTextI18n")
    private fun offlineCheck() {
        val offlinePreferences = OfflinePreference(this)
        val offlinePrefValue = offlinePreferences.getValue()

        if (offlinePrefValue == 1) {
            frame.visibility = View.GONE
            offlineSpace.visibility = View.VISIBLE
            spImg.visibility = View.GONE
            spOffline.visibility = View.VISIBLE
            spOffline.text = "Go online for emission lines"
        } else {
            frame.visibility = View.VISIBLE
            offlineSpace.visibility = View.GONE
            spImg.visibility = View.VISIBLE
            spOffline.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteBarSetup()
    }

    private fun detailViews() {
        electronView.setOnClickListener {
            Utils.fadeInAnim(shell, 300)
            Utils.fadeInAnim(shellBackground, 300)
        }
        closeShellBtn.setOnClickListener {
            Utils.fadeOutAnim(shell, 300)
            Utils.fadeOutAnim(shellBackground, 300)
        }
        shellBackground.setOnClickListener {
            Utils.fadeOutAnim(shell, 300)
            Utils.fadeOutAnim(shellBackground, 300)
        }
        spImg.setOnClickListener {
            Utils.fadeInAnim(detailEmission, 300)
            Utils.fadeInAnim(detailEmissionBackground, 300)
        }
        closeEmissionBtn.setOnClickListener {
            Utils.fadeOutAnim(detailEmission, 300)
            Utils.fadeOutAnim(detailEmissionBackground, 300)
        }
        detailEmissionBackground.setOnClickListener {
            Utils.fadeOutAnim(detailEmission, 300)
            Utils.fadeOutAnim(detailEmissionBackground, 300)
        }
    }

    private fun elementAnim(view: View, view2: View) {
        view.alpha = 0.0f
        view.animate().duration = 150
        view.animate().alpha(1.0f)
        val delay = Handler(Looper.getMainLooper())
        delay.postDelayed({
            view2.alpha = 0.0f
            view2.animate().duration = 150
            view2.animate().alpha(1.0f)
        }, 150)
    }

    private fun nextPrev() {
        nextBtn.setOnClickListener {
            val jsonString: String?
            try {
                val elementSendAndLoadPreference = ElementSendAndLoad(this)
                val elementSendAndLoadValue = elementSendAndLoadPreference.getValue()
                val ext = ".json"
                val elementJson = "$elementSendAndLoadValue$ext"
                val inputStream: InputStream = assets.open(elementJson)
                jsonString = inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonString)
                val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                val currentNumb = jsonObject.optString("element_atomic_number", "---")
                val elements = ArrayList<Element>()
                ElementModel.getList(elements)
                val item = elements[currentNumb.toInt()]
                val elementSendAndLoad = ElementSendAndLoad(this)
                elementSendAndLoad.setValue(item.element)
                readJson()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        previousBtn.setOnClickListener {
            val jsonString: String?
            try {
                val elementSendAndLoadPreference = ElementSendAndLoad(this)
                val elementSendAndLoadValue = elementSendAndLoadPreference.getValue()
                val ext = ".json"
                val elementJson = "$elementSendAndLoadValue$ext"
                val inputStream: InputStream = assets.open(elementJson)
                jsonString = inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonString)
                val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                val currentNumb = jsonObject.optString("element_atomic_number", "---")
                val elements = ArrayList<Element>()
                ElementModel.getList(elements)
                val item = elements[currentNumb.toInt() - 2]
                val elementSendAndLoad = ElementSendAndLoad(this)
                elementSendAndLoad.setValue(item.element)
                readJson()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
