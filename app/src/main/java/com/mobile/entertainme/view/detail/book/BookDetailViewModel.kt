package com.mobile.entertainme.view.detail.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.entertainme.api.ApiConfig
import com.mobile.entertainme.repository.BookRepository
import com.mobile.entertainme.response.BookDataItem
import com.mobile.entertainme.response.BookResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BookRepository()

    private val _books = MutableLiveData<List<BookDataItem>?>()
    val books: MutableLiveData<List<BookDataItem>?> = _books

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchDetailBooks(uid: String) {
        _isLoading.value = true

        repository.getDetailBooks(uid) { books, isError ->
            _isLoading.value = false
            if (!isError) {
                _books.value = books
            } else {
                // Handle error condition
            }
        }
    }
}