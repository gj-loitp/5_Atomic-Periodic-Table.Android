package com.roy.science.model

import androidx.annotation.Keep

@Keep
data class Ion(
    val name: String,
    val short: String,
    val count: Int
)
