package com.ama.location.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ama.location.model.Event
import kotlinx.android.synthetic.main.item_event.view.*

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(event: Event) {
        itemView.run {
            tv_date.text = event.date.toString()
            tv_message.text = event.message
        }
    }
}