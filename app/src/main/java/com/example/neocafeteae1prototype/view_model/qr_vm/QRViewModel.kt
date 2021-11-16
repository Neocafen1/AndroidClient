package com.example.neocafeteae1prototype.view_model.qr_vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import com.example.neocafeteae1prototype.view.root.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRViewModel @Inject constructor(private val repository: MainRepository) : BaseViewModel() {

    val table = MutableLiveData<AllModels.Table>()
    override var errorLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val lockedTable = MutableLiveData<AllModels.Table>()

    fun checkTable(table:String) {
        viewModelScope.launch {
            repository.checkTable(table).let {
                when (it) {
                    is Resource.Failure -> errorLiveData.postValue(true)
                    is Resource.Success -> this@QRViewModel.table.postValue(it.value)
                }
            }
        }
    }

    fun lockTable(table: String){
        viewModelScope.launch {
            repository.lockTable(table).let {
                when(it){
                    is Resource.Failure -> errorLiveData.postValue(true)
                    is Resource.Success -> this@QRViewModel.lockedTable.postValue(it.value)
                }
            }
        }
    }
}

