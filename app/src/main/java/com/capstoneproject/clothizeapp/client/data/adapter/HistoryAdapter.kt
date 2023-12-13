package com.capstoneproject.clothizeapp.client.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.data.local.entity.MeasurementEntity
import com.capstoneproject.clothizeapp.databinding.ItemHistoryFirstBinding

class HistoryAdapter() :
    PagedListAdapter<MeasurementEntity, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {
    class HistoryViewHolder(private var binding: ItemHistoryFirstBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemCount: Int, position: Int, history: MeasurementEntity) {

            binding.apply {
                val marginLayoutParams =
                    cardItemHistory.layoutParams as ViewGroup.MarginLayoutParams
                val marginBottomLast =
                    binding.root.resources.getDimensionPixelSize(R.dimen.margin_bottom)
                val marginBottomNormal =
                    binding.root.resources.getDimensionPixelSize(R.dimen.margin_bottom_normal)
                marginLayoutParams.bottomMargin =
                    if (position == itemCount - 1) marginBottomLast else marginBottomNormal
                cardItemHistory.layoutParams = marginLayoutParams
                tvBodySize.text = history.size
                tvTypeClothes.text = "${history.clothingType} (${history.gender})"
                tvSizeBodyCircum.text = "Body C : ${history.bodyGirth} cm"
                tvSizeChestCircum.text = "Chest C : ${history.chestGirth} cm"
                tvSizeBodyHeight.text = "Body L : ${history.bodyLength} cm"
                tvSizeShoulderLength.text = "Shoulder W : ${history.shoulderWidth} cm"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemHistoryFirstBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

//    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        if (history != null) {
            holder.bind(itemCount, position, history)
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MeasurementEntity>() {
            override fun areItemsTheSame(
                oldItem: MeasurementEntity,
                newItem: MeasurementEntity,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MeasurementEntity,
                newItem: MeasurementEntity,
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

}