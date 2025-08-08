package com.youssef.inovolatask.domain.data

data class Expense(
    val category: String,
    var amount: Double = 0.0,
    var currency : String = "",
    var convertedAmount: Double = 0.0,
    val date: Long,
    val receiptUri: String?
)