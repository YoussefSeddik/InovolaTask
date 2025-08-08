package com.youssef.inovolatask.domain

import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.domain.repo.ExpenseRepository

class AddExpenseUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(expense: Expense) = repository.addExpense(expense)
}



