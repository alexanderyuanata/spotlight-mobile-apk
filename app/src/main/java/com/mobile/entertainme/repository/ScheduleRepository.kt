package com.mobile.entertainme.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobile.entertainme.data.ScheduleActivity

class ScheduleRepository {

    private val database = FirebaseDatabase.getInstance().getReference("schedule_activity")

    fun fetchSchedules(userUid: String, category: String = "", callback: (List<ScheduleActivity>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val scheduleList = mutableListOf<ScheduleActivity>()
                dataSnapshot.children.forEach { snapshot ->
                    val schedule = snapshot.getValue(ScheduleActivity::class.java)
                    schedule?.let {
                        if (it.uid == userUid && (category.isBlank() || it.category == category)) {
                            it.firebaseKey = snapshot.key ?: ""
                            scheduleList.add(it)
                        }
                    }
                }
                callback(scheduleList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancellation if needed
            }
        })
    }

    fun markScheduleAsCompleted(schedule: ScheduleActivity, onComplete: () -> Unit) {
        val updatedSchedule = schedule.copy(completed = true)
        database.child(schedule.firebaseKey).setValue(updatedSchedule)
            .addOnSuccessListener {
                onComplete()
            }
            .addOnFailureListener {
                // Handle failure if needed
            }
    }

    fun deleteSchedule(schedule: ScheduleActivity, onComplete: () -> Unit) {
        database.child(schedule.firebaseKey).removeValue()
            .addOnSuccessListener {
                onComplete()
            }
            .addOnFailureListener {
                // Handle failure if needed
            }
    }

    fun addSchedule(scheduleActivity: ScheduleActivity, callback: (Boolean) -> Unit) {
        val scheduleId = database.push().key

        if (scheduleId != null) {
            database.child(scheduleId).setValue(scheduleActivity)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful)
                }
        } else {
            callback(false)
        }
    }
}