package com.roy.science.activities.tables

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import com.roy.science.R
import com.roy.science.activities.BaseActivity
import com.roy.science.model.Element
import com.roy.science.model.ElementModel
import com.roy.science.preferences.ThemePreference
import kotlinx.android.synthetic.main.activity_nuclide.commonTitleBackNuc
import kotlinx.android.synthetic.main.activity_nuclide.ldnPlace
import kotlinx.android.synthetic.main.activity_nuclide.nucBackBtn
import kotlinx.android.synthetic.main.activity_nuclide.scrollNuc
import kotlinx.android.synthetic.main.activity_nuclide.scrollViewNuc
import kotlinx.android.synthetic.main.activity_nuclide.seekBarNuc
import kotlinx.android.synthetic.main.activity_nuclide.viewStub
import kotlinx.android.synthetic.main.activity_nuclide.viewNuc
import kotlinx.android.synthetic.main.stub_nuclide.nuc_view
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class NuclideActivity : BaseActivity() {
    private val elementLists = ArrayList<Element>()
    var mScale = 1f
    lateinit var mScaleDetector: ScaleGestureDetector
    lateinit var gestureDetector: GestureDetector

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
        setContentView(R.layout.activity_nuclide) //REMEMBER: Never move any function calls above this

        viewStub.inflate()

        runOnUiThread {
            ldnPlace.visibility = View.VISIBLE
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            addViews(elementLists)
        }, 100)

        gestureDetector = GestureDetector(this, GestureListener())
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
                    if (mScale > 1f)
                        mScale = 1f
                    val scaleAnimation = ScaleAnimation(
                        /* fromX = */ 1f / pScale,
                        /* toX = */ 1f / mScale,
                        /* fromY = */ 1f / pScale,
                        /* toY = */ 1f / mScale,
                        /* pivotX = */ detector.focusX,
                        /* pivotY = */ detector.focusY
                    )
                    scaleAnimation.duration = 0
                    scaleAnimation.fillAfter = true
                    scaleAnimation.willChangeBounds()
                    scaleAnimation.willChangeTransformationMatrix()
                    val layout = scrollNuc as LinearLayout
                    layout.startAnimation(scaleAnimation)

                    return true
                }
            })

        seekBarNuc.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
                val scaleAnimation = ScaleAnimation(
                    /* fromX = */ 1f / currentValue,
                    /* toX = */ 1f / currentValue,
                    /* fromY = */ 1f / currentValue,
                    /* toY = */ 1f / currentValue
                )
                scaleAnimation.duration = 0
                scaleAnimation.fillAfter = true
                val layout = scrollNuc as LinearLayout
                layout.startAnimation(scaleAnimation)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        viewNuc.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        nucBackBtn.setOnClickListener { this.onBackPressed() }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        super.dispatchTouchEvent(event)
        mScaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return mScaleDetector.onTouchEvent(event)
    }

    private class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return true
        }
    }

    private fun addViews(list: ArrayList<Element>) {
        ElementModel.getList(list)
        val dLayout = nuc_view
        val inflate = layoutInflater
        val mLayout: View = inflate.inflate(R.layout.item_nuclide, dLayout, false)
        val param = RelativeLayout.LayoutParams(
            /* w = */ resources.getDimensionPixelSize(R.dimen.item_nuclide),
            /* h = */ resources.getDimensionPixelSize(R.dimen.item_nuclide)
        )
        param.leftMargin = resources.getDimensionPixelSize(R.dimen.item_nuclide) * 0
        param.topMargin = resources.getDimensionPixelSize(R.dimen.item_nuclide) * 1
        val s = mLayout.findViewById(R.id.nuclide_element) as TextView
        val t = mLayout.findViewById(R.id.nuclide_number) as TextView
        s.text = "n"
        t.text = "1"
        dLayout.addView(mLayout, param)
        ldnPlace.visibility = View.GONE

        for (item in list) {
            var jsonString: String?
            try {
                val hyd = item.element
                val ext = ".json"
                val elementJson: String = "$hyd$ext"
                val inputStream: InputStream = assets.open(elementJson.toString())
                jsonString = inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonString)
                val jsonObject: JSONObject = jsonArray.getJSONObject(0)

                for (i in 1..item.isotopes) {
                    val isoN = "iso_N_"
                    val isoZ = "iso_Z_"
                    val isoHalf = "iso_half_"
                    val decayType = "decay_type_"
                    val number = i.toString()
                    val nJson: String = "$isoN$number"
                    val zJson: String = "$isoZ$number"
                    val halfJson: String = "$isoHalf$number"
                    val decayTypeString = "$decayType$number"
                    val n = jsonObject.optString(nJson, "-")
                    val z = jsonObject.optString(zJson, "-")
                    val half = jsonObject.optString(halfJson, "-")

                    val decayTypeResult = jsonObject.optString(decayTypeString, "default")
                    val mainLayout = nuc_view
                    val inflater = layoutInflater
                    val myLayout: View = inflater.inflate(R.layout.item_nuclide, mainLayout, false)
                    val params = RelativeLayout.LayoutParams(
                        resources.getDimensionPixelSize(R.dimen.item_nuclide),
                        resources.getDimensionPixelSize(R.dimen.item_nuclide)
                    )

                    if (n.isDigitsOnly() && z.isDigitsOnly()) {
                        params.leftMargin =
                            resources.getDimensionPixelSize(R.dimen.item_nuclide) * (z.toInt())
                        params.topMargin =
                            resources.getDimensionPixelSize(R.dimen.item_nuclide) * (n.toInt())
                        val short = myLayout.findViewById(R.id.nuclide_element) as TextView
                        val top = myLayout.findViewById(R.id.nuclide_number) as TextView
                        val frame = myLayout.findViewById(R.id.item_nuclide_frame) as FrameLayout
                        val decay = myLayout.findViewById(R.id.nuclide_decay) as TextView

                        short.text = item.short
                        top.text = (z.toInt() + n.toInt()).toString()
                        decay.text = decayTypeResult
                        if (decayTypeResult == "stable") {
                            frame.background.setTint(Color.argb(255, 42, 50, 61))
                            short.setTextColor(resources.getColor(R.color.colorLightPrimary))
                            top.setTextColor(resources.getColor(R.color.colorLightPrimary))
                        }
                        if (decayTypeResult == "3p") {
                            frame.background.setTint(Color.argb(255, 137, 0, 7))
                            short.setTextColor(resources.getColor(R.color.colorLightPrimary))
                            top.setTextColor(resources.getColor(R.color.colorLightPrimary))
                        }
                        if (decayTypeResult == "2p") {
                            frame.background.setTint(Color.argb(255, 154, 0, 7))
                            short.setTextColor(resources.getColor(R.color.colorLightPrimary))
                            top.setTextColor(resources.getColor(R.color.colorLightPrimary))
                        }
                        if (decayTypeResult == "p") {
                            frame.background.setTint(Color.argb(255, 211, 47, 47))
                            short.setTextColor(resources.getColor(R.color.colorLightPrimary))
                            top.setTextColor(resources.getColor(R.color.colorLightPrimary))
                        }
                        if (decayTypeResult == "B+") {
                            frame.background.setTint(Color.argb(255, 211, 102, 89))
                            short.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                            top.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                        }
                        if (decayTypeResult == "2B-") {
                            frame.background.setTint(Color.argb(255, 3, 155, 229))
                            short.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                            top.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                        }
                        if (decayTypeResult == "B-") {
                            frame.background.setTint(Color.argb(255, 89, 204, 255))
                            short.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                            top.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                        }
                        if (decayTypeResult == "n") {
                            frame.background.setTint(Color.argb(255, 78, 186, 170))
                            short.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                            top.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                        }
                        if (decayTypeResult == "2n") {
                            frame.background.setTint(Color.argb(255, 0, 137, 123))
                            short.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                            top.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                        }
                        if (decayTypeResult == "a") {
                            frame.background.setTint(Color.argb(255, 255, 235, 59))
                            short.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                            top.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                        }
                        if (decayTypeResult == "e- capture") {
                            frame.background.setTint(Color.argb(255, 176, 0, 78))
                            short.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                            top.setTextColor(resources.getColor(R.color.colorDarkPrimary))
                        }
                        mainLayout.addView(myLayout, params)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    override fun onApplySystemInsets(top: Int, bottom: Int, left: Int, right: Int) {
        scrollViewNuc.setPadding(
            /* left = */ 0,
            /* top = */ resources.getDimensionPixelSize(R.dimen.title_bar) + top,
            /* right = */ 0,
            /* bottom = */ resources.getDimensionPixelSize(R.dimen.title_bar)
        )

        val params2 = commonTitleBackNuc.layoutParams as ViewGroup.LayoutParams
        params2.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackNuc.layoutParams = params2
    }

}
