package com.bachelor.DriverApp.ui.scanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.config.DriverData
import com.bachelor.DriverApp.data.viewmodel.PackageServiceViewModel

class ScannerFragment : Fragment() {

    private var packageServiceViewModel = PackageServiceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_scanner, container, false)
        root.findViewById<Button>(R.id.packagePickUp).setOnClickListener {
            onDriverPickUpScan()
        }

        packageServiceViewModel.getErrorMessage().observe(this, Observer {
            Toast.makeText(root.context, it, Toast.LENGTH_SHORT).show()
        })
        return root;
    }

    private fun onDriverPickUpScan() {
        packageServiceViewModel.registerNewPackage()
        if (DriverData.driverID != null) {
            packageServiceViewModel.driverPickUp(packageServiceViewModel.getRandomValidPackageID(),
                DriverData.driverID!!
            )
        }

    }

}