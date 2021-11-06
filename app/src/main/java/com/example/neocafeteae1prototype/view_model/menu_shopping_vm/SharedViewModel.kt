package com.example.neocafeteae1prototype.view_model.menu_shopping_vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import com.example.neocafeteae1prototype.view.tools.mainLogging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var sortedList = mutableListOf<AllModels.Popular>()
    var bonus = 50
    val list = MutableLiveData<Resource<MutableList<AllModels.Popular>>>()
    val userData = MutableLiveData<AllModels.User>()
    val menuList = mutableListOf<AllModels.Menu>(
        AllModels.Menu("Чай", R.drawable.tea),
        AllModels.Menu("Кофе", R.drawable.coffee),
        AllModels.Menu("Напитки", R.drawable.soda),
        AllModels.Menu("Десерты", R.drawable.desert),
        AllModels.Menu("Выпечка", R.drawable.cake),
    )
    val popularList = mutableListOf<AllModels.Popular>()
    val shoppingList = mutableListOf<AllModels.Popular>()

    init {
        getAllProduct()
    }

    fun getUserInfo(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = async { repository.getUserInfo(token) }.await()
            if (response is Resource.Success) {
                userData.postValue(response.value!!)
            }
        }
    }

    private fun getAllProduct() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = async { repository.getAllProduct() }.await()
            list.postValue(response)
        }
    }

    fun sort(category: String, list: MutableList<AllModels.Popular>): MutableList<AllModels.Popular> {
        val myList = mutableListOf<AllModels.Popular>()

        if (category == "Все") {
            return list
        }else{
            list.forEach {
                if (it.category_name == category) {
                    myList.add(it)
                }
            }
            sortedList = myList
            return sortedList
        }
    }

    fun sortProductForShopping(list: MutableList<AllModels.Popular>) {
        shoppingList.clear()
        viewModelScope.launch {
            list.forEach {
                if (it.county > 0) {
                    shoppingList.add(it)
                }
            }
        }
    }

    fun getPopularProduct(list: MutableList<AllModels.Popular>){
        list.forEach {
            if (it.isPopular){
                popularList.add(it)
            }
        }
    }

    fun getTotalPrice(): Int {
        var totalPrice = 0
        shoppingList.forEach {
            totalPrice += it.price * it.county
        }
        return totalPrice
    }

    fun getBonus(token:String){
        CoroutineScope(Dispatchers.IO).launch {
            val bonusResponse = repository.getBonus(token)
            if (bonusResponse.isSuccessful){
                bonus = bonusResponse.body() ?: 0
            }
        }
    }
}