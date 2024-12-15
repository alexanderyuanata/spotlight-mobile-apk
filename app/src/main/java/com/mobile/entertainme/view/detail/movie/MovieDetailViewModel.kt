package com.mobile.entertainme.view.detail.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.entertainme.api.ApiConfig
import com.mobile.entertainme.repository.MovieRepository
import com.mobile.entertainme.response.MovieDataItem
import com.mobile.entertainme.response.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository()

    private val _movies = MutableLiveData<List<MovieDataItem>?>()
    val movies: MutableLiveData<List<MovieDataItem>?> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchDetailMovies(uid: String) {
        _isLoading.value = true

        repository.getDetailMovies(uid) { movies, isError ->
            _isLoading.value = false
            if (!isError) {
                _movies.value = movies
            } else {
                // Handle error condition
            }
        }
    }
}