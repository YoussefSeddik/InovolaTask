package com.youssef.inovolatask.di

import com.youssef.inovolatask.data.repository.ExpenseRepositoryImpl
import com.youssef.inovolatask.domain.AddExpenseUseCase
import com.youssef.inovolatask.domain.ConvertToUSDUseCase
import com.youssef.inovolatask.domain.ExportExpensesToCsvUseCase
import com.youssef.inovolatask.domain.FilterExpensesUseCase
import com.youssef.inovolatask.domain.FormatDateUseCase
import com.youssef.inovolatask.domain.GetConversionRatesUseCase
import com.youssef.inovolatask.domain.GetPaginatedExpensesUseCase
import com.youssef.inovolatask.domain.GetTotalExpensesUseCase
import com.youssef.inovolatask.domain.ShareFileUseCase
import com.youssef.inovolatask.domain.ValidateAmountInputUseCase
import com.youssef.inovolatask.domain.repo.ExpenseRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {
    factory<ExpenseRepository> { ExpenseRepositoryImpl(get(), get()) }

    factory { AddExpenseUseCase(get()) }

    factory { GetPaginatedExpensesUseCase(get()) }

    factory { GetTotalExpensesUseCase(get()) }

    factory { GetConversionRatesUseCase(get()) }

    factory { ConvertToUSDUseCase(get()) }

    factory { FormatDateUseCase() }

    factory { FilterExpensesUseCase() }

    factory { ValidateAmountInputUseCase() }

    factory { ExportExpensesToCsvUseCase(androidContext(), get()) }

    factory { ShareFileUseCase(androidContext()) }


}