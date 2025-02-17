package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        fetchUpcomingEvents()
        fetchFinishedEvents()

    }

    private fun fetchUpcomingEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(1 , "" , 5)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse> , response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                     response.body()?.let { eventResponse ->
                    //if (responseBody != null) {
                        _upcomingEvent.value = eventResponse.listEvents.take(5)
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse> , t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }

        })

    }

    private fun fetchFinishedEvents() {
     _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(0 , "" , 5)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse> , response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                     response.body()?.let { eventResponse ->
                   // if (responseBody != null) {
                        _finishedEvent.value = eventResponse.listEvents.take(5)
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse> , t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }

        })

    }
}



//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text


