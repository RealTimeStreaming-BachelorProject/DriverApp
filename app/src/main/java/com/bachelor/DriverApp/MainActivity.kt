package com.bachelor.DriverApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bachelor.DriverApp.ui.login.LoginFragment
import android.util.Log
import com.bachelor.DriverApp.api.loginservice.LoginResponse
import com.bachelor.DriverApp.api.loginservice.LoginService
import com.bachelor.DriverApp.ui.main.MainFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment()) // Start app at LOGIN FRAGMENT
                    .commitNow()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:5005/authentication/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(LoginService::class.java)
        val call = retrofitService.Login("MonkeyApe", "RandomPassword1.");
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.i(TAG, response.body()!!.token)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.i(TAG, t.message!!);
            }
        })
    }

    companion object {
        const val TAG = "main_activity"
    }
}