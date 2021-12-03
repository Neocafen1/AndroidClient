package com.example.neocafeteae1prototype.view_model.qr_vm

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
class QRViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val table = MutableLiveData<Resource<AllModels.Table?>>()
    val lockedTable = MutableLiveData<Boolean?>()
    val isUserHaveTable = MutableLiveData<AllModels.QrTable>()

    fun checkIsUserHaveTable() {
        viewModelScope.launch {
            repository.isUserHaveTable().let {
                if (it is Resource.Success) this@QRViewModel.isUserHaveTable.postValue(it.value)
            }
        }
    }

    fun checkTable(table: String) {
        viewModelScope.launch {
            repository.checkTable(table).let {
                this@QRViewModel.table.postValue(it)
            }
        }
    }

    fun lockTable(table: String) {
        viewModelScope.launch {
            repository.lockTable(table).let {
                if (it is Resource.Success) this@QRViewModel.lockedTable.postValue(it.value)
            }
        }
    }
}

