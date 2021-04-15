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

    private lateinit var loginServiceViewModel: LoginServiceViewModel
    private lateinit var testSuccess: TextView
    private lateinit var testFail : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.main_fragment, container, false)

        var btn = root.findViewById<Button>(R.id.loginBtn);
        btn.setOnClickListener {
            onLogin(btn)
        }

        testSuccess = root.findViewById(R.id.successMessage)
        testFail = root.findViewById(R.id.failMessage)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginServiceViewModel = ViewModelProvider(this).get(LoginServiceViewModel::class.java)

        loginServiceViewModel.successLoginMessage.observe(this) { message ->
            testSuccess.text = message
        }
        loginServiceViewModel.failLoginMessage.observe(this) { message ->
            testFail.text = message
        }
    }

    fun onLogin(view: View) {
        loginServiceViewModel.login("MonkeyApe", "RandomPassword1.")
    }
}