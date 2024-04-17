package com.mckimquyen.atomicPeriodicTable.model

import androidx.annotation.Keep

@Keep
object IndicatorModel {
    fun getList(indicator: ArrayList<Indicator>) {
        indicator.add(Indicator(
            name = "bromothymol_blue",
            acid = "6.0",
            acidColor = "yellow",
            neutral = "6.0-7.6",
            neutralColor = "green",
            alkali = "7.6",
            alkaliColor = "blue"
        ))
        indicator.add(Indicator(
            name = "methyl_orange",
            acid = "3.1",
            acidColor = "red",
            neutral = "3.1-4.4",
            neutralColor = "orange",
            alkali = "4.4",
            alkaliColor = "yellow"
        ))
        indicator.add(Indicator(
            name = "congo_red",
            acid = "3.0",
            acidColor = "blue",
            neutral = "3.0-5.0",
            neutralColor = "purple",
            alkali = "5.0",
            alkaliColor = "red"
        ))
        indicator.add(Indicator(
            name = "phenolphthalein",
            acid = "8.3",
            acidColor = "colorless",
            neutral = "8.3-10",
            neutralColor = "colorless",
            alkali = "10",
            alkaliColor = "pink"
        ))
    }
}
