package com.dicoding.dicodingevent

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dicodingevent.data.response.ListEventsItem
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.databinding.ItemListBinding
import com.dicoding.dicodingevent.ui.detail.DetailActivity

//import com.dicoding.dicodingevent.ui.home.HomeFinishedEventAdapter.EventViewHolder

class ListEventAdapter(private val onItemClicked : (String) -> Unit ) :
    ListAdapter<ListEventsItem, ListEventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup ,
        viewType: Int
    ): EventViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return EventViewHolder(binding)

    }

    override fun onBindViewHolder(holder: EventViewHolder , position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }


    class EventViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            with(binding) {
                tvNameItem.text = event.name
                tvSummaryItem.text = event.summary
                Glide.with(binding.root.context)
                    .load(event.imageLogo)
                    .into(binding.imgPhotoItem)

                // Perbaikan: Tambahkan onClickListener untuk berpindah ke DetailActivity
                root.setOnClickListener {
                    val context = it.context
                    val intent = Intent(context , DetailActivity::class.java).apply {
                        putExtra("EVENT_ID" , event.id)
                        putExtra("EVENT_NAME" , event.name)
                        putExtra("EVENT_SUMMARY" , event.summary)
                        putExtra("EVENT_IMAGE" , event.imageLogo)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    private class EventDiffCallback : DiffUtil.ItemCallback<ListEventsItem>() {
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



   // }