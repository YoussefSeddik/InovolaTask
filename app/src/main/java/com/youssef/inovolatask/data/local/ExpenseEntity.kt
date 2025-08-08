package com.youssef.inovolatask.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val amount: Double,
    val convertedAmount: Double,
    val date: Long,
    val receiptUri: String?,
    val currency: String
)