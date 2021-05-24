package com.bachelor.DriverApp

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bachelor.DriverApp.broadcastreceivers.BatteryCallback
import com.bachelor.DriverApp.broadcastreceivers.LowBatteryReceiver
import com.bachelor.DriverApp.services.LocationService
import com.bachelor.DriverApp.ui.login.LoginFragment
import com.bachelor.DriverApp.ui.maps.MapsFragment
import com.bachelor.DriverApp.ui.packages.PackagesFragment
import com.bachelor.DriverApp.ui.scanner.ScannerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var REQUEST_CODE_LOCATION = 2300

    // Shortcut to change fragment
    private fun gotoFragment(fragment: Fragment, nameOnStack: String?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        LocationService.actionText = getString(R.string.stop_location_action)
        LocationService.contentText = getString(R.string.sending_coordinates)
        LocationService.actionText = getString(R.string.stop_location_action)
        LocationService.errorMessage = getString(R.string.coordinate_error)

        // Bind bottom nav to this activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(this)

        // Set default fragment
        if (savedInstanceState == null) {
            gotoFragment(LoginFragment(), "login_fragment")
        }

        requestPermissions()

        // register receivers
        val filter = IntentFilter()
        filter.addAction("android.intent.action.BATTERY_LOW")
        filter.addAction("android.intent.action.BATTERY_OKAY")
        val receiver = LowBatteryReceiver()
        receiver.registerBatteryCallback(object : BatteryCallback {
            override fun onLowBattery() {
                val intent = Intent(baseContext, LocationService::class.java)
                baseContext.stopService(intent)
                Snackbar.make(bottomNav, R.string.battery_unhealthy, Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNav
                }.setTextColor(Color.RED).show()
            }

            override fun onOkayBattery() {
                val intent = Intent(baseContext, LocationService::class.java)
                baseContext.startForegroundService(intent)

                Snackbar.make(bottomNav, R.string.battery_okay, Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNav
                }.setTextColor(Color.GREEN).show()
            }
        })
        registerReceiver(receiver, filter);
    }

    // Handle click on bottom navigation
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        var nextFragment: Fragment? = null

        when (id) {
            R.id.action_map -> {
                nextFragment = MapsFragment()
            }
            R.id.action_packages -> {
                nextFragment = PackagesFragment()
            }
            R.id.action_scanner -> {
                nextFragment = ScannerFragment()
            }
        }

        if (nextFragment == null) return false // Don't change fragment

        gotoFragment(nextFragment, null)
        return true
    }

    private fun requestPermissions() {

        if (
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

        )
        {
            println(R.string.missing_permissions)
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_CODE_LOCATION
            )
        }


    }

}