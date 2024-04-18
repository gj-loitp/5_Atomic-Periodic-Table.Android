package com.mckimquyen.atomicPeriodicTable.activitie.setting

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activitie.BaseAct
import com.mckimquyen.atomicPeriodicTable.pref.ThemePref
import com.mckimquyen.atomicPeriodicTable.util.Utils
import kotlinx.android.synthetic.main.a_submit.background
import kotlinx.android.synthetic.main.a_submit.commonTitleBackSub
import kotlinx.android.synthetic.main.a_submit.commonTitleBackSubColor
import kotlinx.android.synthetic.main.a_submit.dropBtn
import kotlinx.android.synthetic.main.a_submit.dropIssue
import kotlinx.android.synthetic.main.a_submit.iBtn
import kotlinx.android.synthetic.main.a_submit.iContent
import kotlinx.android.synthetic.main.a_submit.iTitle
import kotlinx.android.synthetic.main.a_submit.submitScroll
import kotlinx.android.synthetic.main.a_submit.submitTitle
import kotlinx.android.synthetic.main.a_submit.submitTitleDownstate
import kotlinx.android.synthetic.main.a_submit.viewSub
import kotlinx.android.synthetic.main.a_solubility.backBtn
import kotlinx.android.synthetic.main.view_drop_issue.bug
import kotlinx.android.synthetic.main.view_drop_issue.dataIssue
import kotlinx.android.synthetic.main.view_drop_issue.question

class SubmitAct : BaseAct() {

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
        setContentView(R.layout.a_submit)

        viewSub.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        dropSelector()

        //Title Controller
        commonTitleBackSubColor.visibility = View.INVISIBLE
        submitTitle.visibility = View.INVISIBLE
        commonTitleBackSub.elevation = (resources.getDimension(R.dimen.zero_elevation))
        submitScroll.viewTreeObserver
            .addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
                var y = 200f
                override fun onScrollChanged() {
                    if (submitScroll.scrollY > 150f) {
                        commonTitleBackSubColor.visibility = View.VISIBLE
                        submitTitle.visibility = View.VISIBLE
                        submitTitleDownstate.visibility = View.INVISIBLE
                        commonTitleBackSub.elevation =
                            (resources.getDimension(R.dimen.one_elevation))
                    } else {
                        commonTitleBackSubColor.visibility = View.INVISIBLE
                        submitTitle.visibility = View.INVISIBLE
                        submitTitleDownstate.visibility = View.VISIBLE
                        commonTitleBackSub.elevation =
                            (resources.getDimension(R.dimen.zero_elevation))
                    }
                    y = submitScroll.scrollY.toFloat()
                }
            })

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
        val params = commonTitleBackSub.layoutParams as ViewGroup.LayoutParams
        params.height = top + resources.getDimensionPixelSize(R.dimen.title_bar)
        commonTitleBackSub.layoutParams = params

        val params2 = submitTitleDownstate.layoutParams as ViewGroup.MarginLayoutParams
        params2.topMargin =
            top + resources.getDimensionPixelSize(R.dimen.title_bar) + resources.getDimensionPixelSize(
                R.dimen.header_down_margin
            )
        submitTitleDownstate.layoutParams = params2

    }

    override fun onBackPressed() {
        if (dropIssue.visibility == View.VISIBLE) {
            Utils.fadeOutAnim(background, 150)
            Utils.fadeOutAnim(dropIssue, 150)
            return
        }
        super.onBackPressed()
    }

    private fun dropSelector() {
        var type = "#data_issue"
        buildForm(type)
        dropBtn.setOnClickListener {
            Utils.fadeInAnim(dropIssue, 150)
            Utils.fadeInAnim(background, 150)
        }
        background.setOnClickListener {
            Utils.fadeOutAnim(dropIssue, 150)
            Utils.fadeOutAnim(background, 150)
        }

        dataIssue.setOnClickListener {
            type = "#data_issue"
            Utils.fadeOutAnim(dropIssue, 150)
            Utils.fadeOutAnim(background, 150)
            dropBtn.text = getString(R.string.data_issue)
            buildForm(type)
        }
        bug.setOnClickListener {
            type = "#bug"
            Utils.fadeOutAnim(dropIssue, 150)
            Utils.fadeOutAnim(background, 150)
            dropBtn.text = getString(R.string.bug)
            buildForm(type)
        }
        question.setOnClickListener {
            type = "#question"
            Utils.fadeOutAnim(dropIssue, 150)
            Utils.fadeOutAnim(background, 150)
            dropBtn.text = getString(R.string.question)
            buildForm(type)
        }
    }

    private fun buildForm(type: String) {
        iBtn.setOnClickListener {
            val title = iTitle.text.toString()
            val content = iContent.text.toString()
            val request = Intent(Intent.ACTION_VIEW)
            request.data = Uri.parse(
                Uri.parse("mailto:roy93group@gmail.com?subject=$type $title&body=$content")
                    .toString()
            )
            startActivity(request)
        }
    }

}
