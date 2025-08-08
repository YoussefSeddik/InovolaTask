package com.youssef.inovolatask.domain

import com.youssef.inovolatask.domain.data.ConvertedCurrency
import java.util.*

class ConvertToUSDUseCase(
    private val getConversionRatesUseCase: GetConversionRatesUseCase
) {
    suspend operator fun invoke(input: String): ConvertedCurrency {
        val parts = input.trim().split(" ")
        val amount = parts.getOrNull(0)?.toDoubleOrNull() ?: 0.0
        val currency = parts.getOrNull(1)?.uppercase(Locale.getDefault()) ?: ""

        val converted = runCatching {
            val rate = getConversionRatesUseCase().rates[currency] ?: 0.0
            if (rate != 0.0) "%.2f".format(Locale.US, amount / rate).toDouble() else 0.0
        }.getOrDefault(0.0)

        return ConvertedCurrency(amount, currency, converted)
    }
}
