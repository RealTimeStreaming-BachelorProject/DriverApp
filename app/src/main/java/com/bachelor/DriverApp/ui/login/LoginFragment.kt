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
import androidx.lifecycle.observe

import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.data.viewmodel.LoginServiceViewModel

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

        loginServiceViewModel.successLoginMessage.observe(viewLifecycleOwner) { message ->
            successfulLogin(message)
        }
        loginServiceViewModel.failLoginMessage.observe(viewLifecycleOwner) { message ->
            showLoginFailed(message)
        }
    }

    private fun successfulLogin(message: String) {
        loadingProgressBar.visibility = View.INVISIBLE
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
        // TODO: switch view
    }

    private fun showLoginFailed(message: String) {
        loadingProgressBar.visibility = View.INVISIBLE
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
    }
}