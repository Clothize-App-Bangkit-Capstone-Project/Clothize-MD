package com.capstoneproject.clothizeapp.client.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.databinding.ItemHistorySecondBinding

class HistoryAdapter() :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(private var binding: ItemHistorySecondBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemCount: Int, position: Int) {
            val marginLayoutParams =
                binding.cardItemHistory.layoutParams as ViewGroup.MarginLayoutParams
            val marginBottomLast = binding.root.resources.getDimensionPixelSize(R.dimen.margin_bottom)
            val marginBottomNormal = binding.root.resources.getDimensionPixelSize(R.dimen.margin_bottom_normal)
            marginLayoutParams.bottomMargin = if (position == itemCount - 1)  marginBottomLast else marginBottomNormal
            binding.cardItemHistory.layoutParams = marginLayoutParams
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemHistorySecondBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        holder.bind(itemCount, position)

    }

}