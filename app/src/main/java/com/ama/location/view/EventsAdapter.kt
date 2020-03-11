package com.ama.location.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ama.R
import com.ama.location.model.Event

class EventsAdapter : RecyclerView.Adapter<EventViewHolder>() {

    private val asyncDiffer by lazy {
        AsyncListDiffer(this, getDiffCallback())
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun getItemCount() = asyncDiffer.currentList.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(asyncDiffer.currentList[position])
    }

    fun updateEvents(newEvents: List<Event>) {
        asyncDiffer.submitList(newEvents)
    }

    private fun getDiffCallback(): DiffUtil.ItemCallback<Event> =
        object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(
                oldItem: Event,
                newItem: Event
            ) = oldItem.date == newItem.date

            override fun areContentsTheSame(
                oldItem: Event,
                newItem: Event
            ) = oldItem == newItem
        }
}