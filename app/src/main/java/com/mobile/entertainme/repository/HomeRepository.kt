package com.mobile.entertainme.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.mobile.entertainme.api.ApiConfig
import com.mobile.entertainme.response.BookDataItem
import com.mobile.entertainme.response.BookResponse
import com.mobile.entertainme.response.MovieDataItem
import com.mobile.entertainme.response.MovieResponse
import com.mobile.entertainme.response.StressPredictionResponse
import com.mobile.entertainme.response.TravelDataItem
import com.mobile.entertainme.response.TravelResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {

    private val _books = MutableLiveData<List<BookDataItem>>()
    val books: LiveData<List<BookDataItem>> = _books

    private val _movies = MutableLiveData<List<MovieDataItem>>()
    val movies: LiveData<List<MovieDataItem>> = _movies

    private val _travel = MutableLiveData<List<TravelDataItem>>()
    val travel: LiveData<List<TravelDataItem>> = _travel

    fun fetchBooks(uid: String) {
        val client = ApiConfig.getApiService().getRecommendedBooks(uid)
        client.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    _books.value = response.body()?.titles?.filterNotNull()
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun fetchMovies(uid: String) {
        val client = ApiConfig.getApiService().getRecommendedMovies(uid)
        client.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    _movies.value = response.body()?.movies?.filterNotNull()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun fetchTravel(uid: String) {
        val client = ApiConfig.getApiService().getRecommendedTravel(uid)
        client.enqueue(object : Callback<TravelResponse> {
            override fun onResponse(call: Call<TravelResponse>, response: Response<TravelResponse>) {
                if (response.isSuccessful) {
                    _travel.value = response.body()?.recommendations?.filterNotNull()
                }
            }

            override fun onFailure(call: Call<TravelResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}