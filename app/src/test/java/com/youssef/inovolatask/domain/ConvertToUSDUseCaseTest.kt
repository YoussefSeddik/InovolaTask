package com.youssef.inovolatask.domain

import com.youssef.inovolatask.data.remote.CurrencyResponse
import com.youssef.inovolatask.domain.data.ConvertedCurrency
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ConvertToUSDUseCaseTest {
    private val getRatesUseCase = mockk<GetConversionRatesUseCase>()
    private val useCase = ConvertToUSDUseCase(getRatesUseCase)

    @Test
    fun `valid input should return converted amount`() = runTest {
        coEvery { getRatesUseCase() } returns CurrencyResponse(rates = mapOf("EGP" to 30.0))

        val result = useCase("150 EGP")

        assertEquals(150.0, result.amount)
        assertEquals(5.0, result.convertedAmount)
    }

    @Test
    fun `invalid format returns zeroes`() = runTest {
        val result = useCase("invalid")
        assertEquals(ConvertedCurrency(0.0, "", 0.0), result)
    }
}
