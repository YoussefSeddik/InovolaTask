package com.youssef.inovolatask.domain.repo

import com.youssef.inovolatask.data.remote.CurrencyResponse
import com.youssef.inovolatask.domain.data.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun addExpense(expense: Expense)
    fun getExpenses(limit: Int, offset: Int): Flow<List<Expense>>
    fun getExpenses(): Flow<Double?>
    suspend fun getConversionRates(): CurrencyResponse
}