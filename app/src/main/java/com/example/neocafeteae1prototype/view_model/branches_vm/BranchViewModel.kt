package com.example.neocafeteae1prototype.view_model.branches_vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import com.example.neocafeteae1prototype.view.root.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BranchViewModel @Inject constructor(private val repository: MainRepository) :
    BaseViewModel() {

    val listOfBranches = MutableLiveData<AllModels.Branches>()
    override var errorLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        getAllBranches()
    }

    private fun getAllBranches() {
        viewModelScope.launch {
            repository.getAllBranches().let {
                when (it) {
                    is Resource.Failure -> errorLiveData.postValue(true)
                    is Resource.Success -> listOfBranches.postValue(it.value!!)
                }
            }
        }
    }
}