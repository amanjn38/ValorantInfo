package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valorantinfo.data.models.maps.Data
import com.example.valorantinfo.databinding.ItemMapBinding

class MapAdapter(
    private val onMapClick: (Data) -> Unit
) : ListAdapter<Data, MapAdapter.MapViewHolder>(MapDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val binding = ItemMapBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MapViewHolder(
        private val binding: ItemMapBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMapClick(getItem(position))
                }
            }
        }

        fun bind(map: Data) {
            binding.apply {
                mapNameTextView.text = map.displayName
                mapDescriptionTextView.text = map.tacticalDescription

                Glide.with(mapImageView)
                    .load(map.splash)
                    .centerCrop()
                    .into(mapImageView)
            }
        }
    }

    private class MapDiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }
} 