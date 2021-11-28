package com.example.neocafeteae1prototype.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

sealed class AllModels : Serializable {

    data class Branches(val filials: List<Filial>, val instagram: String, val facebook: String)

    data class FinishProduct(
        val order: Order,
        val orderItems: MutableList<OrderItem>
    )

    data class Order(val bonus: Int)

    data class OrderItem(
        val productId: Int,
        val quantity: Int
    )

    data class RefreshResponse(
        @SerializedName("access")
        val access: String
    )


    data class Table(
        val id: Int,
        val qrCode: String,
        val user:Int?
    )

    data class Test(val products: MutableList<AllModels.Popular>) : Serializable

    data class Filial(
        val adress: String,
        val description: String,
        val id: Int,
        val image: String,
        val link2Gis: String,
        val phone: String,
        val schedule: List<Schedule>
    ) : AllModels()

    data class Schedule(val day: String, val end: String, val isToday: Boolean, val start: String) :
        AllModels()

    data class SocialMedia(val instagram: String, val facebook: String) : Serializable

    data class User(val number: Int, val first_name: String, val birthDate: String)

    data class Popular(
        val id: Int,
        val category_name: String,
        val description: String,
        val image: String,
        val isPopular: Boolean,
        val price: Int,
        val title: String,
        var county: Int
    ) : AllModels()

    data class Menu(val name: String, val image: Int) : AllModels()

    data class Product(
        val price: Int,
        val productId: Int,
        val productTitle: String,
        val quantity: Int,
        val sum: Int
    ) : AllModels()

    data class Receipt(
        val date: String,
        val details: Details,
        val finalPrice: Int,
        val id: Int,
        val time: String,
        val userId: Int
    ) : AllModels()

    data class Details(
        val bonus: Int,
        val filial: String,
        val orderItems: List<Product>,
        val username: String
    )

    data class Notification(val title: String, val message: String, val time: String) : AllModels()

    data class JWT_token(val refresh: String, val access: String)

}
