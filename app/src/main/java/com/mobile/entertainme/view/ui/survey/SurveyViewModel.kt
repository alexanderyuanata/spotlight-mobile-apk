package com.mobile.entertainme.view.ui.survey

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobile.entertainme.repository.SurveyRepository

class SurveyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SurveyRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage

    private val _stressPrediction = MutableLiveData<Int?>()
    val stressPrediction: MutableLiveData<Int?> = _stressPrediction

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun submitSurvey(surveyData: HashMap<String, Any>) {
        _isLoading.value = true

        repository.submitSurvey(surveyData) { isSuccess, error ->
            _isLoading.value = false
            if (isSuccess) {
                _isSuccessful.value = true
            } else {
                _isSuccessful.value = false
                _errorMessage.value = error
            }
        }
    }

    fun fetchStressPrediction() {
        val uid = auth.currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("Users")

        // Fetch prediction from API
        repository.fetchStressPrediction(uid) { prediction ->
            _stressPrediction.postValue(prediction)
        }

        // Fetch saved stress level from database
        database.child(uid).child("stress_level").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stressLevel = snapshot.getValue(Int::class.java)
                _stressPrediction.postValue(stressLevel)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}