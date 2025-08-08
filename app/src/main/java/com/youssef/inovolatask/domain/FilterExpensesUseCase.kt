package com.youssef.inovolatask.domain

import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.domain.data.ExpenseFilter
import java.util.*

class FilterExpensesUseCase {
    operator fun invoke(expenses: List<Expense>, filter: ExpenseFilter): List<Expense> {
        val now = Calendar.getInstance()
        return when (filter) {
            ExpenseFilter.THIS_MONTH -> {
                val month = now.get(Calendar.MONTH)
                val year = now.get(Calendar.YEAR)
                expenses.filter {
                    val cal = Calendar.getInstance().apply { timeInMillis = it.date }
                    cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
                }
            }

            ExpenseFilter.LAST_7_DAYS -> {
                val currentMillis = now.timeInMillis
                val weekAgo = now.apply { add(Calendar.DAY_OF_MONTH, -7) }.timeInMillis
                expenses.filter {
                    it.date in weekAgo..currentMillis
                }
            }
        }
    }
}
