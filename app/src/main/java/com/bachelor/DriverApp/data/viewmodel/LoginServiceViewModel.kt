package com.bachelor.DriverApp.data.viewmodel;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bachelor.DriverApp.data.requestbodies.loginservice.LoginRequestBody
import com.bachelor.DriverApp.data.repository.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginServiceViewModel : ViewModel() {
    private val loginService = ServiceBuilder().getLoginService()
    private val _successLoginMessage = MutableLiveData<String>()
    val successLoginMessage: LiveData<String>
        get() = _successLoginMessage

    private val _failureLoginMessage = MutableLiveData<String>()
    val failLoginMessage: LiveData<String>
        get() = _failureLoginMessage

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val jsonBody: LoginRequestBody = LoginRequestBody(username, password)
            withContext(Dispatchers.IO) {
                val loginResponse = loginService.login(jsonBody)
                if (loginResponse.isSuccessful) {
                    // TODO:
                    _successLoginMessage.postValue(loginResponse.body()!!.message)
                } else {
                    _failureLoginMessage.postValue(loginResponse.message())
                }
            }

        }
    }

}
