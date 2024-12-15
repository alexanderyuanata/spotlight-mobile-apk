package com.mobile.entertainme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mobile.entertainme.R
import com.mobile.entertainme.databinding.RvHomeItemBinding
import com.mobile.entertainme.response.BookDataItem

class BookAdapter : ListAdapter<BookDataItem, BookAdapter.BookViewHolder>(DIFF_CALLBACK) {

    inner class BookViewHolder(private val binding: RvHomeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookDataItem) {
            var coverUrl = book.coverUrl
            if (!coverUrl.isNullOrEmpty()) {
                if (coverUrl.startsWith("http://")) {
                    coverUrl = coverUrl.replace("http://", "https://")
                }
                binding.ivItemCover.load(coverUrl) {
                    placeholder(R.drawable.book_placeholder_image)
                    error(R.drawable.book_placeholder_image)
                }
            } else {
                binding.ivItemCover.load(R.drawable.book_placeholder_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = RvHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)

        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == 0) {
            layoutParams.setMargins(18.dpToPx(holder.itemView.context), 0, 18.dpToPx(holder.itemView.context), 0)
        } else {
            layoutParams.setMargins(0, 0, 18.dpToPx(holder.itemView.context), 0)
        }
        holder.itemView.layoutParams = layoutParams
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookDataItem>() {
            override fun areItemsTheSame(oldItem: BookDataItem, newItem: BookDataItem): Boolean {
                return oldItem.book == newItem.book
            }

            override fun areContentsTheSame(oldItem: BookDataItem, newItem: BookDataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}

