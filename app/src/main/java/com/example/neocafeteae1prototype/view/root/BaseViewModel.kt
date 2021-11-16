package com.example.neocafeteae1prototype.view.root

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    abstract var errorLiveData:MutableLiveData<Boolean>

}