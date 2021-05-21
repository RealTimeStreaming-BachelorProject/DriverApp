package com.bachelor.DriverApp.ui.packages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.data.viewmodel.PackageServiceViewModel
import com.bachelor.DriverApp.ui.adapters.PackageAdapter

class PackagesFragment : Fragment() {

    private var packageServiceViewModel: PackageServiceViewModel = PackageServiceViewModel
    private lateinit var recyclerAdapter: PackageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_packages, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.packages)

        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.setHasFixedSize(true)

        recyclerAdapter = PackageAdapter(packageServiceViewModel.packages.value!!)
        recyclerView.adapter = recyclerAdapter

        packageServiceViewModel.packages.observe(viewLifecycleOwner, Observer { packages ->
            recyclerAdapter.setPackages(packages)
        })

        return root
    }
}