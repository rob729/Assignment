package com.robin729.assignment.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConvRatioAqi {
    private val BASE_URL = "https://blockchain.info/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService: ConvRatioService by lazy {
        retrofit.create(ConvRatioService::class.java)
    }
}