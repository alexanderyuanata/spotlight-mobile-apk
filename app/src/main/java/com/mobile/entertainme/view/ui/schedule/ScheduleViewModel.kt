package com.mobile.entertainme.view.ui.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobile.entertainme.data.ScheduleActivity
import com.mobile.entertainme.repository.ScheduleRepository

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ScheduleRepository()

    private val _schedules = MutableLiveData<List<ScheduleActivity>>()
    val schedules: LiveData<List<ScheduleActivity>> = _schedules

    fun fetchSchedules(userUid: String, category: String = "") {
        repository.fetchSchedules(userUid, category) { scheduleList ->
            _schedules.postValue(scheduleList)
        }
    }

    fun markScheduleAsCompleted(schedule: ScheduleActivity) {
        repository.markScheduleAsCompleted(schedule) {
            fetchSchedules(schedule.uid)
        }
    }

    fun deleteSchedule(schedule: ScheduleActivity) {
        repository.deleteSchedule(schedule) {
            fetchSchedules(schedule.uid)
        }
    }
}