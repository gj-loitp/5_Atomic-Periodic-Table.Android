package com.mckimquyen.atomicPeriodicTable.model

import androidx.annotation.Keep

@Keep
data class Element(
    val element: String,
    val short: String,
    val number: Int,
    val electro: Double,
    val isotopes: Int,
)
