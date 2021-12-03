package com.example.neocafeteae1prototype.view_model.user_shopping_history_vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import com.example.neocafeteae1prototype.view.tools.mainLogging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserShoppingViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    val orderList = MutableLiveData<MutableList<AllModels.Receipt>>()
    val isProductListSent = MutableLiveData<Boolean>()
    val isUserHaveTable = MutableLiveData<AllModels.QrTable?>()
    val isHistoryDeleted = MutableLiveData<Boolean?>()

    fun deleteUserHistory(){
        viewModelScope.launch {
            repository.deleteUserHistory().let {
                if (it is Resource.Success) this@UserShoppingViewModel.isHistoryDeleted.postValue(it.value)
            }
        }
    }

    // Проверка забронирован ли за юзером стол
    fun checkIsUserHaveTable(){
        viewModelScope.launch {
            repository.isUserHaveTable().let {
                if(it is Resource.Success) this@UserShoppingViewModel.isUserHaveTable.postValue(it.value)
            }
        }
    }

    fun getOrderHistory() {
        viewModelScope.launch {
            repository.orderHistory().let {
                if (it is Resource.Success) this@UserShoppingViewModel.orderList.postValue(it.value)
            }
        }
    }

    fun sendProductList(productList: List<AllModels.Product>, bonus: Int) {
        isProductListSent.postValue(false)
        val list = mutableListOf<AllModels.OrderItem>()
        val order = AllModels.Order(bonus)
        productList.map {
            list.add(AllModels.OrderItem(it.productId, it.quantity))
        }
        viewModelScope.launch {
            val finishOrder = AllModels.FinishProduct(order, list)
            repository.sendProductList(finishOrder).let {
                if (it is Resource.Success) this@UserShoppingViewModel.isProductListSent.postValue(
                    it.value
                )
            }
        }
    }
}