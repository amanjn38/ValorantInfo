package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantinfo.data.models.ceremony.Ceremony
import com.example.valorantinfo.databinding.ItemCeremonyBinding

class CeremoniesAdapter(private val onCeremonyClick: (Ceremony) -> Unit) :
    ListAdapter<Ceremony, CeremoniesAdapter.CeremonyViewHolder>(CeremonyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CeremonyViewHolder {
        val binding = ItemCeremonyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return CeremonyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CeremonyViewHolder, position: Int) {
        holder.bind(getItem(position), onCeremonyClick)
    }

    class CeremonyViewHolder(private val binding: ItemCeremonyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ceremony: Ceremony, onCeremonyClick: (Ceremony) -> Unit) {
            binding.tvCeremonyName.text = ceremony.displayName

            // Set click listener
            binding.root.setOnClickListener {
                onCeremonyClick(ceremony)
            }
        }
    }

    class CeremonyDiffCallback : DiffUtil.ItemCallback<Ceremony>() {
        override fun areItemsTheSame(oldItem: Ceremony, newItem: Ceremony): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Ceremony, newItem: Ceremony): Boolean {
            return oldItem == newItem
        }
    }
}
