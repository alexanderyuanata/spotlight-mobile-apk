package com.mobile.entertainme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mobile.entertainme.R
import com.mobile.entertainme.databinding.BookDetailItemBinding
import com.mobile.entertainme.response.BookDataItem

class BookDetailAdapter : ListAdapter<BookDataItem, BookDetailAdapter.BookViewHolder>(DIFF_CALLBACK){

    inner class BookViewHolder(private val binding: BookDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookDataItem) {
            var coverUrl = book.coverUrl
            if (!coverUrl.isNullOrEmpty()) {
                if (coverUrl.startsWith("http://")) {
                    coverUrl = coverUrl.replace("http://", "https://")
                }
                binding.ivItemBookCover.load(coverUrl) {
                    placeholder(R.drawable.book_placeholder_image)
                    error(R.drawable.book_placeholder_image)
                }
            } else {
                binding.ivItemBookCover.load(R.drawable.book_placeholder_image)
            }

            val genres = book.genres
                ?.replace("[", "")
                ?.replace("]", "")
                ?.replace("'", "")
                ?.split(", ")
                ?.joinToString(", ") ?: "N/A"

            binding.bookTitle.text = book.book
            binding.yearText.text = book.publishYear
            binding.genreText.text = genres
            binding.authorText.text = book.author
            binding.ratingText.text = book.avgRating?.toString() ?: "N/A"
            binding.overviewText.text = book.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
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
}