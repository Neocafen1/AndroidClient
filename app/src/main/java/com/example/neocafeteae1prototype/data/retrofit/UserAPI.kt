package com.example.neocafeteae1prototype.data.retrofit

import com.example.neocafeteae1prototype.data.models.AllModels
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

    @GET("user/")
    suspend fun getUserData(): AllModels.User?

    @GET("bonus/")
    suspend fun getBonus():Response<Int>

    @FormUrlEncoded
    @PATCH("userch/")
    suspend fun changeName(@Field("first_name") first_name:String)

    @GET("ncafe/orders/history/")
    suspend fun getOrderHistory():MutableList<AllModels.Receipt>

    @DELETE("ncafe/orders/history/")
    suspend fun deleteUserHistory():Boolean

    @DELETE("ncafe/notifications/")
    suspend fun deleteUserNotifications(@Query("type") type:String, @Query("id")id:Int):Boolean

}