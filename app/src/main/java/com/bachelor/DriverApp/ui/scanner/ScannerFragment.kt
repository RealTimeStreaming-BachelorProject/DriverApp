package com.bachelor.DriverApp.ui.scanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.config.DriverData
import com.bachelor.DriverApp.data.viewmodel.PackageServiceViewModel

class ScannerFragment : Fragment() {

    private var packageServiceViewModel = PackageServiceViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_scanner, container, false)
        root.findViewById<Button>(R.id.packagePickUp).setOnClickListener {
            onDriverPickUpScan()
        }
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