package com.roy.science.activities.tables

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roy.science.R
import com.roy.science.activities.BaseActivity
import com.roy.science.adapter.DictionaryAdapter
import com.roy.science.anim.Anim
import com.roy.science.model.Dictionary
import com.roy.science.model.DictionaryModel
import com.roy.science.preferences.DictionaryPreferences
import com.roy.science.preferences.ThemePreference
import com.roy.science.utils.Utils
import kotlinx.android.synthetic.main.a_dictionary.*
import java.util.Locale

class DictionaryActivity : BaseActivity(), DictionaryAdapter.OnDictionaryClickListener {
    private var dictionaryList = ArrayList<Dictionary>()
    private var mAdapter = DictionaryAdapter(
        dictionaryList = dictionaryList,
        clickListener = this,
        con = this
    )

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
        setContentView(R.layout.a_dictionary) //REMEMBER: Never move any function calls above this

        val recyclerView = findViewById<RecyclerView>(R.id.rcView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val itemse = ArrayList<Dictionary>()
        DictionaryModel.getList(itemse)

        recyclerView()
        clickSearch()
        chipListeners(itemse, recyclerView)
        clearBtn.visibility = View.GONE

        val dictionaryPreference = DictionaryPreferences(this)
        viewDic.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        backBtnD.setOnClickListener {
            this.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun chipListeners(
        list: ArrayList<Dictionary>,
        recyclerView: RecyclerView
    ) {
        chemistryBtn.setOnClickListener {
            updateButtonColor("chemistry_btn")
            val dictionaryPreference = DictionaryPreferences(this)
            dictionaryPreference.setValue("chemistry")
            editIso.setText("test")
            editIso.setText("")
        }
        physicsBtn.setOnClickListener {
            updateButtonColor("physics_btn")
            val dictionaryPreference = DictionaryPreferences(this)
            dictionaryPreference.setValue("physics")
            editIso.setText("test1")
            editIso.setText("")
        }
        mathBtn.setOnClickListener {
            updateButtonColor("math_btn")
            val dictionaryPreference = DictionaryPreferences(this)
            dictionaryPreference.setValue("math")
            editIso.setText("test1")
            editIso.setText("")
        }
        reactionsBtn.setOnClickListener {
            updateButtonColor("reactions_btn")
            val dictionaryPreference = DictionaryPreferences(this)
            dictionaryPreference.setValue("reactions")
            editIso.setText("test1")
            editIso.setText("")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateButtonColor(btn: String) {
        chemistryBtn.background = getDrawable(R.drawable.shape_chip)
        physicsBtn.background = getDrawable(R.drawable.shape_chip)
        mathBtn.background = getDrawable(R.drawable.shape_chip)
        reactionsBtn.background = getDrawable(R.drawable.shape_chip)

        val delay = Handler(Looper.getMainLooper())
        delay.postDelayed({
            val resIDB = resources.getIdentifier(btn, "id", packageName)
            val button = findViewById<Button>(resIDB)
            button?.background = getDrawable(R.drawable.shape_chip_active)
        }, 200)

        clearBtn.visibility = View.VISIBLE
        clearBtn.setOnClickListener {
            val resIDB = resources.getIdentifier(btn, "id", packageName)
            val button = findViewById<Button>(resIDB)
            val dictionaryPreference = DictionaryPreferences(this)
            button?.background = getDrawable(R.drawable.shape_chip)
            dictionaryPreference.setValue("")
            editIso.setText("test1")
            editIso.setText("")
            clearBtn.visibility = View.GONE
        }
    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        rcView.setPadding(
            /* left = */ 0,
            /* top = */ resources.getDimensionPixelSize(R.dimen.title_bar_ph) + top,
            /* right = */ 0,
            /* bottom = */ resources.getDimensionPixelSize(R.dimen.title_bar_ph)
        )
        val params2 = commonTitleBackDic.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar_ph)
        commonTitleBackDic.layoutParams = params2

        val searchEmptyImgPrm = emptySearchBoxDic.layoutParams as ViewGroup.MarginLayoutParams
        searchEmptyImgPrm.topMargin = top + (resources.getDimensionPixelSize(R.dimen.title_bar))
        emptySearchBoxDic.layoutParams = searchEmptyImgPrm
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rcView)
        val dictionaryList = ArrayList<Dictionary>()
        DictionaryModel.getList(dictionaryList)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = DictionaryAdapter(dictionaryList, this, this)
        recyclerView.adapter = adapter
        dictionaryList.sortWith(Comparator { lhs, rhs ->
            if (lhs.heading < rhs.heading) -1 else if (lhs.heading < rhs.heading) 1 else 0
        })

        adapter.notifyDataSetChanged()
        editIso.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString(), dictionaryList, recyclerView)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filter(
        text: String,
        list: ArrayList<Dictionary>,
        recyclerView: RecyclerView
    ) {
        val filteredList: ArrayList<Dictionary> = ArrayList()
        for (item in list) {
            val dictionaryPreference = DictionaryPreferences(this)
            val dictionaryPrefValue1 = dictionaryPreference.getValue()
            if (item.heading.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                if (item.category.lowercase(Locale.ROOT)
                        .contains(dictionaryPrefValue1.lowercase(Locale.ROOT))
                ) {
                    filteredList.add(item)
                }
            }
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (recyclerView.adapter?.itemCount == 0) {
                    Anim.fadeIn(emptySearchBoxDic, 300)
                } else {
                    emptySearchBoxDic.visibility = View.GONE
                }
            }, 10)
            mAdapter.notifyDataSetChanged()
            mAdapter.filterList(filteredList)
            recyclerView.adapter = DictionaryAdapter(
                dictionaryList = filteredList,
                clickListener = this,
                con = this
            )
        }
    }

    private fun clickSearch() {
        searchBtn.setOnClickListener {
            Utils.fadeInAnim(searchBarIso, 150)
            Utils.fadeOutAnim(titleBox, 1)

            editIso.requestFocus()
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editIso, InputMethodManager.SHOW_IMPLICIT)
        }
        closeIsoSearch.setOnClickListener {
            Utils.fadeOutAnim(searchBarIso, 1)

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

    override fun dictionaryClickListener(
        item: Dictionary,
        wiki: TextView,
        url: String,
        position: Int
    ) {
        wiki.setOnClickListener {
            val packageNameString = "com.android.chrome"
            val customTabBuilder = CustomTabsIntent.Builder()

            customTabBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.wikipediaColor))
            customTabBuilder.setSecondaryToolbarColor(
                ContextCompat.getColor(
                    this,
                    R.color.wikipediaColor
                )
            )
            customTabBuilder.setShowTitle(true)

            val customTab = customTabBuilder.build()
            val intent = customTab.intent
            intent.data = Uri.parse(url)

            val packageManager = packageManager
            val resolveInfoList = packageManager.queryIntentActivities(
                customTab.intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            for (resolveInfo in resolveInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                if (TextUtils.equals(packageName, packageNameString))
                    customTab.intent.setPackage(packageNameString)
            }
            customTab.intent.data?.let { it1 -> customTab.launchUrl(this, it1) }
        }
    }
}
