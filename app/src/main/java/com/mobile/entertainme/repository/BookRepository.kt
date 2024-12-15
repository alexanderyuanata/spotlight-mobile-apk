package com.mobile.entertainme.repository

import com.mobile.entertainme.api.ApiConfig
import com.mobile.entertainme.response.BookDataItem
import com.mobile.entertainme.response.BookResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookRepository {

    fun getDetailBooks(uid: String, callback: (List<BookDataItem>?, Boolean) -> Unit) {
        val client = ApiConfig.getApiService().getRecommendedBooks(uid)
        client.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    val books = response.body()?.titles?.filterNotNull()
                    callback(books, false)
                } else {
                    callback(null, true)
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                callback(null, true)
            }
        })
    }
}