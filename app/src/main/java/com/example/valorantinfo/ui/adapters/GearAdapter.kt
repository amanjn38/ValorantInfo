package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantinfo.data.models.gear.Gear
import com.example.valorantinfo.databinding.ItemGearBinding

class GearAdapter(
    private val onGearClick: (Gear) -> Unit
) : ListAdapter<Gear, GearAdapter.GearViewHolder>(GearDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GearViewHolder {
        val binding = ItemGearBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GearViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GearViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GearViewHolder(
        private val binding: ItemGearBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onGearClick(getItem(position))
                }
            }
        }

        fun bind(gear: Gear) {
            binding.apply {
                tvGearName.text = gear.displayName
                tvGearDescription.text = gear.description
                tvGearCost.text = gear.shopData?.cost.toString()
            }
        }
    }

    private class GearDiffCallback : DiffUtil.ItemCallback<Gear>() {
        override fun areItemsTheSame(oldItem: Gear, newItem: Gear): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Gear, newItem: Gear): Boolean {
            return oldItem == newItem
        }
    }
} 