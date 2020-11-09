package com.robin729.assignment.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ConvRatioService {

    @GET("tobtc")
    suspend fun getConvRatio(
        @Query("currency") currency: String = "USD",
        @Query("value") value: Int = 1
    ): Response<Float>
}