package com.dicoding.dicodingevent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _event = MutableLiveData<Event?>() // Nullable if API can return null
    val event: LiveData<Event?> get() = _event

    fun fetchEventDetail(eventId: String) {
        Log.d("DetailViewModel", "Fetching event details for eventId: $eventId")

        ApiConfig.getApiService().getDetailEvents(eventId).enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    val event = response.body()
                    if (event != null) {
                        _event.value = event
                        Log.d("DetailViewModel", "Event details fetched successfully: $event")
                    } else {
                        Log.e("DetailViewModel", "Event is null for eventId: $eventId")
                        _event.value = null
                    }
                } else {
                    Log.e("DetailViewModel", "Failed to get event details: ${response.message()}")
                    _event.value = null
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                Log.e("DetailViewModel", "API call failed: ${t.message}")
                _event.value = null
                t.printStackTrace()
            }
        })
    }
}