package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.valorantinfo.data.models.buddy.Buddy
import com.example.valorantinfo.databinding.ItemBuddyBinding

class BuddiesAdapter(private val onBuddyClick: (Buddy) -> Unit) :
    ListAdapter<Buddy, BuddiesAdapter.BuddyViewHolder>(BuddyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuddyViewHolder {
        val binding = ItemBuddyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuddyViewHolder(binding, onBuddyClick)
    }

    override fun onBindViewHolder(holder: BuddyViewHolder, position: Int) {
        val buddy = getItem(position)
        holder.bind(buddy)
    }

    inner class BuddyViewHolder(
        private val binding: ItemBuddyBinding,
        private val onBuddyClick: (Buddy) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(buddy: Buddy) {
            binding.apply {
                tvBuddyName.text = buddy.displayName
                
                Glide.with(itemView.context)
                    .load(buddy.displayIcon)
                    .apply(RequestOptions()
                        .centerInside()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivBuddyIcon)

                root.setOnClickListener {
                    onBuddyClick(buddy)
                }

                root.isClickable = true
                root.isFocusable = true
            }
        }
    }

    class BuddyDiffCallback : DiffUtil.ItemCallback<Buddy>() {
        override fun areItemsTheSame(oldItem: Buddy, newItem: Buddy): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Buddy, newItem: Buddy): Boolean {
            return oldItem == newItem
        }
    }
} 