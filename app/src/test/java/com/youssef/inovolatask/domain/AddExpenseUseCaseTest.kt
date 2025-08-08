package com.youssef.inovolatask.domain

import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.domain.repo.ExpenseRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddExpenseUseCaseTest {
    private lateinit var repository: ExpenseRepository
    private lateinit var useCase: AddExpenseUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = AddExpenseUseCase(repository)
    }

    @Test
    fun `invoke adds expense`() = runTest {
        val expense = Expense("Food", 50.0, "",50.0, System.currentTimeMillis(), null)
        useCase(expense)
        coVerify { repository.addExpense(expense) }
    }
}
