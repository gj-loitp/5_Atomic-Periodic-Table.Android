package com.roy.science.activities.tables

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roy.science.R
import com.roy.science.activities.BaseActivity
import com.roy.science.adapter.IonAdapter
import com.roy.science.animations.Anim
import com.roy.science.model.Ion
import com.roy.science.model.IonModel
import com.roy.science.preferences.ThemePreference
import com.roy.science.utils.Utils
import kotlinx.android.synthetic.main.activity_dictionary.titleBox
import kotlinx.android.synthetic.main.activity_ion.*
import kotlinx.android.synthetic.main.ion_details.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class IonActivity : BaseActivity(), IonAdapter.OnIonClickListener {
    private var ionList = ArrayList<Ion>()
    var mAdapter = IonAdapter(ionList, this, this)

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
        setContentView(R.layout.activity_ion) //REMEMBER: Never move any function calls above this

        recyclerView()
        clickSearch()
        view_ion.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        detail_background_ion.setOnClickListener { Utils.fadeOutAnim(ion_detail, 300) }
        back_btn_ion.setOnClickListener { this.onBackPressed() }
    }

    override fun ionClickListener(item: Ion, position: Int) {
        if (item.count > 1) {
            Utils.fadeInAnim(ion_detail, 300)
            ion_detail_title.text = ((item.name).capitalize() + " " + "ionization")
            var jsonString : String? = null
            val ext = ".json"
            val element = item.name
            val ElementJson: String? = "$element$ext"
            val inputStream: InputStream = assets.open(ElementJson.toString())
            jsonString = inputStream.bufferedReader().use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val jsonObject: JSONObject = jsonArray.getJSONObject(0)
            for (i in 1..item.count) {
                val text = "element_ionization_energy"
                val add = i.toString()
                val final = (text + add)
                val ionization = jsonObject.optString(final, "---")
                val extText = i.toString()
                val name = "ion_text_"
                val eView = "$name$extText"
                val iText = findViewById<TextView>(resources.getIdentifier(eView, "id", packageName))
                val dot ="."
                val space = " "
                iText.text = "$i$dot$space$ionization"
                iText.visibility = View.VISIBLE
            }
            for (i in (item.count+1)..30) {
                val extText = i.toString()
                val name = ("ion_text_")
                val eView = "$name$extText"
                val iText = findViewById<TextView>(resources.getIdentifier(eView, "id", packageName))
                iText.visibility = View.GONE
            }
        }
    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        ion_view.setPadding(0, resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(R.dimen.margin_space) + top, 0, resources.getDimensionPixelSize(R.dimen.title_bar))
        val params2 = common_title_back_ion.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        common_title_back_ion.layoutParams = params2

        val searchEmptyImgPrm = empty_search_box_ion.layoutParams as ViewGroup.MarginLayoutParams
        searchEmptyImgPrm.topMargin = top + (resources.getDimensionPixelSize(R.dimen.title_bar))
        empty_search_box_ion.layoutParams = searchEmptyImgPrm
    }

    private fun recyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.ion_view)
        val ionList = ArrayList<Ion>()
        IonModel.getList(ionList)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = IonAdapter(ionList, this, this)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        edit_ion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){}
            override fun afterTextChanged(s: Editable) { filter(s.toString(), ionList, recyclerView) }
        })
    }

    private fun filter(text: String, list: ArrayList<Ion>, recyclerView: RecyclerView) {
        val filteredList: ArrayList<Ion> = ArrayList()
        for (item in list) { if (item.name.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) { filteredList.add(item) } }
        val handler = android.os.Handler()
        handler.postDelayed({
            if (recyclerView.adapter!!.itemCount == 0) {
                Anim.fadeIn(empty_search_box_ion, 300)
            }
            else {
                empty_search_box_ion.visibility = View.GONE
            }
        }, 10)
        mAdapter.filterList(filteredList)
        mAdapter.notifyDataSetChanged()
        recyclerView.adapter = IonAdapter(filteredList, this, this)
    }

    private fun clickSearch() {
        search_btn_ion.setOnClickListener {
            Utils.fadeInAnim(search_bar_ion, 150)
            Utils.fadeOutAnim(titleBox, 1)
            edit_ion.requestFocus()
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edit_ion, InputMethodManager.SHOW_IMPLICIT)
        }
        close_ele_search_ion.setOnClickListener {
            Utils.fadeOutAnim(search_bar_ion, 1)
            val delayClose = Handler()
            delayClose.postDelayed({
                Utils.fadeInAnim(titleBox, 150)
            }, 151)

            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    override fun onBackPressed() {
        if (ion_detail.visibility == View.VISIBLE) {
            Utils.fadeOutAnim(ion_detail, 300)
            return
        } else { super.onBackPressed() }
    }

}



