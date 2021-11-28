package com.example.neocafeteae1prototype.view_model.menu_shopping_vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var sortedList = mutableListOf<AllModels.Popular>() // сортированные данные
    var bonus = 0
    val productList = MutableLiveData<MutableList<AllModels.Popular>>()
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
    val isUserHaveTable = MutableLiveData<Boolean>()

    init {
        getAllProduct()
    }

    fun getUserInfo() { // Получаем иформацию о Юзере
        viewModelScope.launch {
            repository.getUserInfo().let {
                if(it is Resource.Success) this@SharedViewModel.userData.postValue(it.value)
            }
        }
    }

    private fun getAllProduct() { // Получаем все продукты
        viewModelScope.launch {
            repository.getAllProduct().let {
                if(it is Resource.Success) this@SharedViewModel.productList.postValue(it.value)
            }
        }
    }

    // Сортируем продукты по их категории
    fun sort(category: String, list: MutableList<AllModels.Popular>): MutableList<AllModels.Popular> {
        val myList = mutableListOf<AllModels.Popular>()
        return if (category == "Все") {
            list
        }else{
            list.forEach {
                if (it.category_name == category) {
                    myList.add(it)
                }
            }
            sortedList = myList

            sortedList
        }
    }

    // Берет все продукты где кол-во больше, такеи образом делаем корзину
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

    fun getPopularProduct(list: MutableList<AllModels.Popular>){ // возращает список популярных продуктов
        list.forEach {
            if (it.isPopular){
                popularList.add(it)
            }
        }
    }

    // Проверка забронирован ли за юзером стол
    fun checkIsUserHaveTable(){
        viewModelScope.launch {
            repository.isUserHaveTable().let {
                if(it is Resource.Success) this@SharedViewModel.isUserHaveTable.postValue(it.value)
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

    fun getBonus(){
        CoroutineScope(Dispatchers.IO).launch {
            val bonusResponse = repository.getBonus()
            if (bonusResponse.isSuccessful){
                bonus = bonusResponse.body() ?: 0
            }
        }
    }

    fun updateProductList(){
        getAllProduct()
    }
}