package com.mckimquyen.atomicPeriodicTable.activitie

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activitie.table.ElectrodeActivity
import com.mckimquyen.atomicPeriodicTable.activitie.table.EquationsActivity
import com.mckimquyen.atomicPeriodicTable.activitie.table.IonActivity
import com.mckimquyen.atomicPeriodicTable.activitie.table.NuclideActivity
import com.mckimquyen.atomicPeriodicTable.activitie.table.PHActivity
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import kotlinx.android.synthetic.main.a_solubility.backBtn
import kotlinx.android.synthetic.main.a_submit.viewSub
import kotlinx.android.synthetic.main.a_tables.commonTitleBackTab
import kotlinx.android.synthetic.main.a_tables.commonTitleTableColor
import kotlinx.android.synthetic.main.a_tables.eleButton
import kotlinx.android.synthetic.main.a_tables.eleTable
import kotlinx.android.synthetic.main.a_tables.equButton
import kotlinx.android.synthetic.main.a_tables.equTable
import kotlinx.android.synthetic.main.a_tables.ionButton
import kotlinx.android.synthetic.main.a_tables.ionTable
import kotlinx.android.synthetic.main.a_tables.nucButton
import kotlinx.android.synthetic.main.a_tables.nucTable
import kotlinx.android.synthetic.main.a_tables.phButton
import kotlinx.android.synthetic.main.a_tables.phTable
import kotlinx.android.synthetic.main.a_tables.solButton
import kotlinx.android.synthetic.main.a_tables.solTable
import kotlinx.android.synthetic.main.a_tables.tableScroll
import kotlinx.android.synthetic.main.a_tables.tablesTitle
import kotlinx.android.synthetic.main.a_tables.tablesTitleDownstate

class TableAct : BaseActivity() {

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
        setContentView(R.layout.a_tables)

        viewSub.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        //Title Controller
        commonTitleTableColor.visibility = View.INVISIBLE
        tablesTitle.visibility = View.INVISIBLE
        commonTitleBackTab.elevation = (resources.getDimension(R.dimen.zero_elevation))
        tableScroll.viewTreeObserver
            .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                var y = 300f
                override fun onScrollChanged() {
                    if (tableScroll.scrollY > 150) {
                        commonTitleTableColor.visibility = View.VISIBLE
                        tablesTitle.visibility = View.VISIBLE
                        tablesTitleDownstate.visibility = View.INVISIBLE
                        commonTitleBackTab.elevation =
                            (resources.getDimension(R.dimen.one_elevation))
                    } else {
                        commonTitleTableColor.visibility = View.INVISIBLE
                        tablesTitle.visibility = View.INVISIBLE
                        tablesTitleDownstate.visibility = View.VISIBLE
                        commonTitleBackTab.elevation =
                            (resources.getDimension(R.dimen.zero_elevation))
                    }
                    y = tableScroll.scrollY.toFloat()
                }
            })

        tableListeners()

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
        val params = commonTitleBackTab.layoutParams as ViewGroup.LayoutParams
        params.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackTab.layoutParams = params

        val params2 = tablesTitleDownstate.layoutParams as ViewGroup.MarginLayoutParams
        params2.topMargin =
            top + resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(
                R.dimen.header_down_margin
            )
        tablesTitleDownstate.layoutParams = params2

    }

    private fun tableListeners() {
        solTable.setOnClickListener {
            val intent = Intent(this, SolubilityAct::class.java)
            startActivity(intent)
        }
        solButton.setOnClickListener {
            val intent = Intent(this, SolubilityAct::class.java)
            startActivity(intent)
        }
        eleTable.setOnClickListener {
            val intent = Intent(this, ElectrodeActivity::class.java)
            startActivity(intent)
        }
        eleButton.setOnClickListener {
            val intent = Intent(this, ElectrodeActivity::class.java)
            startActivity(intent)
        }
        equTable.setOnClickListener {
            val intent = Intent(this, EquationsActivity::class.java)
            startActivity(intent)
        }
        equButton.setOnClickListener {
            val intent = Intent(this, EquationsActivity::class.java)
            startActivity(intent)
        }
        ionTable.setOnClickListener {
            val intent = Intent(this, IonActivity::class.java)
            startActivity(intent)
        }
        ionButton.setOnClickListener {
            val intent = Intent(this, IonActivity::class.java)
            startActivity(intent)
        }
        nucTable.setOnClickListener {
            val intent = Intent(this, NuclideActivity::class.java)
            startActivity(intent)
        }
        nucButton.setOnClickListener {
            val intent = Intent(this, NuclideActivity::class.java)
            startActivity(intent)
        }
        phTable.setOnClickListener {
            val intent = Intent(this, PHActivity::class.java)
            startActivity(intent)
        }
        phButton.setOnClickListener {
            val intent = Intent(this, PHActivity::class.java)
            startActivity(intent)
        }
    }

}
