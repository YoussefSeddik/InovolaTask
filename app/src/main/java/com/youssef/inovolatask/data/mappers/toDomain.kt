package com.youssef.inovolatask.data.mappers

import com.youssef.inovolatask.data.local.ExpenseEntity
import com.youssef.inovolatask.domain.data.Expense
import java.text.SimpleDateFormat
import java.util.*

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        category = category,
        amount = amount,
        convertedAmount = convertedAmount,
        date = date,
        currency = currency,
        receiptUri = receiptUri
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        category = category,
        amount = amount,
        convertedAmount = convertedAmount,
        date = date,
        receiptUri = receiptUri,
        currency = currency
    )
}
