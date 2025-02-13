package com.dicoding.dicodingevent.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import com.dicoding.dicodingevent.ListEventAdapter
//import com.dicoding.dicodingevent.ListEventAdapter.EventViewHolder
import com.dicoding.dicodingevent.databinding.ItemVerticalFinishedBinding
import com.dicoding.dicodingevent.data.response.ListEventsItem

class HomeFinishedEventAdapter(private val onItemClick: (Int) -> Unit) :
   ListAdapter<ListEventsItem, HomeFinishedEventAdapter.EventViewHolder>(EventDiffCallBack()) {

       override fun onCreateViewHolder(parent: ViewGroup, viwType : Int): EventViewHolder {
           val binding = ItemVerticalFinishedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
           return EventViewHolder(binding)

       }

    override fun onBindViewHolder(holder: EventViewHolder, position : Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            event.id?.let {id -> onItemClick(id) }
        }

    }

    class EventViewHolder(private val binding : ItemVerticalFinishedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event : ListEventsItem) {
            with(binding) {
                tvItemFinished.text = event.name
                tvFinishedDescription.text = event.description
                Glide.with(itemView.context)
                    .load(event.imageLogo)
                    .into(binding.imgFinishedPhoto)

            }
         }
    }

    private class EventDiffCallBack : DiffUtil.ItemCallback<ListEventsItem>() {
        override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
            return oldItem == newItem

        }
    }
}