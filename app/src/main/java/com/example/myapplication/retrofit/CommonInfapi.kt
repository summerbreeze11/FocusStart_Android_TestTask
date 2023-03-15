package com.example.myapplication.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface CommonInfapi {
    @GET("{cardnum}")
        suspend fun getCommon(@Path("cardnum") cardnum: String): Card
}