package com.example.neocafeteae1prototype.data.retrofit


import com.example.neocafeteae1prototype.application.HiltApplication
import com.example.neocafeteae1prototype.data.local.LocalDatabase
import com.example.neocafeteae1prototype.view.tools.bearerToken
import kotlinx.coroutines.runBlocking
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class ValidateInterceptor : Interceptor {

    private val localDatabase: LocalDatabase by lazy { LocalDatabase(HiltApplication.getContext()!!) }


    companion object {
        const val UNAUTHORIZED_CODE = 401
    }

    private lateinit var originalRequest: Request
    private lateinit var authenticationRequest: Request

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            originalRequest = chain.request()
            authenticationRequest = chain.request()
            if (localDatabase.fetchAccessToken() != null) {
                authenticationRequest = originalRequest.newBuilder()
                    .addHeader(
                        "Authorization",
                        localDatabase.fetchAccessToken()!!.bearerToken()
                    )
                    .build()
            }

            val initialResponse = chain.proceed(authenticationRequest)

            when {
                initialResponse.code == UNAUTHORIZED_CODE -> {
                    val responseNewTokenLoginModel = runBlocking {
                        localDatabase.fetchRefreshToken()?.let {
                            retrofitAuth().create(RestApiRegistration::class.java).updateAccessTokenWithRefresh(
                                it
                            ).execute()
                        }
                    }

                    return when {
                        else -> {
                            if (responseNewTokenLoginModel != null) {
                                responseNewTokenLoginModel.body()?.access?.let {
                                    localDatabase.saveAccessToken(it)
                                }
                            }
                            val newAuthenticationRequest = originalRequest.newBuilder().addHeader(
                                "Authorization",
                                responseNewTokenLoginModel?.body()?.access!!.bearerToken()
                            ).build()
                            chain.proceed(newAuthenticationRequest)
                        }
                    }
                }
                else -> return initialResponse
            }
        }
    }

    private fun retrofitAuth(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://neocafe1.herokuapp.com/")
            .client(createRefreshAuth())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createRefreshAuth(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        okHttpClientBuilder.dispatcher(dispatcher)
        return okHttpClientBuilder.build()
    }
}




