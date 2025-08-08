package com.youssef.inovolatask.domain

import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.domain.repo.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetPaginatedExpensesUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(limit: Int, offset: Int): Flow<List<Expense>> =
        repository.getExpenses(limit, offset)
}