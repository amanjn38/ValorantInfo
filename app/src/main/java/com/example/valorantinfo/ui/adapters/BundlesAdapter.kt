package com.example.valorantinfo.ui.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.valorantinfo.data.models.bundle.Bundle
import com.example.valorantinfo.databinding.ItemBundleBinding

class BundlesAdapter(private val onBundleClick: (Bundle) -> Unit) :
    ListAdapter<Bundle, BundlesAdapter.BundleViewHolder>(BundleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BundleViewHolder {
        val binding = ItemBundleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return BundleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BundleViewHolder, position: Int) {
        holder.bind(getItem(position), onBundleClick)
    }

    class BundleViewHolder(private val binding: ItemBundleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bundle: Bundle, onBundleClick: (Bundle) -> Unit) {
            binding.tvBundleName.text = bundle.displayName

            // Show progress indicator before loading starts
            binding.progressBarItem.visibility = View.VISIBLE
            binding.ivBundleIcon.visibility = View.INVISIBLE
            binding.viewOverlay.visibility = View.INVISIBLE

            // Load the bundle icon using Glide with a listener to handle loading states
            Glide.with(binding.root.context)
                .load(bundle.displayIcon) // Use displayIcon for the card
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<android.graphics.drawable.Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean,
                    ): Boolean {
                        binding.progressBarItem.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        binding.progressBarItem.visibility = View.GONE
                        binding.ivBundleIcon.visibility = View.VISIBLE
                        binding.viewOverlay.visibility = View.VISIBLE
                        return false
                    }
                })
                .into(binding.ivBundleIcon)

            // Set click listener
            binding.root.setOnClickListener {
                onBundleClick(bundle)
            }
        }
    }

    class BundleDiffCallback : DiffUtil.ItemCallback<Bundle>() {
        override fun areItemsTheSame(oldItem: Bundle, newItem: Bundle): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Bundle, newItem: Bundle): Boolean {
            return oldItem == newItem
        }
    }
}
