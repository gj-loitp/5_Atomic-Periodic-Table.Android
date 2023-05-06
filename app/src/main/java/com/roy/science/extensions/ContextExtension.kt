package com.roy.science.extensions

import android.content.Context
import com.roy.science.R

fun Context.getStatusBarHeight(): Int {
    return try {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            throw IllegalArgumentException("no res id for status bar height")
        }
    } catch (e: Exception) {
        resources.getDimensionPixelSize(R.dimen.status_bar_height)
    }
}
