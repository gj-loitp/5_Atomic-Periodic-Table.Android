package com.mckimquyen.atomicPeriodicTable.act

import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity

abstract class BaseAct : AppCompatActivity(), View.OnApplyWindowInsetsListener {
    companion object {
        private const val TAG = "BaseActivity"
    }

    private var systemUiConfigured = false

    override fun onStart() {
        super.onStart()
        val content = findViewById<View>(android.R.id.content)
        content.setOnApplyWindowInsetsListener(this)

        if (!systemUiConfigured) {
            systemUiConfigured = true
        }
    }

    open fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int,
    ) = Unit

    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
//        Pasteur.info(TAG, "height: ${insets.systemWindowInsetBottom}")
        onApplySystemInsets(
            insets.systemWindowInsetTop,
            insets.systemWindowInsetBottom,
            insets.systemWindowInsetLeft,
            insets.systemWindowInsetRight
        )
        return insets.consumeSystemWindowInsets()
    }
}
