package com.roy.science.model

import androidx.annotation.Keep

@Keep
data class Series(
    val name: String,
    val voltage: Double,
    val charge: String,
    val short: String
)
