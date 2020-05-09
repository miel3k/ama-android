package com.ama.presentation.location.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ama.data.events.model.Event
import kotlinx.android.synthetic.main.item_event.view.*

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(event: Event) {
        itemView.run {
            tv_date.text = event.date
            tv_message.text = event.message
        }
    }
}