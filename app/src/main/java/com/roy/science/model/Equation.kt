package com.roy.science.model

import androidx.annotation.Keep

@Keep
data class Equation(
    val equationTitle: String,
    val category: String,
    val equation: Int,
    val description: String
)
