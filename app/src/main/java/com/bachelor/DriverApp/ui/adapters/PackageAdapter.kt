package com.bachelor.DriverApp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.data.models.packageservice.PackageData
import java.time.LocalTime

class PackageAdapter(packages: ArrayList<PackageData>) : RecyclerView.Adapter<PackageAdapter.ViewHolder>() {
    private var packagesList: ArrayList<PackageData> = packages

    fun setPackages(packages: ArrayList<PackageData>) {
        this.packagesList = packages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_packages, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.packageAddress.text = packagesList[position].receiverAddress
        holder.estimatedDeliveryTime.text = LocalTime.of(packagesList[position].expectedDeliveryTime.hours, packagesList[position].expectedDeliveryTime.minutes, 0)
            .toString()
        val delivered = packagesList[position].delivered
        if (delivered)
            holder.deliveryStatus.visibility = View.VISIBLE
        else
            holder.deliveryStatus.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return packagesList.size
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var packageAddress: TextView = view.findViewById(R.id.packageAddress)
        var estimatedDeliveryTime: TextView = view.findViewById(R.id.estimatedDeliveryTime)
        var deliveryStatus: ImageView = view.findViewById(R.id.deliveredStatus)
    }
}