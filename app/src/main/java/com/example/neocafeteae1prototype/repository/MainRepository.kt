package com.example.neocafeteae1prototype.repository

import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.data.models.SafeApiCall
import com.example.neocafeteae1prototype.data.retrofit.*
import com.example.neocafeteae1prototype.view.tools.bearerToken
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val registration: RestApiRegistration,
    private val branchAPI: BranchAPI,
    private val userAPI: UserAPI,
    private val qrApi: QR_API,
    private val shoppingAPI: ShoppingAPI
) : SafeApiCall {

    suspend fun postUserData(
        number: Int,
        password: String,
        name: String,
        birthDate: String
    ): Response<String> {
        return registration.sendUserData(number, password, name, birthDate)
    }

    suspend fun postUserDataWithoutBirthday(
        number: Int,
        password: String,
        name: String
    ): Response<String> {
        return registration.sendUserDataWithoutBirthday(number, password, name)
    }

    suspend fun getJWTtoken(number: Int, uid: String): Response<AllModels.JWT_token> {
        return registration.getJWTtoken(number, uid)
    }

    suspend fun getAllBranches(): Resource<AllModels.Branches> {
        return safeApiCall { branchAPI.getBranches() }
    }

    suspend fun getUserInfo(): Resource<AllModels.User?> {
        return safeApiCall { userAPI.getUserData() }
    }

    suspend fun getBonus(): Response<Int> {
        return userAPI.getBonus()
    }

    suspend fun checkNumber(number: Int): Response<Boolean> {
        return registration.checkUserNumber(number)
    }

    suspend fun changeUserName(name: String) {
        return userAPI.changeName(name)
    }

    suspend fun checkTable(table: String): Resource<AllModels.Table?> {
        return safeApiCall { qrApi.checkTable(table) }
    }

    suspend fun lockTable(table: String): Resource<AllModels.Table?> {
        return safeApiCall { qrApi.lockTable(table) }
    }

    suspend fun getAllProduct(): Resource<MutableList<AllModels.Popular>> {
        return safeApiCall { shoppingAPI.getAllProduct() }
    }

    suspend fun sendProductList(
        order: AllModels.Order,
        productList: List<AllModels.FinishProduct>,
    ) {
        return shoppingAPI.sendProductList(order, productList)
    }

}