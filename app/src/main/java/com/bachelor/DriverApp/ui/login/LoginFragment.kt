package com.bachelor.DriverApp.ui.login

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.bachelor.DriverApp.R
import com.bachelor.DriverApp.data.viewmodel.LoginServiceViewModel
import com.bachelor.DriverApp.ui.maps.MapsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private lateinit var loginServiceViewModel: LoginServiceViewModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var rootView: View

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
        rootView = view

        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.login)
        loadingProgressBar = view.findViewById(R.id.loading)

        loginButton.setOnClickListener {
            hideKeyboard()
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
            showLoginFailed(message)
        })
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun successfulLogin(message: String) {
        switchToNextFragment()

        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        loadingProgressBar.visibility = View.INVISIBLE

        if (navBar != null) {
            navBar.visibility = View.VISIBLE
        }
    }

    private fun switchToNextFragment() {
        val nextFragment = MapsFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, nextFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showLoginFailed(message: String) {
        loadingProgressBar.visibility = View.INVISIBLE
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).setTextColor(Color.RED).show()
    }
}