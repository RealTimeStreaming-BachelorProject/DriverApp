package com.bachelor.DriverApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bachelor.DriverApp.ui.login.LoginFragment
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.bachelor.DriverApp.api.loginservice.LoginResponse
import com.bachelor.DriverApp.api.loginservice.LoginService
import com.bachelor.DriverApp.ui.main.MainFragment
import com.bachelor.DriverApp.ui.maps.MapsFragment
import com.google.android.gms.maps.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    // Shortcut to change fragment
    fun gotoFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitNow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Bind bottom nav to this activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(this)

        // Set default fragment
        if (savedInstanceState == null) {
            gotoFragment(LoginFragment())
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

    // Handle click on bottom navigation
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        var nextFragment: Fragment? = null

        if (id.equals(R.id.action_main)) {
            nextFragment = MainFragment()
        } else if (id.equals(R.id.action_map)) {
            nextFragment = MapsFragment()
        }

        if (nextFragment == null) return false // Don't change fragment

        gotoFragment(nextFragment)
        return true
    }
}