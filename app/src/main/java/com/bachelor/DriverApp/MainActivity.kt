package com.bachelor.DriverApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bachelor.DriverApp.ui.login.LoginFragment
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.bachelor.DriverApp.ui.main.MainFragment
import com.bachelor.DriverApp.ui.maps.MapsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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