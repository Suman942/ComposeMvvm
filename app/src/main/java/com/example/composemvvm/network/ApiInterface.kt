package com.example.composemvvm.network

import com.example.composemvvm.models.GetPoetsModel
import com.example.composemvvm.models.Poet
import com.example.composemvvm.models.PoetsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiInterface {

    @GET("v3/b/65905753dc746540188ae3da?meta=false")
    @Headers("X-JSON-Path: poets..name")
    suspend fun getPoets():Response<List<String>>

    @GET("v3/b/65905753dc746540188ae3da?meta=false")
    suspend fun getBooks(@Header("X-JSON-Path") name:String):Response<List<Poet>>
}