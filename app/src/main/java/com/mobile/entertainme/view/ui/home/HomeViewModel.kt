package com.mobile.entertainme.view.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobile.entertainme.repository.HomeRepository
import com.mobile.entertainme.response.BookDataItem
import com.mobile.entertainme.response.MovieDataItem
import com.mobile.entertainme.response.TravelDataItem
import com.mobile.entertainme.utils.UserPreferences

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HomeRepository = HomeRepository()
    val books: LiveData<List<BookDataItem>> = repository.books
    val movies: LiveData<List<MovieDataItem>> = repository.movies
    val travel: LiveData<List<TravelDataItem>> = repository.travel

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _stressPrediction = MutableLiveData<Int?>()
    val stressPrediction: MutableLiveData<Int?> = _stressPrediction

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    init {
        fetchUsername()
        fetchStressLevel()
    }

    private fun fetchUsername() {
        val uid = auth.currentUser?.uid ?: return
        database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("name").getValue(String::class.java)
                _username.value = username!!
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun fetchStressLevel() {
        val uid = auth.currentUser?.uid ?: return

        database.child(uid).child("stress_level").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stressLevel = snapshot.getValue(Int::class.java)
                _stressPrediction.postValue(stressLevel)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    fun fetchBooks() {
        val uid = auth.currentUser?.uid ?: return
        repository.fetchBooks(uid)
    }

    fun fetchMovies() {
        val uid = auth.currentUser?.uid ?: return
        repository.fetchMovies(uid)
    }

    fun fetchTravel() {
        val uid = auth.currentUser?.uid ?: return
        repository.fetchTravel(uid)
    }

    fun logout() {
        auth.signOut()
        UserPreferences(getApplication()).clear()
    }

    fun clearGoogleSignInInfo() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val providerId = currentUser.providerData.find { it.providerId == GoogleAuthProvider.PROVIDER_ID }?.providerId
            if (providerId != null) {
                currentUser.unlink(providerId)
            }
        }
    }
}