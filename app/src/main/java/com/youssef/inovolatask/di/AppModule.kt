package com.youssef.inovolatask.di

import com.youssef.inovolatask.ui.addexpense.AddExpenseViewModel
import com.youssef.inovolatask.ui.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { DashboardViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { AddExpenseViewModel(get(), get(), get()) }
}