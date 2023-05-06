package com.roy.science.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roy.science.R
import com.roy.science.adapter.IsotopeAdapter
import com.roy.science.animations.Anim
import com.roy.science.model.Element
import com.roy.science.model.ElementModel
import com.roy.science.preferences.ElementSendAndLoad
import com.roy.science.preferences.IsoPreferences
import com.roy.science.preferences.ThemePreference
import com.roy.science.preferences.sendIso
import com.roy.science.utils.ToastUtil
import com.roy.science.utils.Utils
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_isotopes_experimental.backBtn
import kotlinx.android.synthetic.main.activity_isotopes_experimental.backgroundI2
import kotlinx.android.synthetic.main.activity_isotopes_experimental.closeIsoSearch
import kotlinx.android.synthetic.main.activity_isotopes_experimental.common_title_back_iso
import kotlinx.android.synthetic.main.activity_isotopes_experimental.editIso
import kotlinx.android.synthetic.main.activity_isotopes_experimental.emptySearchBoxIso
import kotlinx.android.synthetic.main.activity_isotopes_experimental.filterBackground
import kotlinx.android.synthetic.main.activity_isotopes_experimental.filterBtn2
import kotlinx.android.synthetic.main.activity_isotopes_experimental.isoFilterBox
import kotlinx.android.synthetic.main.activity_isotopes_experimental.panelInfo
import kotlinx.android.synthetic.main.activity_isotopes_experimental.rView
import kotlinx.android.synthetic.main.activity_isotopes_experimental.searchBarIso
import kotlinx.android.synthetic.main.activity_isotopes_experimental.searchBtn
import kotlinx.android.synthetic.main.activity_isotopes_experimental.slidPanel
import kotlinx.android.synthetic.main.activity_isotopes_experimental.titleBox
import kotlinx.android.synthetic.main.activity_isotopes_experimental.view1
import kotlinx.android.synthetic.main.filter_view_iso.isoAlphabetBtn
import kotlinx.android.synthetic.main.filter_view_iso.isoElementNumbBtn
import kotlinx.android.synthetic.main.isotope_panel.frameIso
import kotlinx.android.synthetic.main.isotope_panel.slidingLayoutI
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.Locale

class IsotopesActivityExperimental : BaseActivity(), IsotopeAdapter.OnElementClickListener {
    private var elementList = ArrayList<Element>()
    var mAdapter = IsotopeAdapter(elementList = elementList, clickListener = this, context = this)

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
        setContentView(R.layout.activity_isotopes_experimental) //Don't move down (Needs to be before we call our functions)

        val recyclerView = findViewById<RecyclerView>(R.id.rView)
        slidingLayoutI.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val elements = ArrayList<Element>()
        ElementModel.getList(elements)
        val adapter = IsotopeAdapter(elementList = elements, clickListener = this, context = this)
        recyclerView.adapter = adapter

