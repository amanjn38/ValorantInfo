package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valorantinfo.data.models.contenttiers.ContentTier
import com.example.valorantinfo.databinding.ItemContentTierBinding

class ContentTiersAdapter(
    private val onItemClick: (ContentTier) -> Unit,
) : ListAdapter<ContentTier, ContentTiersAdapter.ContentTierViewHolder>(ContentTierDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentTierViewHolder {
        val binding = ItemContentTierBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ContentTierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentTierViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContentTierViewHolder(
        private val binding: ItemContentTierBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(contentTier: ContentTier) {
            binding.apply {
                tvContentTierName.text = contentTier.displayName
                tvRank.text = "Rank: ${contentTier.rank}"
                tvJuiceValue.text = "Juice Value: ${contentTier.juiceValue}"
                tvJuiceCost.text = "Juice Cost: ${contentTier.juiceCost}"

                Glide.with(ivContentTierIcon)
                    .load(contentTier.displayIcon)
                    .into(ivContentTierIcon)
            }
        }
    }

    private class ContentTierDiffCallback : DiffUtil.ItemCallback<ContentTier>() {
        override fun areItemsTheSame(oldItem: ContentTier, newItem: ContentTier): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: ContentTier, newItem: ContentTier): Boolean {
            return oldItem == newItem
        }
    }
}
