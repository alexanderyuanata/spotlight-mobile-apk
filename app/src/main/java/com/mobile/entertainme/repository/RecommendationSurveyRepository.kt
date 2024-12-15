package com.mobile.entertainme.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecommendationSurveyRepository {

    private val database = FirebaseDatabase.getInstance().getReference("recommendation_surveys")

    fun submitSurvey(surveyData: HashMap<String, Any>, callback: (Boolean) -> Unit) {
        val userId = surveyData["user_id"] as String
        checkExistingSurvey(userId) { existingSurveyId ->
            if (existingSurveyId != null) {
                // Update existing survey
                database.child(existingSurveyId).setValue(surveyData).addOnCompleteListener { task ->
                    callback(task.isSuccessful)
                }
            } else {
                // Create new survey
                val newSurveyRef = database.push()
                newSurveyRef.setValue(surveyData).addOnCompleteListener { task ->
                    callback(task.isSuccessful)
                }
            }
        }
    }

    private fun checkExistingSurvey(userId: String, callback: (String?) -> Unit) {
        val query = database.orderByChild("user_id").equalTo(userId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (surveySnapshot in snapshot.children) {
                        callback(surveySnapshot.key) // Return the key of the existing survey
                        return
                    }
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null) // Handle the error appropriately in a real application
            }
        })
    }
}