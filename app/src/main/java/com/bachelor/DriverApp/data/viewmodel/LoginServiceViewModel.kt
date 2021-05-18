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

    private val successMessage = SingleLiveEvent<String>()
    fun getSuccessMessage(): SingleLiveEvent<String> {
        return successMessage
    }

    private val errorMessage = SingleLiveEvent<String>()
    fun getErrorMessage(): SingleLiveEvent<String> {
        return errorMessage
    }

    fun login(username: String, password: String) {

        if (username == "test") {
            successMessage.postValue("Hej Tester")
            return
        }

        viewModelScope.launch {
            val jsonBody = LoginRequestBody(username, password)
            try {
                val loginResponse = withContext(Dispatchers.IO) {
                    loginService.login(jsonBody)
                }
                if (loginResponse.isSuccessful) {
                    successMessage.postValue(loginResponse.body()?.message)
                    val jwt = JWT(loginResponse.body()?.token!!)
                    DriverData.JWT = loginResponse.body()?.token!!
                    DriverData.driverID = UUID.fromString(jwt.getClaim("driverID").asString())
                } else {
                    errorMessage.postValue(loginResponse.message())
                }
            } catch (e: Exception) {
                errorMessage.postValue("Could not contact Login Server")
            }
        }
    }

}
