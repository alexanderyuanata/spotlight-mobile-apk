package com.mobile.entertainme.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun registerUser(name: String, email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid
                    val userRef = database.reference.child("Users").child(uid ?: "")
                    val userMap = mapOf(
                        "name" to name,
                        "email" to email
                    )
                    userRef.setValue(userMap)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                onComplete(true, null)
                            } else {
                                onComplete(false, dbTask.exception?.message)
                            }
                        }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }
}