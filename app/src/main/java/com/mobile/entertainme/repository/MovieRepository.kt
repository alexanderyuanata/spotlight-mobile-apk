package com.mobile.entertainme.repository

import com.mobile.entertainme.api.ApiConfig
import com.mobile.entertainme.response.MovieDataItem
import com.mobile.entertainme.response.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository {

    fun getDetailMovies(uid: String, callback: (List<MovieDataItem>?, Boolean) -> Unit) {
        val client = ApiConfig.getApiService().getRecommendedMovies(uid)
        client.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.movies?.filterNotNull()
                    callback(movies, false)
                } else {
                    callback(null, true)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                callback(null, true)
            }
        })
    }
}