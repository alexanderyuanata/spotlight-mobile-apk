package com.mobile.entertainme.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mobile.entertainme.databinding.TravelDetailItemBinding
import com.mobile.entertainme.response.TravelDataItem
import com.mobile.entertainme.view.ui.maps.MapsActivity
import org.json.JSONObject

class TravelDetailAdapter : ListAdapter<TravelDataItem, TravelDetailAdapter.TravelViewHolder>(DIFF_CALLBACK) {

    inner class TravelViewHolder(private val binding: TravelDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(travel: TravelDataItem) {
            binding.ivItemTravelCover.load(travel.coverUrl)
            binding.travelTitle.text = travel.placeName
            binding.categoryText.text = travel.categories
            binding.cityText.text = travel.city
            binding.priceText.text = travel.price.toString()
            binding.ratingText.text = travel.ratings.toString()
            binding.descriptionText.text = travel.description

            binding.mapsBtn.setOnClickListener {
                val context = binding.root.context
                val (latitude, longitude) = parseCoordinate(travel.coordinate ?: "")
                val intent = Intent(context, MapsActivity::class.java).apply {
                    putExtra("placeName", travel.placeName)
                    putExtra("latitude", latitude)
                    putExtra("longitude", longitude)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val binding = TravelDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
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

    private fun parseCoordinate(coordinate: String): Pair<Double, Double> {
        val jsonObject = JSONObject(coordinate)
        val latitude = jsonObject.getDouble("lat")
        val longitude = jsonObject.getDouble("lng")
        return Pair(latitude, longitude)
    }
}