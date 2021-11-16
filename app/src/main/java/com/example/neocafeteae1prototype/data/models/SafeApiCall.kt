package com.example.neocafeteae1prototype.data.models

import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(apiCall.invoke())
        } catch (throwable: HttpException) {
            Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
        }
    }
}

/*

return try {
    Resource.Success(apiCall.invoke())
} catch (throwable: Throwable) {
    when (throwable) {
        is HttpException -> {
            "Catch".mainLogging()
            Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
        }
        else -> {
            Resource.Failure(true, null, null)
        }
    }
}
}*/
