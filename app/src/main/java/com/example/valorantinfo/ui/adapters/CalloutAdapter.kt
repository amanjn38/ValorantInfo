package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantinfo.data.models.maps.Callout
import com.example.valorantinfo.databinding.ItemCalloutBinding

class CalloutAdapter : ListAdapter<Callout, CalloutAdapter.CalloutViewHolder>(CalloutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalloutViewHolder {
        val binding = ItemCalloutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalloutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalloutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CalloutViewHolder(
        private val binding: ItemCalloutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(callout: Callout) {
            binding.apply {
                regionNameTextView.text = callout.regionName
                superRegionNameTextView.text = "Super Region: ${callout.superRegionName}"
            }
        }
    }

    private class CalloutDiffCallback : DiffUtil.ItemCallback<Callout>() {
        override fun areItemsTheSame(oldItem: Callout, newItem: Callout): Boolean {
            return oldItem.regionName == newItem.regionName &&
                    oldItem.superRegionName == newItem.superRegionName
        }

        override fun areContentsTheSame(oldItem: Callout, newItem: Callout): Boolean {
            return oldItem == newItem
        }
    }
} 