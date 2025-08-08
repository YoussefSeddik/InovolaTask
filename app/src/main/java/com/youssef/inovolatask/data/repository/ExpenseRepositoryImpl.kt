package com.youssef.inovolatask.data.repository

import com.youssef.inovolatask.data.local.ExpenseDao
import com.youssef.inovolatask.data.local.ExpenseEntity
import com.youssef.inovolatask.data.mappers.toDomain
import com.youssef.inovolatask.data.mappers.toEntity
import com.youssef.inovolatask.data.remote.ApiService
import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.domain.repo.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(
    private val dao: ExpenseDao,
    private val api: ApiService
) : ExpenseRepository {
    override suspend fun addExpense(expense: Expense) {
        val entity = expense.toEntity()
        dao.insertExpense(entity)
    }

    override fun getExpenses(limit: Int, offset: Int): Flow<List<Expense>> {
        return dao.getExpenses(limit, offset).map { list -> list.map { it.toDomain() } }
    }

    override fun getExpenses(): Flow<Double?> {
        return dao.getExpenses()
    }

    override suspend fun getConversionRates() = api.getRates()
}