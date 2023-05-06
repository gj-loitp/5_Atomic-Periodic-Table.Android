package com.roy.science.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat

object TabUtil {
    fun openCustomTab(
        url: String,
        pkgManager: PackageManager,
        context: Context
    ) {
        val pkgName = "com.android.chrome"
        val customTabBuilder = CustomTabsIntent.Builder()

        customTabBuilder.setToolbarColor(
            ContextCompat.getColor(
                context,
                com.roy.science.R.color.colorLightPrimary
            )
        )
        customTabBuilder.setSecondaryToolbarColor(
            ContextCompat.getColor(
                context,
                com.roy.science.R.color.colorLightPrimary
            )
        )
        customTabBuilder.setShowTitle(true)

        val customTab = customTabBuilder.build()
        val intent = customTab.intent
        intent.data = Uri.parse(url)

        val resolveInfoList =
            pkgManager.queryIntentActivities(customTab.intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resolveInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            if (TextUtils.equals(packageName, pkgName))
                customTab.intent.setPackage(pkgName)
        }
        customTab.intent.data?.let { it1 -> customTab.launchUrl(context, it1) }
    }
}
