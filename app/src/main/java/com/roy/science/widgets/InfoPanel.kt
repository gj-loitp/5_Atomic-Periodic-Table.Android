package com.roy.science.widgets

import android.os.Handler
import android.view.View
import android.widget.TextView

object InfoPanel {

    fun showInfoPanel(title: String, view_title: TextView, text: String, view_text: TextView, info_view: View, back_btn: View) {

        info_view.visibility = View.VISIBLE
        info_view.alpha = 0.0f
        info_view.animate().setDuration(150)
        info_view.animate().alpha(1.0f)

        view_title.text = title
        view_text.text = text

        back_btn.setOnClickListener {
            hideInfoPanel(info_view)
        }

    }

    private fun hideInfoPanel(info_view: View) {
        info_view.animate().setDuration(150)
        info_view.animate().alpha(0.0f)

        val handler = Handler()
        handler.postDelayed({
            info_view.visibility = View.GONE
        }, 150+1)
    }

}