package com.mckimquyen.atomicPeriodicTable.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.view.WindowInsets
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mckimquyen.atomicPeriodicTable.BuildConfig
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activities.tables.DictionaryActivity
import com.mckimquyen.atomicPeriodicTable.adapter.ElementAdapter
import com.mckimquyen.atomicPeriodicTable.anim.Anim
import com.mckimquyen.atomicPeriodicTable.extensions.TableExtension
import com.mckimquyen.atomicPeriodicTable.extensions.moreApp
import com.mckimquyen.atomicPeriodicTable.extensions.openBrowserPolicy
import com.mckimquyen.atomicPeriodicTable.extensions.openUrlInBrowser
import com.mckimquyen.atomicPeriodicTable.extensions.rateApp
import com.mckimquyen.atomicPeriodicTable.extensions.shareApp
import com.mckimquyen.atomicPeriodicTable.model.Element
import com.mckimquyen.atomicPeriodicTable.model.ElementModel
import com.mckimquyen.atomicPeriodicTable.preferences.ElementSendAndLoad
import com.mckimquyen.atomicPeriodicTable.preferences.SearchPreferences
import com.mckimquyen.atomicPeriodicTable.preferences.ThemePreference
import com.mckimquyen.atomicPeriodicTable.utils.Utils
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import kotlinx.android.synthetic.main.a_main.*
import kotlinx.android.synthetic.main.search_layout.*
import kotlinx.android.synthetic.main.v_filter_view.*
import kotlinx.android.synthetic.main.v_hover_menu.*
import kotlinx.android.synthetic.main.view_nav_menu_view.*
import org.deejdev.twowaynestedscrollview.TwoWayNestedScrollView
import java.util.Locale

class MainActivity : TableExtension(), ElementAdapter.OnElementClickListener2 {
    private var elementList = ArrayList<Element>()
    private var mAdapter =
        ElementAdapter(elementList = elementList, clickListener = this, con = this)

