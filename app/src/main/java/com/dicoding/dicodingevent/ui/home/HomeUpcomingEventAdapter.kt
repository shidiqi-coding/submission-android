package com.dicoding.dicodingevent.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.databinding.ItemHorizontalUpcomingBinding
import com.dicoding.dicodingevent.data.response.ListEventsItem

class HomeUpcomingEventAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<ListEventsItem, HomeUpcomingEventAdapter.EventViewHolder>(EventDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): EventViewHolder {
        val binding = ItemHorizontalUpcomingBinding.inflate(
            LayoutInflater.from(parent.context) ,
            parent ,
            false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder , position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            event.id?.toString()?.let { id -> onItemClick(id) } // Memastikan id tidak null
        }
    }

    class EventViewHolder(private val binding: ItemHorizontalUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            with(binding) {
                tvNameUpcoming.text = event.name
                Glide.with(root.context)
                    .load(event.imageLogo)
                    .into(imgPhotoVertical)
            }
        }
    }

    private class EventDiffCallBack : DiffUtil.ItemCallback<ListEventsItem>() {
        override fun areItemsTheSame(oldItem: ListEventsItem , newItem: ListEventsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ListEventsItem ,
            newItem: ListEventsItem
        ): Boolean {
            return oldItem == newItem
        }
    }

}
