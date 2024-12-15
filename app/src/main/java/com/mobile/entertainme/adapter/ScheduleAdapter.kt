package com.mobile.entertainme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobile.entertainme.data.ScheduleActivity
import com.mobile.entertainme.databinding.ScheduleItemBinding

class ScheduleAdapter(
    private val onScheduleChecked: (ScheduleActivity) -> Unit
) : ListAdapter<ScheduleActivity, ScheduleAdapter.ScheduleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ScheduleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding, onScheduleChecked)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ScheduleViewHolder(
        private val binding: ScheduleItemBinding,
        private val onScheduleChecked: (ScheduleActivity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: ScheduleActivity) {
            binding.activityText.text = schedule.title
            binding.descriptionText.text = schedule.description
            binding.categoryText.text = schedule.category
            binding.dateText.text = schedule.date
            binding.timeText.text = schedule.time

            binding.scheduleCheckedBtn.setOnClickListener {
                onScheduleChecked(schedule)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ScheduleActivity>() {
            override fun areItemsTheSame(oldItem: ScheduleActivity, newItem: ScheduleActivity): Boolean {
                return oldItem.firebaseKey == newItem.firebaseKey
            }

            override fun areContentsTheSame(oldItem: ScheduleActivity, newItem: ScheduleActivity): Boolean {
                return oldItem == newItem
            }
        }
    }
}