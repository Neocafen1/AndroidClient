package com.example.neocafeteae1prototype.view_model.user_shopping_history_vm

import androidx.lifecycle.ViewModel
import com.example.neocafeteae1prototype.data.models.AllModels
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserShoppingViewModel @Inject constructor() : ViewModel() {

    private val listProduct = listOf<AllModels.Product>(
            AllModels.Product("Латте", "120", "3", "360", 0),
            AllModels.Product("Капучино", "120", "3", "360",0),
            AllModels.Product("Капучино", "120", "3", "360",0),
            AllModels.Product("Капучино", "120", "3", "360",0),
            AllModels.Product("Брауни", "120", "3", "360",0),
            AllModels.Product("Брауни", "120", "3", "360",0),
            AllModels.Product("Брауни", "120", "3", "360",0),
            AllModels.Product("Брауни", "120", "3", "360",0),
    )

    val list = mutableListOf<AllModels.Receipt>(
            AllModels.Receipt(listProduct , false, "16:50", "Кулатова 85", "500"),
            AllModels.Receipt(listProduct, true, "18:15", "Кулатова 85", "500"),
            AllModels.Receipt(listProduct, true, "18:15", "Совестская 50", "500"),
            AllModels.Receipt(listProduct,  false, "18:15", "Совестская 50", "500"),
    )

}