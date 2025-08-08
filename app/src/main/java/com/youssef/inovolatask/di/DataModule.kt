package com.youssef.inovolatask.di

import android.app.Application
import androidx.room.Room
import com.youssef.inovolatask.data.local.ExpenseDatabase
import com.youssef.inovolatask.data.remote.ApiService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {
    single {
        Room.databaseBuilder(get<Application>(), ExpenseDatabase::class.java, "expenses_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<ExpenseDatabase>().expenseDao() }

    single {
        Retrofit.Builder()
            .baseUrl("https://open.er-api.com/v6/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}