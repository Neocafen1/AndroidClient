package com.example.neocafeteae1prototype.data.retrofit

import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import retrofit2.http.*

interface ShoppingAPI {

    @GET("ncafe/products/county/")
    suspend fun getAllProduct(): MutableList<AllModels.Popular>

    @POST("ncafe/orders/")
    suspend fun sendProductList(@Body list:AllModels.FinishProduct):Boolean

    @GET("ncafe/orders/history/")
    suspend fun getShoppingHistory(): MutableList<AllModels.Receipt>

    @POST("ncafe/orders/takeout/")
    suspend fun createOrderTakeOut(@Body list:AllModels.FinishProduct):Boolean
}