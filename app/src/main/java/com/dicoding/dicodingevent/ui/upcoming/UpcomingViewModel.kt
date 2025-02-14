package com.dicoding.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {

    private val _listEvent = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
     val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object {
        private const val TAG = "UpcomingViewModel"
    }

    init {
        getEvents()
    }

    fun getEvents() {
        findEvents(active = 1)

    }

    fun searchEvents(query: String) {
        findEvents(active = 1 , query = query)
    }

    private fun findEvents(active: Int? = null , query: String? = null) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvent(active = active, q = query , 40)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse> ,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    //if (responseBody != null){
                    _listEvent.value = response.body()?.listEvents
                } else {
                    Log.e(TAG , "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse> , t: Throwable) {
                _isLoading.value = false
                Log.e(TAG , "onFailure: ${t.message.toString()}")

            }
        })

    }
}



//    viewModel.listEvent.observe(viewLifecycleOwner) {
//
//    }