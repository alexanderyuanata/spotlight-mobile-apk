package com.mobile.entertainme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mobile.entertainme.R
import com.mobile.entertainme.databinding.RvTravelItemBinding
import com.mobile.entertainme.response.TravelDataItem

class TravelAdapter : ListAdapter<TravelDataItem, TravelAdapter.TravelViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TravelViewHolder {
        val binding = RvTravelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == 0) {
            layoutParams.setMargins(18.dpToPx(holder.itemView.context), 0, 18.dpToPx(holder.itemView.context), 0)
        } else {
            layoutParams.setMargins(0, 0, 18.dpToPx(holder.itemView.context), 0)
        }
        holder.itemView.layoutParams = layoutParams
    }

    inner class TravelViewHolder(private val binding: RvTravelItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(travel: TravelDataItem) {
            binding.ivItemCover.load(travel.coverUrl) {
                placeholder(R.drawable.travel_placeholder_image)
                error(R.drawable.travel_placeholder_image)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TravelDataItem>() {
            override fun areItemsTheSame(oldItem: TravelDataItem, newItem: TravelDataItem): Boolean {
                return oldItem.coverUrl == newItem.coverUrl
            }

            override fun areContentsTheSame(oldItem: TravelDataItem, newItem: TravelDataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}