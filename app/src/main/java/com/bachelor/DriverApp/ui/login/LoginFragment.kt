package com.bachelor.DriverApp.ui.login

import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.bachelor.DriverApp.MainActivity

import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.config.DriverData
import com.bachelor.DriverApp.data.viewmodel.LoginServiceViewModel
import com.bachelor.DriverApp.ui.main.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginFragment : Fragment() {

    private lateinit var loginServiceViewModel: LoginServiceViewModel
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginServiceViewModel = ViewModelProvider(this).get(LoginServiceViewModel::class.java)

        // TODO: check if JWT is present

        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.login)
        loadingProgressBar = view.findViewById(R.id.loading)

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginServiceViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        loginServiceViewModel.getSuccessMessage().observe(this, Observer { message ->
            successfulLogin(message)
        })

        loginServiceViewModel.getErrorMessage().observe(this, Observer { message ->
            Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun successfulLogin(message: String) {
        loadingProgressBar.visibility = View.INVISIBLE
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()

        switchToMainFragment()

        var navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        if (navBar != null) {
            navBar.visibility = View.VISIBLE
        }
    }

    private fun switchToMainFragment() {
        val mainFragment = MainFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, mainFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showLoginFailed(message: String) {
        loadingProgressBar.visibility = View.INVISIBLE
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
    }
}