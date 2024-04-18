package com.mckimquyen.atomicPeriodicTable.activitie.table

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.ColorMatrixColorFilter
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
import com.mckimquyen.atomicPeriodicTable.activitie.BaseAct
import com.mckimquyen.atomicPeriodicTable.adt.EquationsAdt
import com.mckimquyen.atomicPeriodicTable.anim.Anim
import com.mckimquyen.atomicPeriodicTable.model.Equation
import com.mckimquyen.atomicPeriodicTable.model.EquationModel
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import com.mckimquyen.atomicPeriodicTable.util.Utils
import kotlinx.android.synthetic.main.a_equations.backBtnEqu
import kotlinx.android.synthetic.main.a_equations.closeEquSearch
import kotlinx.android.synthetic.main.a_equations.commonTitleBackEqu
import kotlinx.android.synthetic.main.a_equations.eInc
import kotlinx.android.synthetic.main.a_equations.editEqu
import kotlinx.android.synthetic.main.a_equations.emptySearchBoxEqu
import kotlinx.android.synthetic.main.a_equations.equRecycler
import kotlinx.android.synthetic.main.a_equations.searchBarEqu
import kotlinx.android.synthetic.main.a_equations.searchBtnEqu
import kotlinx.android.synthetic.main.a_equations.titleBoxEqu
import kotlinx.android.synthetic.main.a_equations.viewEqu
import kotlinx.android.synthetic.main.view_equations_info.eBackBtn
import kotlinx.android.synthetic.main.view_equations_info.eText
import kotlinx.android.synthetic.main.view_equations_info.eTitle
import kotlinx.android.synthetic.main.view_equations_info.lBackgroundE
import java.util.Locale

class EquationsAct : BaseAct(), EquationsAdt.OnEquationClickListener {
    private var equationList = ArrayList<Equation>()
    private var mAdapter = EquationsAdt(list = equationList, clickListener = this, context = this)

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
        setContentView(R.layout.a_equations) //REMEMBER: Never move any function calls above this

        recyclerView()
        clickSearch()
        eBackBtn.setOnClickListener { hideInfoPanel() }
        lBackgroundE.setOnClickListener { hideInfoPanel() }

        viewEqu.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        backBtnEqu.setOnClickListener {
            this.onBackPressed()
        }
    }

    override fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int,
    ) {
        equRecycler.setPadding(
            /* left = */ 0,
            /* top = */
            resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(R.dimen.margin_space) + top,
            /* right = */
            0,
            /* bottom = */
            resources.getDimensionPixelSize(R.dimen.title_bar)
        )

        val params2 = commonTitleBackEqu.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackEqu.layoutParams = params2

        val searchEmptyImgPrm = emptySearchBoxEqu.layoutParams as ViewGroup.MarginLayoutParams
        searchEmptyImgPrm.topMargin = top + (resources.getDimensionPixelSize(R.dimen.title_bar))
        emptySearchBoxEqu.layoutParams = searchEmptyImgPrm
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recyclerView() {
        val equRecycler = findViewById<RecyclerView>(R.id.equRecycler)
        val equation = ArrayList<Equation>()

        EquationModel.getList(equation)
        equRecycler.layoutManager = LinearLayoutManager(
            /* context = */ this,
            /* orientation = */ RecyclerView.VERTICAL,
            /* reverseLayout = */ false
        )
        val adapter = EquationsAdt(list = equation, clickListener = this, context = this)
        equRecycler.adapter = adapter

        equation.sortWith { lhs, rhs ->
            if (lhs.equationTitle < rhs.equationTitle) -1 else if (lhs.equationTitle < rhs.equationTitle) 1 else 0
        }

        adapter.notifyDataSetChanged()

        editEqu.addTextChangedListener(object : TextWatcher {
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
                filter(s.toString(), equation, equRecycler)
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (eInc.visibility == View.VISIBLE) {
            hideInfoPanel()
            return
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filter(text: String, list: ArrayList<Equation>, recyclerView: RecyclerView) {
        val filteredList: ArrayList<Equation> = ArrayList()
        for (item in list) {
            if (item.equationTitle.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (recyclerView.adapter?.itemCount == 0) {
                Anim.fadeIn(emptySearchBoxEqu, 300)
            } else {
                emptySearchBoxEqu.visibility = View.GONE
            }
        }, 10)
        mAdapter.filterList(filteredList)
        mAdapter.notifyDataSetChanged()
        recyclerView.adapter = EquationsAdt(filteredList, this, this)
    }

    private fun clickSearch() {
        searchBtnEqu.setOnClickListener {
            Utils.fadeInAnim(searchBarEqu, 150)
            Utils.fadeOutAnim(titleBoxEqu, 1)

            editEqu.requestFocus()
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editEqu, InputMethodManager.SHOW_IMPLICIT)
        }
        closeEquSearch.setOnClickListener {
            Utils.fadeOutAnim(searchBarEqu, 1)

            val delayClose = Handler(Looper.getMainLooper())
            delayClose.postDelayed({
                Utils.fadeInAnim(titleBoxEqu, 150)
            }, 151)

            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    override fun equationClickListener(item: Equation, position: Int) {
        showInfoPanel(title = item.equation, text = item.description)
    }

    private fun showInfoPanel(title: Int, text: String) {
        Anim.fadeIn(eInc, 150)

        eTitle.setImageResource(title)
        val themePref = ThemePref(this)
        val themePrefValue = themePref.getValue()
        if (themePrefValue == 1) {
            eTitle.colorFilter = ColorMatrixColorFilter(negative)
        }
        if (themePrefValue == 100) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    eTitle.colorFilter = ColorMatrixColorFilter(negative)
                }
            }
        }
        eText.text = text
    }

    private fun hideInfoPanel() {
        Anim.fadeOutAnim(view = eInc, time = 150)
    }

    private val negative = floatArrayOf(
        -1.0f, 0f, 0f, 0f, 255f,
        0f, -1.0f, 0f, 0f, 255f,
        0f, 0f, -1.0f, 0f, 255f,
        0f, 0f, 0f, 1.0f, 0f
    )
}
