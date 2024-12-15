package com.mobile.entertainme.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.mobile.entertainme.R
import com.mobile.entertainme.databinding.ActivityLoginBinding
import com.mobile.entertainme.utils.UserPreferences
import com.mobile.entertainme.view.MainActivity
import com.mobile.entertainme.view.ViewModelFactory
import com.mobile.entertainme.view.recommendsurvey.RecommendationSurveyActivity
import com.mobile.entertainme.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(application)
        )[LoginViewModel::class.java]

        val userPreferences = UserPreferences(this)
        if (userPreferences.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding.signupText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        val emailEditText = binding.edLoginEmail
        val emailInputLayout = binding.emailEditTextLayout
        val passwordEditText = binding.edLoginPassword
        val passwordInputLayout = binding.passwordEditTextLayout

        emailInputLayout.setEditText(emailEditText)
        passwordInputLayout.setEditText(passwordEditText)

        binding.loginBtn.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginViewModel.loginUser(email, password)
        }


        binding.googleLoginBtn.setOnClickListener {
            setupGoogleSignInClient()
            signInWithGoogle()
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        loginViewModel.isSuccessful.observe(this) { isSuccessful ->
            if (isSuccessful) {
                showDialog("Login Successful") {
                    startActivity(Intent(this, RecommendationSurveyActivity::class.java))
                    finish()
                }
            }
        }

        loginViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                showDialog("Login Failed: $errorMessage")
            }
        }
    }

    private fun setupGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                loginViewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                showDialog("Google sign in failed: ${e.message}")
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDialog(message: String, onPositiveButtonClick: (() -> Unit)? = null) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onPositiveButtonClick?.invoke()
            }
            .create()
            .show()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}