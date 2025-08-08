package com.youssef.inovolatask.data.remote

import retrofit2.http.GET

data class CurrencyResponse(val rates: Map<String, Double>)

interface ApiService {
    @GET("latest/USD")
    suspend fun getRates(): CurrencyResponse
}