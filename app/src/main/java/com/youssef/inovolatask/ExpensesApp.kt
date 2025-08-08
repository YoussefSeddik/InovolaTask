package com.youssef.inovolatask

import android.app.Application
import com.youssef.inovolatask.di.appModule
import com.youssef.inovolatask.di.dataModule
import com.youssef.inovolatask.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class ExpensesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ExpensesApp)
            modules(appModule, dataModule, domainModule)
        }
    }
}