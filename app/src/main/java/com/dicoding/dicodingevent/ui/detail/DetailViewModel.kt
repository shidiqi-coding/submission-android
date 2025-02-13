package com.dicoding.dicodingevent.ui.detail

//import android.R.id
//import android.util.Log
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.data.response.DetailEventResponse
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detailEvent = MutableLiveData<Event>()
    val detailEvent: LiveData<Event>  = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>  = _isLoading

   private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    @SuppressLint("SuspiciousIndentation")
    fun getDetailEvents(id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvents(id)
            client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val detailEventResponse = response.body()
                    if (detailEventResponse!= null && !detailEventResponse.error)  {
                        _detailEvent.value = detailEventResponse.event
                        //Log.d("DetailViewModel", "Event details fetched successfully: $event")
                    } else {
                       // Log.e("DetailViewModel", "Event is null for eventId: $eventId")
                        _errorMessage.value = detailEventResponse?.message ?:"Unknown error occured"
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DetailEventResponse> , t: Throwable) {
//                Log.e("DetailViewModel", "API call failed: ${t.message}")
               _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }
}