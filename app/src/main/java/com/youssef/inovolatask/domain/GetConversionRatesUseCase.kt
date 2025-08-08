package com.youssef.inovolatask.domain

import com.youssef.inovolatask.data.remote.CurrencyResponse
import com.youssef.inovolatask.domain.repo.ExpenseRepository

class GetConversionRatesUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(): CurrencyResponse = repository.getConversionRates()
}