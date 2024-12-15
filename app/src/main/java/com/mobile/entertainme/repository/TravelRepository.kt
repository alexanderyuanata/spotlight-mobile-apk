package com.mobile.entertainme.repository

import com.mobile.entertainme.api.ApiConfig
import com.mobile.entertainme.response.TravelDataItem
import com.mobile.entertainme.response.TravelResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TravelRepository {

    fun getDetailTravel(uid: String, callback: (List<TravelDataItem>?, Boolean) -> Unit) {
        val client = ApiConfig.getApiService().getRecommendedTravel(uid)
        client.enqueue(object : Callback<TravelResponse> {
            override fun onResponse(
                call: Call<TravelResponse>,
                response: Response<TravelResponse>
            ) {
                if (response.isSuccessful) {
                    val travel = response.body()?.recommendations?.filterNotNull()
                    callback(travel, false)
                } else {
                    callback(null, true)
                }
            }

            override fun onFailure(call: Call<TravelResponse>, t: Throwable) {
                callback(null, true)
            }
        })
    }
}