    private var mScale = 1f
    private lateinit var mScaleDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector

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
        setContentView(R.layout.a_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rvElement)
        recyclerView.layoutManager = LinearLayoutManager(
            /* context = */ this,
            /* orientation = */ RecyclerView.VERTICAL,
            /* reverseLayout = */ false
        )
        val elements = ArrayList<Element>()
        ElementModel.getList(elements)
        val adapter = ElementAdapter(elementList = elements, clickListener = this, con = this)
        recyclerView.adapter = adapter
        editElement.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString(), elements, recyclerView)
            }
        })

        setOnCLickListenerSetups(elements)
        scrollAdapter()
        setupNavListeners()
        onClickNav()
        searchListener()
        slidingLayout.panelState = PanelState.COLLAPSED
        searchFilter(elements, recyclerView)
        mediaListeners()
        hoverListeners(elements)
        initName(elements)
        moreBtn.setOnClickListener { openHover() }
        hoverBackground.setOnClickListener { closeHover() }
        btRandomBtn.setOnClickListener { getRandomItem() }
        view_main.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        val handler = android.os.Handler(Looper.getMainLooper())
        handler.postDelayed({
            initName(elements)
        }, 250)

        gestureDetector = GestureDetector(/* context = */ this, /* listener = */ GestureListener())
        mScaleDetector = ScaleGestureDetector(
            /* context = */ this,
            /* listener = */ object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val scale = 1 - detector.scaleFactor
                    val pScale = mScale
                    mScale += scale
                    mScale += scale
                    if (mScale < 1f)
                        mScale = 1f
                    if (mScale > 12.5f)
                        mScale = 12.5f
                    val scaleAnimation = ScaleAnimation(
                        1f / pScale,
                        1f / mScale,
                        1f / pScale,
                        1f / mScale,
                        detector.focusX,
                        detector.focusY
                    )
                    if (mScale > 1f) {
                        topBar.visibility = View.GONE
                        leftBar.visibility = View.GONE
                        corner.visibility = View.GONE
                    }
                    if (mScale == 1f) {
                        topBar.visibility = View.VISIBLE
                        leftBar.visibility = View.VISIBLE
                        corner.visibility = View.VISIBLE
                    }
                    scaleAnimation.duration = 0
                    scaleAnimation.fillAfter = true
                    val layout = scrollLin as LinearLayout
                    layout.startAnimation(scaleAnimation)
                    return true
                }
            })

        scrollView.viewTreeObserver
            .addOnScrollChangedListener(object : OnScrollChangedListener {
                var y = 0f
                override fun onScrollChanged() {
                    if (scrollView.scrollY > y) {
                        Utils.fadeOutAnim(navBarMain, 150)
                        Utils.fadeOutAnim(moreBtn, 150)
                    } else {
                        Utils.fadeInAnim(navBarMain, 150)
                        Utils.fadeInAnim(moreBtn, 150)
                    }
                    y = scrollView.scrollY.toFloat()
                }
            })

        slidingLayout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}
            override fun onPanelStateChanged(
                panel: View?,
                previousState: PanelState,
                newState: PanelState,
            ) {
                if (slidingLayout.panelState === PanelState.COLLAPSED) {
                    navMenuInclude.visibility = View.GONE
                    Utils.fadeOutAnim(view = navBackground, time = 100)
                }
            }
        })
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        super.dispatchTouchEvent(event)
        mScaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return gestureDetector.onTouchEvent(event)
    }

    private class GestureListener : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return true
        }
    }

    private fun scrollAdapter() {
        scrollView.setOnScrollChangeListener {
                _: TwoWayNestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                _: Int,
                _: Int,
            ->
            leftBar.scrollTo(/* x = */ 0, /* y = */ scrollY)
            topBar.scrollTo(/* x = */ scrollX, /* y = */ 0)
        }
    }

    private fun getRandomItem() {
        val elements = ArrayList<Element>()
        ElementModel.getList(elements)
        val randomNumber = (0..117).random()
        val item = elements[randomNumber]

        val elementSendAndLoad = ElementSendAndLoad(this)
        elementSendAndLoad.setValue(item.element)
        val intent =
            Intent(/* packageContext = */ this, /* cls = */ ElementInfoActivity::class.java)
        startActivity(intent)
    }

    private fun openHover() {
        Utils.fadeInAnimBack(hoverBackground, 200)
        Utils.fadeInAnim(hoverMenuInclude, 300)
    }

    private fun closeHover() {
        Utils.fadeOutAnim(hoverBackground, 200)
        Utils.fadeOutAnim(hoverMenuInclude, 300)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filter(text: String, list: ArrayList<Element>, recyclerView: RecyclerView) {
        val filteredList: ArrayList<Element> = ArrayList()
        for (item in list) {
            if (item.element.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
                Log.v("SSDD2", filteredList.toString())
            }
        }
        val searchPreference = SearchPreferences(this)
        val searchPrefValue = searchPreference.getValue()
        if (searchPrefValue == 2) {
            filteredList.sortWith { lhs, rhs ->
                if (lhs.element < rhs.element) -1 else if (lhs.element < rhs.element) 1 else 0
            }
        }
        mAdapter.filterList(filteredList)
        mAdapter.notifyDataSetChanged()
        val handler = android.os.Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (recyclerView.adapter?.itemCount == 0) {
                Anim.fadeIn(emptySearchBox, 300)
            } else {
                emptySearchBox.visibility = View.GONE
            }
        }, 10)
        recyclerView.adapter = ElementAdapter(
            elementList = filteredList,
            clickListener = this,
            con = this
        )
    }

    override fun elementClickListener2(item: Element, position: Int) {
        val elementSendAndLoad = ElementSendAndLoad(this)
        elementSendAndLoad.setValue(item.element)

        val intent = Intent(this, ElementInfoActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (navBackground.visibility == View.VISIBLE) {
            slidingLayout.panelState = PanelState.COLLAPSED
            Utils.fadeOutAnim(navBackground, 150)
            return
        }
        if (hoverBackground.visibility == View.VISIBLE) {
            Utils.fadeOutAnim(hoverBackground, 150)
            Utils.fadeOutAnim(hoverMenuInclude, 150)
            return
        }
        if (searchMenuInclude.visibility == View.VISIBLE) {
            Utils.fadeInAnim(navBarMain, 150)
            Utils.fadeOutAnim(navBackground, 150)
            Utils.fadeOutAnim(searchMenuInclude, 150)
            Utils.fadeInAnim(moreBtn, 300)
            return
        }
        if (searchMenuInclude.visibility == View.VISIBLE && background.visibility == View.VISIBLE) {
            Utils.fadeOutAnim(background, 150)
            Utils.fadeOutAnim(filterBox, 150)
            Utils.fadeInAnim(moreBtn, 300)
            return
        } else {
            super.onBackPressed()
        }
    }

    private fun searchListener() {
        searchBox.setOnClickListener {
            Utils.fadeInAnim(searchMenuInclude, 300)
            navBarMain.visibility = View.GONE
            Utils.fadeOutAnim(moreBtn, 300)

            editElement.requestFocus()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.show(WindowInsets.Type.ime())
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editElement, InputMethodManager.SHOW_IMPLICIT)
            }
            Utils.fadeOutAnim(filterBox, 150)
            Utils.fadeOutAnim(background, 150)
        }
        closeElementSearch.setOnClickListener {
            Utils.fadeOutAnim(searchMenuInclude, 300)
            Utils.fadeInAnim(moreBtn, 300)
            navBarMain.visibility = View.VISIBLE

            val view = this.currentFocus
            if (view != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    view_main.doOnLayout {
                        window.insetsController?.hide(WindowInsets.Type.ime())
                    }
                } else {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
    }

    private fun mediaListeners() {
        btRate.setOnClickListener {
            rateApp(packageName)
        }
        btMore.setOnClickListener {
            moreApp()
        }
        btShare.setOnClickListener {
            shareApp()
        }
        btPolicy.setOnClickListener {
            openBrowserPolicy()
        }
        btGithub.setOnClickListener {
            if (BuildConfig.DEBUG) {
                openUrlInBrowser("https://github.com/tplloi/Atomic-Periodic-Table.Android/tree/dev")
            } else {
                Toast.makeText(
                    /* context = */ this,
                    /* text = */ "This feature is only available in Debug mode",
                    /* duration = */ Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onClickNav() {
        menuBtn.setOnClickListener {
            navMenuInclude.visibility = View.VISIBLE
            navBackground.visibility = View.VISIBLE
            Utils.fadeInAnimBack(navBackground, 200)
            slidingLayout.panelState = PanelState.EXPANDED
        }
        navBackground.setOnClickListener {
            searchMenuInclude.visibility = View.GONE
            slidingLayout.panelState = PanelState.COLLAPSED
            navBarMain.visibility = View.VISIBLE
            Utils.fadeOutAnim(navBackground, 100)
        }
        solubilityBtn.setOnClickListener {
            val intent = Intent(this, TableActivity::class.java)
            startActivity(intent)
        }
        isotopesBtn.setOnClickListener {
            val intent = Intent(this, IsotopesActivityExperimental::class.java)
            startActivity(intent)
        }
        dictionaryBtn.setOnClickListener {
            val intent = Intent(this, DictionaryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hoverListeners(elements: ArrayList<Element>) {
        hNameBtn.setOnClickListener {
            initName(elements)
        }
        hGroupBtn.setOnClickListener {
            initGroups(elements)
        }
        hElectronegativityBtn.setOnClickListener {
            initElectro(elements)
        }
        atomicWeightBtn.setOnClickListener {
            initWeight(elements)
        }
        boilingBtn.setOnClickListener {
            initBoiling(elements)
        }
        meltingPoint.setOnClickListener {
            initMelting(elements)
        }
        hPhaseBtn.setOnClickListener {
            initPhase(elements)
        }
        hYearBtn.setOnClickListener {
            initYear(elements)
        }
        hFusionBtn.setOnClickListener {
            initHeat(elements)
        }
        hSpecificBtn.setOnClickListener {
            initSpecific(elements)
        }
        hVaporizatonBtn.setOnClickListener {
            initVape(elements)
        }
    }

    private fun setupNavListeners() {
        settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setOnCLickListenerSetups(list: ArrayList<Element>) {
        for (item in list) {
            val name = item.element
            val extBtn = "_btn"
            val eViewBtn = "$name$extBtn"
            val resIDB = resources.getIdentifier(eViewBtn, "id", packageName)

            val btn = findViewById<TextView>(resIDB)
            btn.foreground = ContextCompat.getDrawable(
                /* context = */ this,
                /* id = */ R.drawable.ripple_t_ripple
            )
            btn.isClickable = true
            btn.isFocusable = true
            btn.setOnClickListener {
                val intent = Intent(this, ElementInfoActivity::class.java)
                val elementSend = ElementSendAndLoad(this)
                elementSend.setValue(item.element)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchFilter(list: ArrayList<Element>, recyclerView: RecyclerView) {
        filterBox.visibility = View.GONE
        background.visibility = View.GONE

        btFilterBtn.setOnClickListener {
            Utils.fadeInAnim(filterBox, 150)
            Utils.fadeInAnim(background, 150)
        }
        background.setOnClickListener {
            Utils.fadeOutAnim(filterBox, 150)
            Utils.fadeOutAnim(background, 150)
        }
        elmtNumbBtn2.setOnClickListener {
            editElement.setText("")

            val searchPreference = SearchPreferences(this)
            searchPreference.setValue(0)

            val filtList: ArrayList<Element> = ArrayList()
            for (item in list) {
                filtList.add(item)
            }
            Utils.fadeOutAnim(filterBox, 150)
            Utils.fadeOutAnim(background, 150)
            mAdapter.filterList(filtList)
            mAdapter.notifyDataSetChanged()
            recyclerView.adapter = ElementAdapter(
                elementList = filtList,
                clickListener = this,
                con = this
            )
        }
        electroBtn.setOnClickListener {
            editElement.setText("")

            val searchPreference = SearchPreferences(this)
            searchPreference.setValue(1)

            val filtList: ArrayList<Element> = ArrayList()
            for (item in list) {
                filtList.add(item)
            }
            Utils.fadeOutAnim(filterBox, 150)
            Utils.fadeOutAnim(background, 150)
            mAdapter.filterList(filtList)
            mAdapter.notifyDataSetChanged()
            recyclerView.adapter = ElementAdapter(
                elementList = filtList,
                clickListener = this,
                con = this
            )
        }
        alphabetBtn.setOnClickListener {
            editElement.setText("")

            val searchPreference = SearchPreferences(this)
            searchPreference.setValue(2)

            val filtList: ArrayList<Element> = ArrayList()
            for (item in list) {
                filtList.add(item)
            }
            Utils.fadeOutAnim(filterBox, 150)
            Utils.fadeOutAnim(background, 150)
            filtList.sortWith { lhs, rhs ->
                if (lhs.element < rhs.element) -1 else if (lhs.element < rhs.element) 1 else 0
            }
            mAdapter.filterList(filtList)
            mAdapter.notifyDataSetChanged()
            recyclerView.adapter = ElementAdapter(
                elementList = filtList,
                clickListener = this,
                con = this
            )
        }
    }

    override fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int,
    ) {
        navLin.setPadding(/* left = */ left, /* top = */ 0, /* right = */ right, /* bottom = */ 0)

        val params = commonTitleBackMain.layoutParams as ViewGroup.LayoutParams
        params.height = top + resources.getDimensionPixelSize(R.dimen.title_bar_main)
        commonTitleBackMain.layoutParams = params

        val params2 = navBarMain.layoutParams as ViewGroup.LayoutParams
        params2.height = bottom + resources.getDimensionPixelSize(R.dimen.nav_bar)
        navBarMain.layoutParams = params2

        val params3 = moreBtn.layoutParams as ViewGroup.MarginLayoutParams
        params3.bottomMargin =
            bottom + (resources.getDimensionPixelSize(R.dimen.nav_bar)) / 2 + (resources.getDimensionPixelSize(
                R.dimen.title_bar_elevation
            ))
        moreBtn.layoutParams = params3

        val params4 = commonTitleBackSearch.layoutParams as ViewGroup.LayoutParams
        params4.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackSearch.layoutParams = params4

        rvElement.setPadding(
            /* left = */ 0,
            /* top = */
            resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(R.dimen.margin_space) + top,
            /* right = */
            0,
            /* bottom = */
            resources.getDimensionPixelSize(R.dimen.title_bar)
        )

        val navSide = navContent.layoutParams as ViewGroup.MarginLayoutParams
        navSide.rightMargin = right
        navSide.leftMargin = left
        navContent.layoutParams = navSide

        val barSide = searchBox.layoutParams as ViewGroup.MarginLayoutParams
        barSide.rightMargin = right + resources.getDimensionPixelSize(R.dimen.search_margin_side)
        barSide.leftMargin = left + resources.getDimensionPixelSize(R.dimen.search_margin_side)
        searchBox.layoutParams = barSide

        val leftScrollBar = leftBar.layoutParams as ViewGroup.MarginLayoutParams
        leftScrollBar.topMargin =
            top + resources.getDimensionPixelSize(R.dimen.title_bar_main) + resources.getDimensionPixelSize(
                R.dimen.left_bar
            )
        leftBar.layoutParams = leftScrollBar

        val topScrollBar = topBar.layoutParams as ViewGroup.MarginLayoutParams
        topScrollBar.topMargin = top + resources.getDimensionPixelSize(R.dimen.title_bar_main)
        topBar.layoutParams = topScrollBar

        val numb = leftBar.layoutParams as ViewGroup.LayoutParams
        numb.width = left + resources.getDimensionPixelSize(R.dimen.left_bar)
        leftBar.layoutParams = numb

        val cornerP = corner.layoutParams as ViewGroup.LayoutParams
        cornerP.width = left + resources.getDimensionPixelSize(R.dimen.left_bar)
        corner.layoutParams = cornerP

        val cornerM = corner.layoutParams as ViewGroup.MarginLayoutParams
        cornerM.topMargin = top + resources.getDimensionPixelSize(R.dimen.title_bar_main)
        corner.layoutParams = cornerM

        val params5 = hoverMenuInclude.layoutParams as ViewGroup.MarginLayoutParams
        params5.bottomMargin = bottom
        hoverMenuInclude.layoutParams = params5

        val params6 = scrollView.layoutParams as ViewGroup.MarginLayoutParams
        params6.topMargin = top + resources.getDimensionPixelSize(R.dimen.title_bar_main)
        scrollView.layoutParams = params6

        val params7 = slidingLayout.layoutParams as ViewGroup.LayoutParams
        params7.height = bottom + resources.getDimensionPixelSize(R.dimen.nav_view)
        slidingLayout.layoutParams = params7

        val searchEmptyImgPrm = emptySearchBox.layoutParams as ViewGroup.MarginLayoutParams
        searchEmptyImgPrm.topMargin = top + (resources.getDimensionPixelSize(R.dimen.title_bar))
        emptySearchBox.layoutParams = searchEmptyImgPrm
    }
}
