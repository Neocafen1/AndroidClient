package com.example.neocafeteae1prototype.view_model.notification_vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import com.example.neocafeteae1prototype.view.root.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {


    fun deleteNotificationsById(id:Int){
        viewModelScope.launch {
            repository.deleteNotifications(id).let {
                if (it is Resource.Success) getNotifications()
            }
        }
    }

    fun getNotifications(){

    }

    val list = mutableListOf<AllModels.Notification>(
        AllModels.Notification("Ваш заказ готов", "Капучинно x2, Латте х2, Коффее х2", "18:13"),
        AllModels.Notification("Ваш заказ оформлен", "Капучинно x2, Латте х2, Коффее х2", "18:13"),
        AllModels.Notification("Вы закрыли счет", "Капучинно x2, Латте х2, Коффее х2", "18:13"),
    )
}