        editIso.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString(), elements, recyclerView)
            }
        })

        slidingLayoutI.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}
            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState,
                newState: SlidingUpPanelLayout.PanelState
            ) {
                if (slidingLayoutI.panelState === SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    Utils.fadeOutAnim(backgroundI2, 300)
                    Utils.fadeOutAnim(slidPanel, 300)
                }
            }
        })

        backgroundI2.setOnClickListener {
            if (panelInfo.visibility == View.VISIBLE) {
                Utils.fadeOutAnim(panelInfo, 300)
                Utils.fadeOutAnim(backgroundI2, 300)
            } else {
                Utils.fadeOutAnim(slidingLayoutI, 300)
                Utils.fadeOutAnim(backgroundI2, 300)
            }
        }

        view1.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        clickSearch()
        searchFilter(elements, recyclerView)
        sentIsotope()
        backBtn.setOnClickListener {
            this.onBackPressed()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchFilter(list: ArrayList<Element>, recyclerView: RecyclerView) {
        filterBtn2.setOnClickListener {
            Utils.fadeInAnim(isoFilterBox, 150)
            Utils.fadeInAnim(filterBackground, 150)
        }
        filterBackground.setOnClickListener {
            Utils.fadeOutAnim(isoFilterBox, 150)
            Utils.fadeOutAnim(filterBackground, 150)
        }
        isoAlphabetBtn.setOnClickListener {
            val isoPreference = IsoPreferences(this)
            isoPreference.setValue(0)

            val filtList: ArrayList<Element> = ArrayList()
            for (item in list) {
                filtList.add(item)
            }
            Utils.fadeOutAnim(isoFilterBox, 150)
            Utils.fadeOutAnim(filterBackground, 150)
            filtList.sortWith { lhs, rhs ->
                if (lhs.element < rhs.element) -1 else if (lhs.element < rhs.element) 1 else 0
            }
            mAdapter.filterList(filtList)
            mAdapter.notifyDataSetChanged()
            recyclerView.adapter = IsotopeAdapter(
                elementList = filtList,
                clickListener = this,
                context = this
            )
        }
        isoElementNumbBtn.setOnClickListener {
            val isoPreference = IsoPreferences(this)
            isoPreference.setValue(1)

            val filtList: ArrayList<Element> = ArrayList()
            for (item in list) {
                filtList.add(item)
            }
            Utils.fadeOutAnim(isoFilterBox, 150)
            Utils.fadeOutAnim(filterBackground, 150)
            mAdapter.filterList(filtList)
            mAdapter.notifyDataSetChanged()
            recyclerView.adapter = IsotopeAdapter(
                elementList = filtList,
                clickListener = this,
                context = this
            )
        }
    }

    private fun clickSearch() {
        searchBtn.setOnClickListener {
            Utils.fadeInAnim(searchBarIso, 300)
            Utils.fadeOutAnim(titleBox, 300)

            editIso.requestFocus()
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editIso, InputMethodManager.SHOW_IMPLICIT)
        }
        closeIsoSearch.setOnClickListener {
            Utils.fadeOutAnim(searchBarIso, 300)
            Utils.fadeInAnim(titleBox, 300)

            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filter(text: String, list: ArrayList<Element>, recyclerView: RecyclerView) {
        val isoPreference = IsoPreferences(this)
        val isoPrefValue = isoPreference.getValue()
        val filteredList: ArrayList<Element> = ArrayList()
        for (item in list) {
            if (item.element.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
                Log.v("SSDD2", filteredList.toString())
            }
        }
        if (isoPrefValue == 0) {
            filteredList.sortWith { lhs, rhs ->
                if (lhs.element < rhs.element) -1 else if (lhs.element < rhs.element) 1 else 0
            }
        }
        val handler = android.os.Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (recyclerView.adapter?.itemCount == 0) {
                Anim.fadeIn(emptySearchBoxIso, 300)
            } else {
                emptySearchBoxIso.visibility = View.GONE
            }
        }, 10)
        mAdapter.filterList(filteredList)
        mAdapter.notifyDataSetChanged()
        recyclerView.adapter = IsotopeAdapter(
            elementList = filteredList,
            clickListener = this,
            context = this
        )
    }

    override fun elementClickListener(item: Element, position: Int) {
        val elementSendAndLoad = ElementSendAndLoad(this)
        elementSendAndLoad.setValue(item.element)
        drawCard(elementList)

        Utils.fadeInAnimBack(backgroundI2, 300)
        Utils.fadeInAnim(slidPanel, 300)
        slidingLayoutI.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    private fun sentIsotope() {
        val isoSent = sendIso(this)
        if (isoSent.getValue() == "true") {
            drawCard(elementList)
            Utils.fadeInAnimBack(backgroundI2, 300)
            Utils.fadeInAnim(slidPanel, 300)
            slidingLayoutI.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            isoSent.setValue("false")
        }
    }

    override fun onBackPressed() {
        if (backgroundI2.visibility == View.VISIBLE) {
            slidingLayoutI.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            return
        }
        if (filterBackground.visibility == View.VISIBLE) {
            Utils.fadeOutAnim(filterBackground, 150)
            Utils.fadeOutAnim(isoFilterBox, 150)
            return
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun drawCard(list: ArrayList<Element>) {
        ElementModel.getList(list)
        var jsonString: String?
        for (item in list) {
            try {
                val elementSendLoad = ElementSendAndLoad(this)
                val nameVal = elementSendLoad.getValue()
                if (item.element.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } == nameVal?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }) {
                    val ext = ".json"
                    val elementJson = "$nameVal$ext"
                    val inputStream: InputStream = assets.open(elementJson)
                    jsonString = inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(jsonString)
                    val jsonObject: JSONObject = jsonArray.getJSONObject(0)

                    frameIso.removeAllViews()

                    val aLayout = frameIso
                    val inflater = layoutInflater
                    val fLayout: View =
                        inflater.inflate(R.layout.row_iso_panel_title_item, aLayout, false)

                    val iTitle = fLayout.findViewById(R.id.iso_title) as TextView
                    val iExt = " Isotopes"
                    iTitle.text = "${
                        nameVal.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                    }$iExt"

                    aLayout.addView(fLayout)

                    for (i in 1..item.isotopes) {
                        val mainLayout = frameIso
                        val inflater = layoutInflater
                        val myLayout: View =
                            inflater.inflate(R.layout.row_iso_panel_item, mainLayout, false)
                        val name = "iso_"
                        val z = "iso_Z_"
                        val n = "iso_N_"
                        val a = "iso_A_"
                        val half = "iso_half_"
                        val mass = "iso_mass_"
                        val halfText = "Half-Time: "
                        val massText = "Mass: "

                        val isoName = jsonObject.optString("$name$i", "---")
                        val isoZ = jsonObject.optString("$z$i", "---")
                        val isoN = jsonObject.optString("$n$i", "---")
                        val isoA = jsonObject.optString("$a$i", "---")
                        val isoHalf = jsonObject.optString("$half$i", "---")
                        val isoMass = jsonObject.optString("$mass$i", "---")

                        val iName = myLayout.findViewById(R.id.i_name) as TextView
                        val iZ = myLayout.findViewById(R.id.i_z) as TextView
                        val iN = myLayout.findViewById(R.id.i_n) as TextView
                        val iA = myLayout.findViewById(R.id.i_a) as TextView
                        val iHalf = myLayout.findViewById(R.id.i_half) as TextView
                        val iMass = myLayout.findViewById(R.id.i_mass) as TextView

                        iName.text = isoName
                        iZ.text = isoZ
                        iN.text = isoN
                        iA.text = isoA
                        iHalf.text = "$halfText$isoHalf"
                        iMass.text = "$massText$isoMass"

                        mainLayout.addView(myLayout)
                    }
                }
            } catch (e: IOException) { ToastUtil.showToast(this, "Couldn't load Data") }
        }
    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        rView.setPadding(
            0,
            resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(R.dimen.margin_space) + top,
            0,
            resources.getDimensionPixelSize(R.dimen.title_bar)
        )
        val params2 = common_title_back_iso.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        common_title_back_iso.layoutParams = params2

        val params3 = slidingLayoutI.layoutParams as ViewGroup.MarginLayoutParams
        params3.topMargin = top + resources.getDimensionPixelSize(R.dimen.panel_margin)
        slidingLayoutI.layoutParams = params3

        val searchEmptyImgPrm = emptySearchBoxIso.layoutParams as ViewGroup.MarginLayoutParams
        searchEmptyImgPrm.topMargin = top + (resources.getDimensionPixelSize(R.dimen.title_bar))
        emptySearchBoxIso.layoutParams = searchEmptyImgPrm
    }
}
