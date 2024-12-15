package com.mobile.entertainme.view.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mobile.entertainme.data.ScheduleActivity
import com.mobile.entertainme.repository.ScheduleRepository

class AddScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ScheduleRepository()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun addSchedule(title: String, description: String, category: String, date: String, time: String) {
        _isLoading.value = true

        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (uid != null) {
            val scheduleActivity = ScheduleActivity(
                title = title,
                description = description,
                category = category,
                date = date,
                time = time,
                completed = false,
                uid = uid
            )

            repository.addSchedule(scheduleActivity) { isSuccess ->
                _isLoading.value = false
                _isSuccess.value = isSuccess
            }
        } else {
            _isLoading.value = false
            _isSuccess.value = false
        }
    }
}