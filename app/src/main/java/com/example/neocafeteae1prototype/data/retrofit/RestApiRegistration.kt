package com.example.neocafeteae1prototype.data.retrofit

import com.example.neocafeteae1prototype.data.models.AllModels
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RestApiRegistration {

    @FormUrlEncoded
    @POST("register/")
    suspend fun sendUserData(
        @Field("number") number: Int,
        @Field("password") password: String,
        @Field("first_name") name: String,
        @Field("birthDate") birthDate:String
    ): Response<String>

    @FormUrlEncoded
    @POST("register/")
    suspend fun sendUserDataWithoutBirthday(
        @Field("number") number: Int,
        @Field("password") password: String,
        @Field("first_name") name: String
    ):Response<String>


    @FormUrlEncoded
    @POST("token/")
    suspend fun getJWTtoken(
        @Field("number") number: Int,
        @Field("password") uid:String
    ):Response<AllModels.JWT_token>

    @FormUrlEncoded
    @POST("authorize/")
    suspend fun checkUserNumber(@Field("number") number: Int):Response<Boolean>

    @FormUrlEncoded
    @POST("refresh/")
    fun updateAccessTokenWithRefresh(@Field("refresh") token: String): Call<AllModels.RefreshResponse>

}