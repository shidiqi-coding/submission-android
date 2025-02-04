package com.dicoding.dicodingevent

import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dicodingevent.data.response.ListEventsItem

class ListEventAdapter(
    private val event:List<ListEventsItem>
    private val onItemClicked : (ListEventsItem) -> Unit

) : RecyclerView.Adapter<ListEventAdapter.EventViewHolder>() {

     inner class EventViewHolder(ItemView)
}