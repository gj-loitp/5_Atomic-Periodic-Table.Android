package com.mckimquyen.atomicPeriodicTable.act.table

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.act.BaseAct
import com.mckimquyen.atomicPeriodicTable.adt.ElectrodeAdt
import com.mckimquyen.atomicPeriodicTable.anim.Anim
import com.mckimquyen.atomicPeriodicTable.model.Series
import com.mckimquyen.atomicPeriodicTable.model.SeriesModel
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import com.mckimquyen.atomicPeriodicTable.util.Utils
import kotlinx.android.synthetic.main.a_dictionary.searchBtn
import kotlinx.android.synthetic.main.a_dictionary.titleBox
import kotlinx.android.synthetic.main.a_electrode.backBtn
import kotlinx.android.synthetic.main.a_electrode.closeEleSearch
import kotlinx.android.synthetic.main.a_electrode.commonTitleBackElo
import kotlinx.android.synthetic.main.a_electrode.eView
import kotlinx.android.synthetic.main.a_electrode.editEle
import kotlinx.android.synthetic.main.a_electrode.emptySearchBoxEle
import kotlinx.android.synthetic.main.a_electrode.searchBarEle
import kotlinx.android.synthetic.main.a_electrode.viewEle
import java.util.Locale

class ElectrodeAct : BaseAct() {
    private var seriesList = ArrayList<Series>()
    private var mAdapter = ElectrodeAdt(list = seriesList, clickListener = this, context = this)

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
        setContentView(R.layout.a_electrode) //REMEMBER: Never move any function calls above this

        recyclerView()
        clickSearch()

        viewEle.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        backBtn.setOnClickListener {
            this.onBackPressed()
        }
    }

    override fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int,
    ) {
        eView.setPadding(
            /* left = */ 0,
            /* top = */ resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(R.dimen.margin_space) + top,
            /* right = */ 0,
            /* bottom = */ resources.getDimensionPixelSize(R.dimen.title_bar)
        )

        val params2 = commonTitleBackElo.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackElo.layoutParams = params2

        val searchEmptyImgPrm = emptySearchBoxEle.layoutParams as ViewGroup.MarginLayoutParams
        searchEmptyImgPrm.topMargin = top + (resources.getDimensionPixelSize(R.dimen.title_bar))
        emptySearchBoxEle.layoutParams = searchEmptyImgPrm
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.eView)
        val series = ArrayList<Series>()

        SeriesModel.getList(series)
        recyclerView.layoutManager = LinearLayoutManager(
            /* context = */ this,
            /* orientation = */ RecyclerView.VERTICAL,
            /* reverseLayout = */ false
        )
        val adapter = ElectrodeAdt(list = series, clickListener = this, context = this)
        recyclerView.adapter = adapter

        adapter.notifyDataSetChanged()

        editEle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int,
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                filter(text = s.toString(), list = series, recyclerView = recyclerView)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filter(
        text: String,
        list: ArrayList<Series>,
        recyclerView: RecyclerView,
    ) {
        val filteredList: ArrayList<Series> = ArrayList()
        for (item in list) {
            if (item.name.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (recyclerView.adapter?.itemCount == 0) {
                Anim.fadeIn(emptySearchBoxEle, 300)
            } else {
                emptySearchBoxEle.visibility = View.GONE
            }
        }, 10)
        mAdapter.filterList(filteredList)
        mAdapter.notifyDataSetChanged()
        recyclerView.adapter = ElectrodeAdt(filteredList, this, this)
    }

    private fun clickSearch() {
        searchBtn.setOnClickListener {
            Utils.fadeInAnim(searchBarEle, 150)
            Utils.fadeOutAnim(titleBox, 1)

            editEle.requestFocus()
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editEle, InputMethodManager.SHOW_IMPLICIT)
        }
        closeEleSearch.setOnClickListener {
            Utils.fadeOutAnim(searchBarEle, 1)

            val delayClose = Handler(Looper.getMainLooper())
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


}
