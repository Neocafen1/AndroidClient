package com.example.neocafeteae1prototype.data.retrofit

import com.example.neocafeteae1prototype.data.Consts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Client {


    @Singleton
    @Provides
    fun getValidateInterceptor(): Interceptor {
        return ValidateInterceptor()
    }

    @Singleton
    @Provides
    fun getInterceptor():OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(getValidateInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(Consts.READ_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(Consts.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun getRegistrationClient(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl(Consts.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getBranchesApi(retrofit:Retrofit):BranchAPI{
        return retrofit.create(BranchAPI::class.java)
    }

    @Singleton
    @Provides
    fun getRegistrationApi(retrofit: Retrofit):RestApiRegistration{
        return retrofit.create(RestApiRegistration::class.java)
    }

    @Singleton
    @Provides
    fun getUserApi(retrofit: Retrofit):UserAPI{
        return retrofit.create(UserAPI::class.java)
    }

    @Provides
    fun getQR_API(retrofit:Retrofit):QR_API{
        return retrofit.create(QR_API::class.java)
    }

    @Provides
    fun getShoppingAPI(retrofit: Retrofit):ShoppingAPI{
        return retrofit.create(ShoppingAPI::class.java)
    }


}
