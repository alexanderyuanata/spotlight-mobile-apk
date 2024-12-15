package com.mobile.entertainme.view.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mobile.entertainme.repository.SignupRepository

class SignupViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SignupRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true

        repository.registerUser(name, email, password) { isSuccess, error ->
            _isLoading.value = false
            if (isSuccess) {
                _isSuccessful.value = true
            } else {
                _isSuccessful.value = false
                _errorMessage.value = error
            }
        }
    }
}