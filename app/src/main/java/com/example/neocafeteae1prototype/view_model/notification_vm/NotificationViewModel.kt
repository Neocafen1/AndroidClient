package com.example.neocafeteae1prototype.view_model.notification_vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.view.root.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(): BaseViewModel() {
    val list = mutableListOf<AllModels.Notification>(
        AllModels.Notification("Ваш заказ готов", "Капучинно x2, Латте х2, Коффее х2", "18:13"),
        AllModels.Notification("Ваш заказ оформлен", "Капучинно x2, Латте х2, Коффее х2", "18:13"),
        AllModels.Notification("Вы закрыли счет", "Капучинно x2, Латте х2, Коффее х2", "18:13"),
    )
    override var errorLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
}