package com.example.neocafeteae1prototype.view_model.menu_shopping_vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingOrderViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val productList = mutableListOf<AllModels.Product>()
    val isProductListSent = MutableLiveData<Boolean?>()

    fun getProductList(list: MutableList<AllModels.Popular>){ // Меняю на другую модел так как она используется в recycler adapter
        list.map {
            productList.add(AllModels.Product(it.price, it.id, it.title, it.county, getTotalPrice(it.county, it.price)))
        }
    }

    private fun getTotalPrice(county: Int, price: Int): Int { // Возвращает сумму продукта
        return county * price
    }

    fun sendProductList(bonus: Int, inCafe: Boolean) {
        val list = mutableListOf<AllModels.OrderItem>()
        val order = AllModels.Order(bonus)
        productList.map {
            list.add(AllModels.OrderItem(it.productId, it.quantity))
        }

        viewModelScope.launch {
            val finishOrder = AllModels.FinishProduct(order, list)
            if (inCafe){
                repository.sendProductList(finishOrder).let {
                    if (it is Resource.Success) this@ShoppingOrderViewModel.isProductListSent.postValue(it.value)
                }
            }else{
                repository.createOrderTakeOut(finishOrder).let {
                    if (it is Resource.Success) this@ShoppingOrderViewModel.isProductListSent.postValue(it.value)
                }
            }
        }
    }
}