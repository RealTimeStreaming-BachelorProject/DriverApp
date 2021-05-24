package com.bachelor.DriverApp.ui.scanner

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.data.viewmodel.PackageServiceViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class ScannerFragment : Fragment() {

    private var packageServiceViewModel = PackageServiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val root = inflater.inflate(R.layout.fragment_scanner, container, false)

        root.findViewById<Button>(R.id.packagePickUp).setOnClickListener {
            packageServiceViewModel.registerNewPackage()
        }

        root.findViewById<Button>(R.id.packageDelivery).setOnClickListener {
            if (packageServiceViewModel.deliveredPackageCounter < packageServiceViewModel.packages.value?.size!!) {
                val packageToDeliver = packageServiceViewModel.packages.value?.get(packageServiceViewModel.deliveredPackageCounter++)

                packageServiceViewModel.driverDelivery(packageToDeliver?.packageId)
            }
            else {
                if (navBar != null) {
                    Snackbar.make(navBar, "No packages to deliver", Snackbar.LENGTH_LONG).apply {
                        anchorView = navBar
                    }.setTextColor(Color.RED).show()
                }
            }
        }

        packageServiceViewModel.getErrorMessage().observe(this, Observer {
            if (navBar != null) {
                Snackbar.make(navBar, it, Snackbar.LENGTH_LONG).apply {
                    anchorView = navBar
                }.setTextColor(Color.RED).show()
            }
        })
        return root
    }
}