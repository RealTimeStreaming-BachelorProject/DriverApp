package com.bachelor.DriverApp.ui.packages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.data.viewmodel.LoginServiceViewModel
import com.bachelor.DriverApp.data.viewmodel.PackageServiceViewModel

class PackagesFragment : Fragment() {

    private lateinit var packageServiceViewModel: PackageServiceViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        packageServiceViewModel = ViewModelProvider(this).get(PackageServiceViewModel::class.java)


    }
}