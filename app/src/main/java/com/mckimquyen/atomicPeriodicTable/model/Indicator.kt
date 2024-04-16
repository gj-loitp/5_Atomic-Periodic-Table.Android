package com.mckimquyen.atomicPeriodicTable.model

import androidx.annotation.Keep

@Keep
data class Indicator(
    val name: String,
    val acid: String,
    val acidColor: String,
    val neutral: String,
    val neutralColor: String,
    val alkali: String,
    val alkaliColor: String
)
