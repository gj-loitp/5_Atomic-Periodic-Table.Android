package com.mckimquyen.atomicPeriodicTable.model

import androidx.annotation.Keep

@Keep
data class Order(
    val orderNumber: Int,
    val orderTitle: String
)
