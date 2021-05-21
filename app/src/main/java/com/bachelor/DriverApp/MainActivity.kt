package com.bachelor.DriverApp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bachelor.DriverApp.ui.login.LoginFragment
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bachelor.DriverApp.ui.main.MainFragment
import com.bachelor.DriverApp.ui.maps.MapsFragment
import com.bachelor.DriverApp.ui.packages.PackagesFragment
import com.bachelor.DriverApp.ui.scanner.ScannerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var REQUEST_CODE_LOCATION = 2300

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

        requestPermissions()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        println(requestCode)
        println(permissions.toString())
        println(grantResults.toString())
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // Handle click on bottom navigation
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        var nextFragment: Fragment? = null

        if (id.equals(R.id.action_main)) {
            nextFragment = MainFragment()
        } else if (id.equals(R.id.action_map)) {
            nextFragment = MapsFragment()
        } else if (id.equals(R.id.action_packages)) {
            nextFragment = PackagesFragment()
        } else if (id.equals(R.id.action_scanner)) {
            nextFragment = ScannerFragment()
        }

        if (nextFragment == null) return false // Don't change fragment

        gotoFragment(nextFragment)
        return true
    }

    private fun requestPermissions() {

        if (
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

        )
        {
            println("Permissions missing. Requesting permission.")
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION
            )
        }


    }

}