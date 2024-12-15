package com.mobile.entertainme.view.detail.travel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobile.entertainme.repository.TravelRepository
import com.mobile.entertainme.response.TravelDataItem

class TravelDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TravelRepository()

    private val _travel = MutableLiveData<List<TravelDataItem>?>()
    val travel: LiveData<List<TravelDataItem>?> = _travel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchDetailTravel(uid: String) {
        _isLoading.value = true

        repository.getDetailTravel(uid) { travel, isError ->
            _isLoading.value = false
            if (!isError) {
                _travel.value = travel
            } else {
                // handle error here
            }
        }
    }
}