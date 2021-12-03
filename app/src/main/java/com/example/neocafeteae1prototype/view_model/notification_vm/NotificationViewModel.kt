package com.example.neocafeteae1prototype.view_model.notification_vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import com.example.neocafeteae1prototype.view.notification.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val repository: MainRepository
): ViewModel() {

    val notificationList = MutableLiveData<MutableList<AllModels.Notification>>()
    val isNotificationsDeleted = MutableLiveData<Boolean>()

    fun deleteNotificationsById(id:Int){
        viewModelScope.launch {
            repository.deleteNotifications(id)
        }
    }

    fun getNotifications(){
        viewModelScope.launch{
            repository.getNotification().let {
                if (it is Resource.Success) this@NotificationViewModel.notificationList.postValue(it.value)
            }
        }
    }

    fun clearAllNotifications(){
        viewModelScope.launch {
            repository.deleteAllNotifications().let {
                if (it is Resource.Success) this@NotificationViewModel.isNotificationsDeleted.postValue(it.value)
            }
        }
    }
}