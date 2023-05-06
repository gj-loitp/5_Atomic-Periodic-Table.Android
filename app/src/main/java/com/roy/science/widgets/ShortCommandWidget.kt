package com.roy.science.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.roy.science.R
import com.roy.science.activities.MainActivity

/**
 * Implementation of App Widget functionality.
 */
class ShortCommandWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        // There may be multiple widgets active, so update all of them
        val widgetIds =
            appWidgetManager.getAppWidgetIds(ComponentName(context, ShortCommandWidget::class.java))
        for (appWidgetId in widgetIds) {

            // Construct the RemoteViews object
            val remoteViews = RemoteViews(context.packageName, R.layout.short_command_widget)

            //Open App on Widget Click
            remoteViews.setOnClickPendingIntent(
                R.id.widget_search_bar,
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )

            //Update Widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val widgetIds = appWidgetManager.getAppWidgetIds((ComponentName(context, Short::class.java)))
    for (appWidgetId in widgetIds) {
        val views = RemoteViews(context.packageName, R.layout.short_command_widget)
    }
}
