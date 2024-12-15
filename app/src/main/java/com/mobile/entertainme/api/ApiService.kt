package com.mobile.entertainme.api

import com.mobile.entertainme.response.BookResponse
import com.mobile.entertainme.response.MovieResponse
import com.mobile.entertainme.response.StressPredictionResponse
import com.mobile.entertainme.response.TravelResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("recommend/books")
    fun getRecommendedBooks(@Query("uid") uid: String): Call<BookResponse>

    @GET("recommend/movies")
    fun getRecommendedMovies(@Query("uid") uid: String): Call<MovieResponse>

    @GET("recommend/travel")
    fun getRecommendedTravel(@Query("uid") uid: String): Call<TravelResponse>

    @GET("predict/stress")
    fun getStressPrediction(@Query("uid") uid: String): Call<StressPredictionResponse>
}