package com.example.valorantinfo.ui.layoutmanagers

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LazyGridLayoutManager @JvmOverloads constructor(
    context: Context,
    spanCount: Int,
    orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {

    private var lastVisibleItem = 0
    private var lastVisiblePosition = 0
    private var isLoading = false
    private var onLoadMoreListener: OnLoadMoreListener? = null

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            val totalItemCount = itemCount
            val lastVisibleItemPosition = findLastVisibleItemPosition()

            if (!isLoading && lastVisibleItemPosition >= totalItemCount - 5) {
                onLoadMoreListener?.onLoadMore()
                isLoading = true
            }
        }
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        lastVisibleItem = findLastVisibleItemPosition()
        lastVisiblePosition = lastVisibleItem
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        onLoadMoreListener = listener
    }


    interface OnLoadMoreListener {
        fun onLoadMore()
    }
} 