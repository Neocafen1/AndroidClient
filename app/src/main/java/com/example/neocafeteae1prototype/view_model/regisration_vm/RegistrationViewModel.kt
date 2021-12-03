package com.example.neocafeteae1prototype.view_model.regisration_vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.repository.MainRepository
import com.example.neocafeteae1prototype.view.tools.logging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: MainRepository):ViewModel() {

    var userCreated = MutableLiveData<Boolean>()
    var tokens = MutableLiveData<AllModels.JWT_token>()
    var isNumberLocateInDB = MutableLiveData<Boolean?>()
    val isFcmSaved = MutableLiveData<Boolean>()
    private lateinit var request:Response<String>


    fun sendUserData(number: Int, uid: String, name: String, birthDay: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (birthDay!=null){
                request = repository.postUserData(AllModels.UserData(number, uid, name, birthDay))
            }else{
                request = repository.postUserDataWithoutBirthday(number,uid,name)
            }

            if (request.isSuccessful) {
                if (request.body().equals("User is created")) {
                    userCreated.postValue(true)
                }
            }
        }
    }

    fun checkNumber(number: Int) {
        viewModelScope.launch {
            repository.checkNumber(number).let{
                if(it.isSuccessful){
                    isNumberLocateInDB.postValue(it.body())
                }
            }
        }
    }

    fun saveFCM(model:AllModels.FCM_token){
        viewModelScope.launch {
            repository.saveFCMtoken(model).let {
                if (it is Resource.Success) this@RegistrationViewModel.isFcmSaved.postValue(it.value)
            }
        }
    }

    fun get_jwt_token(number: Int, uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val request = repository.getJWTtoken(number, uid)
            if (request.isSuccessful) {
                tokens.postValue(request.body())
            }
        }
    }
}