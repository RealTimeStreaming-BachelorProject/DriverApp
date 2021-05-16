package com.bachelor.DriverApp.data.viewmodel;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.bachelor.DriverApp.config.DriverData
import com.bachelor.DriverApp.data.requestbodies.loginservice.LoginRequestBody
import com.bachelor.DriverApp.data.repository.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Driver
import java.util.*

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
            val jsonBody = LoginRequestBody(username, password)
            withContext(Dispatchers.IO) {
                try {
                    val loginResponse = loginService.login(jsonBody)
                    if (loginResponse.isSuccessful) {
                        _successLoginMessage.postValue(loginResponse.body()?.message)
                        var jwt = JWT(loginResponse.body()?.token!!)
                        DriverData.JWT = loginResponse.body()?.token!!
                        DriverData.driverID = UUID.fromString(jwt.getClaim("driverID").asString())
                    } else {
                        _failureLoginMessage.postValue(loginResponse.message())
                    }
                } catch (e: Exception) {
                    _failureLoginMessage.postValue("Server Error")
                }

            }

        }
    }

}
