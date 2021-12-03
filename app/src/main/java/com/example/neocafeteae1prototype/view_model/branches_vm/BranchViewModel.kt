package com.example.neocafeteae1prototype.view_model.branches_vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import com.example.neocafeteae1prototype.view.root.BaseViewModel
import com.example.neocafeteae1prototype.view.tools.firebaseLogging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BranchViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val listOfBranches = MutableLiveData<AllModels.Branches>()

    init {
        getAllBranches()
    }

    private fun getAllBranches() {
        viewModelScope.launch {
            repository.getAllBranches().let {
                if(it is Resource.Success) this@BranchViewModel.listOfBranches.postValue(it.value)
            }
        }
    }
}