package com.youssef.inovolatask.domain

import com.youssef.inovolatask.domain.repo.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetTotalExpensesUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(): Flow<Double?> = repository.getExpenses()
}
