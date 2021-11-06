package com.example.neocafeteae1prototype.data.models

import java.io.Serializable

sealed class AllModels: Serializable{

    data class Branches(val filials: List<Filial>, val instagram: String, val facebook: String)

    data class FinishProduct(val productId:Int, val quantity:Int)

    data class Order(val tableId:Int, val bonus:Int)


    data class Table(
        val filialId: Int,
        val id: Int,
        val qrCode: String,
        val status: Boolean
    )

    data class Test(val products:MutableList<AllModels.Popular>):Serializable

    data class Filial(
        val adress: String,
        val description: String,
        val id: Int,
        val image: String,
        val link2Gis: String,
        val phone: String,
        val schedule: List<Schedule>
    ) : AllModels()

    data class Schedule(val day: String, val end: String, val isToday: Boolean, val start: String) : AllModels()

    data class SocialMedia(val instagram:String , val facebook:String) : Serializable

    data class User(val number:Int, val first_name:String, val birthDate:String)

    data class Popular(
        val id:Int,
        val category_name: String,
        val description: String,
        val image: String,
        val isPopular: Boolean,
        val price: Int,
        val title: String,
        var county: Int
    ) : AllModels()

    data class Menu(val name:String, val image: Int): AllModels()

    data class Product(val productName:String, val productPrice:String, val county:String, val totalProductPrice:String, val id:Int):
        AllModels()

    data class Receipt(val list: List<Product>, val afterTenMin:Boolean, val time:String, val street:String, val total:String):
        AllModels()

    data class Notification(val title:String, val message:String, val time:String): AllModels()

    data class JWT_token(val refresh:String, val access:String)

}
