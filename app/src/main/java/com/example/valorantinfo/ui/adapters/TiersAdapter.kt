package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valorantinfo.data.models.competitivetier.Tier
import com.example.valorantinfo.databinding.ItemTierBinding

class TiersAdapter : ListAdapter<Tier, TiersAdapter.TierViewHolder>(DiffCallback) {

    inner class TierViewHolder(private val binding: ItemTierBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tier: Tier) {
            binding.apply {
                tvTierName.text = tier.tierName
                tvDivisionName.text = tier.divisionName
                tvTierNumber.text = "Tier: ${tier.tier}"

                // Only load image if it exists
                if (!tier.largeIcon.isNullOrEmpty()) {
                    Glide.with(ivTierIcon.context)
                        .load(tier.largeIcon)
                        .centerCrop()
                        .into(ivTierIcon)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TierViewHolder {
        val binding = ItemTierBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return TierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TierViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Tier>() {
            override fun areItemsTheSame(oldItem: Tier, newItem: Tier): Boolean {
                return oldItem.tier == newItem.tier
            }

            override fun areContentsTheSame(oldItem: Tier, newItem: Tier): Boolean {
                return oldItem == newItem
            }
        }
    }
}
