package com.mobile.entertainme.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobile.entertainme.api.ApiConfig
import com.mobile.entertainme.response.StressPredictionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyRepository {

    private val database = FirebaseDatabase.getInstance()
    private val surveysRef = database.getReference("stress_surveys")

    fun submitSurvey(surveyData: HashMap<String, Any>, onComplete: (Boolean, String?) -> Unit) {
        val userId = surveyData["user_id"] as String
        val query = surveysRef.orderByChild("user_id").equalTo(userId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Update existing survey
                    for (surveySnapshot in snapshot.children) {
                        surveySnapshot.ref.setValue(surveyData).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onComplete(true, null)
                            } else {
                                onComplete(false, task.exception?.message)
                            }
                        }
                    }
                } else {
                    // Create new survey
                    val newSurveyRef = surveysRef.push()
                    newSurveyRef.setValue(surveyData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onComplete(true, null)
                        } else {
                            onComplete(false, task.exception?.message)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onComplete(false, error.message)
            }
        })
    }

    fun fetchStressPrediction(uid: String, callback: (Int?) -> Unit) {
        val client = ApiConfig.getApiService().getStressPrediction(uid)
        client.enqueue(object : Callback<StressPredictionResponse> {
            override fun onResponse(call: Call<StressPredictionResponse>, response: Response<StressPredictionResponse>) {
                if (response.isSuccessful) {
                    val prediction = response.body()?.prediction
                    callback(prediction)
                    if (prediction != null) {
                        saveStressPrediction(uid, prediction)
                    }
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<StressPredictionResponse>, t: Throwable) {
                callback(null)
            }
        })
    }


    fun saveStressPrediction(uid: String, stressLevel: Int) {
        val database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(uid).child("stress_level").setValue(stressLevel)
    }
}