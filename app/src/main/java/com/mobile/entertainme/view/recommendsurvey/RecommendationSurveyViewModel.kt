package com.mobile.entertainme.view.recommendsurvey

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobile.entertainme.repository.RecommendationSurveyRepository

class RecommendationSurveyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecommendationSurveyRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun submitSurvey(surveyData: HashMap<String, Any>) {
        _isLoading.value = true

        repository.submitSurvey(surveyData) { isSuccess ->
            _isLoading.value = false
            _isSuccessful.value = isSuccess
        }
    }
}