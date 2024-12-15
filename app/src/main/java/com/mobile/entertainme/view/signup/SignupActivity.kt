package com.mobile.entertainme.view.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.mobile.entertainme.R
import com.mobile.entertainme.databinding.ActivitySignupBinding
import com.mobile.entertainme.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        val emailEditText = binding.edRegisterEmail
        val emailInputLayout = binding.emailEditTextLayout
        val passwordEditText = binding.edRegisterPassword
        val passwordInputLayout = binding.passwordEditTextLayout

        emailInputLayout.setEditText(emailEditText)
        passwordInputLayout.setEditText(passwordEditText)

        binding.signupBtn.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            signupViewModel.registerUser(name, email, password)
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signupViewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        signupViewModel.isSuccessful.observe(this, Observer { isSuccessful ->
            if (isSuccessful) {
                showDialog("Registration Successful") {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        })

        signupViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                showDialog("Registration Failed: $errorMessage")
            }
        })
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
}