package com.example.neocafeteae1prototype.data.retrofit

import com.example.neocafeteae1prototype.data.models.AllModels
import retrofit2.http.*

interface ShoppingAPI {

    @GET("ncafe/products/county/")
    suspend fun getAllProduct(): MutableList<AllModels.Popular>

    @FormUrlEncoded
    @POST("ncafe/orders/")
    suspend fun sendProductList(
        @Header("Authorization") token: String,
        @Field("order") order: AllModels.Order,
        @Field("orderItems") model: List<AllModels.FinishProduct>
    ): String

}