package com.example.neocafeteae1prototype.data.retrofit

import com.example.neocafeteae1prototype.data.models.AllModels
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QR_API {

    @GET("ncafe/tables/{filial1table1}/")
    suspend fun checkTable(@Path(value = "filial1table1", encoded = true) table:String): AllModels.Table

    @POST("ncafe/tables/{filial1table1}/")
    suspend fun lockTable(@Path(value = "filial1table1", encoded = true) table: String): Boolean

    @GET("ncafe/tables/my-tables/")
    suspend fun isUserHaveTable():AllModels.QrTable
}

