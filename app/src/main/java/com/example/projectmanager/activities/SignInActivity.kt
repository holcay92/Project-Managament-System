package com.example.projectmanager.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.projectmanager.R
import com.example.projectmanager.databinding.ActivitySignInBinding
import com.example.projectmanager.firebase.FireStoreClass
import com.example.projectmanager.modals.User
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {
    private var binding: ActivitySignInBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()
        binding?.btnSignIn?.setOnClickListener {
            signInRegisteredUser()
        }
    }

    private fun setupActionBar() {
        // default action bar for android
        setSupportActionBar(binding?.toolbarSignInActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        binding?.toolbarSignInActivity?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun signInRegisteredUser() {
        val email: String = binding?.etEmail?.text.toString().trim { it <= ' ' }
        val password: String = binding?.etPassword?.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            // Log in the user with email and password using FirebaseAuth.
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        FireStoreClass().loadUserData(this)
                       // val user = auth.currentUser
                        startActivity(Intent(this, MainActivity::class.java))
                    }else{
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    private fun validateForm(email: String, password: String): Boolean {
        return when {

            email.isEmpty() -> {
                showErrorSnackBar("Please enter an email address.")
                false
            }
            password.isEmpty() -> {
                showErrorSnackBar("Please enter a password.")
                false
            }
            else -> {
                // Show the error message if the password is less than 6 characters.
                true
            }
        }
    }
    fun signInSuccess(user: User) {
        // Hide the progress dialog
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}