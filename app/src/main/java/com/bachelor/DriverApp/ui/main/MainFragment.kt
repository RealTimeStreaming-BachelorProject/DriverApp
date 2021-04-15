package com.bachelor.DriverApp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.observe
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.data.viewmodel.LoginServiceViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        const val TAG = "main_activity"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.main_fragment, container, false)

        return root
    }

}