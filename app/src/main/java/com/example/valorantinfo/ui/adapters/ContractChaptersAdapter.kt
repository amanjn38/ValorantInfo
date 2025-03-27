package com.example.valorantinfo.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantinfo.data.models.contracts.Chapter
import com.example.valorantinfo.databinding.ItemContractChapterBinding

class ContractChaptersAdapter(
    private val onChapterClick: (Chapter) -> Unit,
) : ListAdapter<Chapter, ContractChaptersAdapter.ChapterViewHolder>(ChapterDiffCallback()) {

    private val TAG = "ContractChaptersAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        Log.d(TAG, "testing onCreateViewHolder")
        val binding = ItemContractChapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        Log.d(TAG, "testing onBindViewHolder for position: $position")
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onChapterClick(getItem(position)) }
    }

    inner class ChapterViewHolder(
        private val binding: ItemContractChapterBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chapter: Chapter) {
            Log.d(TAG, "testing binding chapter: ${chapter.id}")
            binding.apply {
                chapterNumber.text = "Chapter ${chapter.position + 1}"
                levelCount.text = "${chapter.levels.size} Levels"
            }
        }
    }

    private class ChapterDiffCallback : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem == newItem
        }
    }
}
