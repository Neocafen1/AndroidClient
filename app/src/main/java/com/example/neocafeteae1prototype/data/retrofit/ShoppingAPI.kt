package com.example.neocafeteae1prototype.data.retrofit

import com.example.neocafeteae1prototype.data.models.AllModels
import retrofit2.http.*

interface ShoppingAPI {

    @GET("ncafe/products/county/")
    suspend fun getAllProduct(): MutableList<AllModels.Popular>

    @FormUrlEncoded
    @POST("ncafe/test/")
    suspend fun sendProductList(
        @Field("order") order: AllModels.Order,
        @Field("orderItems") model: List<AllModels.FinishProduct>
    )

    @GET("ncafe/orders/history/")
    suspend fun getShoppingHistory(): MutableList<AllModels.Receipt>
}