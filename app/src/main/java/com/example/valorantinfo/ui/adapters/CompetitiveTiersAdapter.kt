package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTier
import com.example.valorantinfo.databinding.ItemCompetitiveTierBinding

class CompetitiveTiersAdapter(private val onItemClicked: (CompetitiveTier) -> Unit) :
    ListAdapter<CompetitiveTier, CompetitiveTiersAdapter.CompetitiveTierViewHolder>(DiffCallback) {

    inner class CompetitiveTierViewHolder(private val binding: ItemCompetitiveTierBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(competitiveTier: CompetitiveTier) {
            binding.apply {
                tvCompetitiveTierName.text = competitiveTier.assetObjectName
                tvTiersCount.text = "Contains ${competitiveTier.tiers.size} tiers"

                // Get the first valid tier with a large icon to display as preview
                val firstValidTier = competitiveTier.tiers.firstOrNull { it.largeIcon != null }
                firstValidTier?.let {
                    Glide.with(ivCompetitiveTierIcon.context)
                        .load(it.largeIcon)
                        .centerCrop()
                        .into(ivCompetitiveTierIcon)
                }

                root.setOnClickListener {
                    onItemClicked(competitiveTier)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitiveTierViewHolder {
        val binding = ItemCompetitiveTierBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return CompetitiveTierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompetitiveTierViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CompetitiveTier>() {
            override fun areItemsTheSame(oldItem: CompetitiveTier, newItem: CompetitiveTier): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: CompetitiveTier, newItem: CompetitiveTier): Boolean {
                return oldItem == newItem
            }
        }
    }
}
