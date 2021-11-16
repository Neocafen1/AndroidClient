package com.example.neocafeteae1prototype.view_model.user_vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import com.example.neocafeteae1prototype.view.root.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: MainRepository) : BaseViewModel() {

    val userData = MutableLiveData<AllModels.User>()
    var bonus = MutableLiveData<Int>()
    override var errorLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()


    fun changeUserName(name: String) {
        viewModelScope.launch {
            repository.changeUserName(name)
        }
    }


    fun getUserInfo() {
        viewModelScope.launch {
            repository.getUserInfo().let {
                when (it) {
                    is Resource.Failure -> errorLiveData.postValue(true)
                    is Resource.Success -> this@UserViewModel.userData.postValue(it.value)
                }
            }
        }


        viewModelScope.launch {
            repository.getBonus().let {
                if (it.isSuccessful) {
                    bonus.postValue(it.body())
                }
            }
        }
    }

}