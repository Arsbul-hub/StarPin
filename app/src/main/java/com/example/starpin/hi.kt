package com.example.starpin

import retrofit2.Call
import retrofit2.http.GET

interface hi {
    @GET("posts")
    fun get(): Call<List<test>>


}