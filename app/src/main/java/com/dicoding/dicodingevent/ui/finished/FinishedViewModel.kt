package com.dicoding.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val eventLiveData = MutableLiveData<List<ListEventsItem>>()
    private val errorMessageLiveData = MutableLiveData<String> ()

    fun getEvent() : LiveData<List<ListEventsItem>> = eventLiveData

    fun getErrorMessage () : LiveData<String> = errorMessageLiveData

    fun fetchFinished(forceReload : Boolean = false){
        if(forceReload || eventLiveData.value == null || eventLiveData.value!!.isEmpty()) {
            ApiConfig.getApiService().getEvent(0).enqueue(object : Callback<com.dicoding.dicodingevent.data.response.EventResponse> {
                override fun onResponse(
                    call: Call<com.dicoding.dicodingevent.data.response.Event> ,
                    response: Response<com.dicoding.dicodingevent.data.response.Event>
                ) {

                    if (response.isSuccessful) {
                        val eventList = response.body()?.listEvents?.filterNotNull()
                        eventLiveData.value = eventList ?: emptyList()
                    } else {
                        errorMessageLiveData.value =
                            "Failed to fetch events. Please try again later."
                    }

                }

                override fun onFailure(call: Call<com.dicoding.dicodingevent.data.response.Event>, t: Throwable) {
                 errorMessageLiveData.value = "Network error. Please check your internet connection."


                }
            })
        }

    }

    fun searchEvents(query : String) {
        ApiConfig.getApiService().searchEvents(query).enqueqe(object : Callback<com.dicoding.dicodingevent.data.response.Event> {
            override fun onResponse(
                call: Call<com.dicoding.dicodingevent.data.response.Event>,
                response: Response<com.dicoding.dicodingevent.data.response.Event>

            ) {
                if(response.isSuccessful) {
                    val eventList = response.body()?.listEvents?.filterNotNull()
                    eventLiveData.value = eventList ?: emptyList()
                } else {
                    errorMessageLiveData.value = "Search failed. Please try again"
                }
            }

            override fun onFailure(call: Call<com.dicoding.dicodingevent.data.response.Event> , t: Throwable) {
                errorMessageLiveData.value = "Network Error. Please check your internet connection"
            }
        })
    }

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is dashboard Fragment"
//    }
//    val text: LiveData<String> = _text
}