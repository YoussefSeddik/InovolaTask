package com.youssef.inovolatask.viewModels

import com.youssef.inovolatask.domain.AddExpenseUseCase
import com.youssef.inovolatask.domain.ConvertToUSDUseCase
import com.youssef.inovolatask.domain.ValidateAmountInputUseCase
import com.youssef.inovolatask.domain.data.ConvertedCurrency
import com.youssef.inovolatask.domain.data.Expense
import com.youssef.inovolatask.ui.addexpense.AddExpenseViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class AddExpenseViewModelTest {
    private val validator = ValidateAmountInputUseCase()
    private val converter = mockk<ConvertToUSDUseCase>()
    private val saver = mockk<AddExpenseUseCase>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should convert amount`() = runTest {
        coEvery { converter("300 EGP") } returns ConvertedCurrency(300.0, "EGP", 10.0)
        val result = converter.invoke("300 EGP")
        assertEquals(ConvertedCurrency(300.0, "EGP", 10.0), result)
    }

    @Test
    fun `should save expense`() = runTest {
        coEvery { converter("100 EGP") } returns ConvertedCurrency(300.0, "EGP", 10.0)
        val expense = Expense("Food", 100.0, "EGP", 200.0, System.currentTimeMillis(), null)
        val addExpenseViewModel = AddExpenseViewModel(validator, converter, saver)
        addExpenseViewModel.saveExpense("100 EGP", expense)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { saver(any()) }
    }
}
