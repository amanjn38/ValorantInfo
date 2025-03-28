package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantinfo.data.models.events.Event
import com.example.valorantinfo.databinding.ItemEventBinding
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class EventAdapter(
    private val onItemClick: (Event) -> Unit
) : ListAdapter<Event, EventAdapter.EventViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EventViewHolder(
        private val binding: ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(event: Event) {
            binding.apply {
                tvEventName.text = event.displayName
                tvEventShortName.text = event.shortDisplayName
                tvEventTime.text = formatEventTime(event.startTime, event.endTime)
            }
        }

        private fun formatEventTime(startTime: String, endTime: String): String {
            val start = Instant.parse(startTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            val end = Instant.parse(endTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
            return "${formatter.format(start)} - ${formatter.format(end)}"
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
} 