package com.mobile.entertainme.view.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.mobile.entertainme.repository.LoginRepository
import com.mobile.entertainme.utils.UserPreferences

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LoginRepository(application)

    val isLoading: LiveData<Boolean> = repository.isLoading
    val isSuccessful: LiveData<Boolean> = repository.isSuccessful
    val errorMessage: LiveData<String> = repository.errorMessage

    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        repository.firebaseAuthWithGoogle(idToken)
    }
}