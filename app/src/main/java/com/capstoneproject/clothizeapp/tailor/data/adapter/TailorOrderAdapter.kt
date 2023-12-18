package com.capstoneproject.clothizeapp.tailor.data.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.data.local.entity.OrderEntity
import com.capstoneproject.clothizeapp.client.ui.client.order.DetailOrderActivity
import com.capstoneproject.clothizeapp.databinding.ItemOrderTailorBinding

class TailorOrderAdapter() :
    ListAdapter<OrderEntity, TailorOrderAdapter.OrderViewHolder>(DIFF_CALLBACK) {
    class OrderViewHolder(private var binding: ItemOrderTailorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemCount: Int, position: Int, order: OrderEntity) {

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

                tvClient.text = order.clientName
                tvOrderType.text = "${order.service} - ${order.gender}"
                tvQuantity.text = "Qty: ${order.qty} Pcs"

                tvStatus.text = order.status
                tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                when(order.status.lowercase()){
                    "finished" -> {
                        tvStatus.background = itemView.context.resources.getDrawable(R.drawable.bg_green_finish)
                    }
                    "on-progress" -> {
                        tvStatus.background = itemView.context.resources.getDrawable(R.drawable.bg_yellow_on_progress)
                    }
                    "rejected"-> {
                        tvStatus.background = itemView.context.resources.getDrawable(R.drawable.bg_red_rejected)
                        tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    }
                    else -> {
                        tvStatus.background = itemView.context.resources.getDrawable(R.drawable.bg_gray_pending)
                    }
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            ItemOrderTailorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        if (order != null) {
            holder.bind(itemCount, position, order)
            holder.itemView.setOnClickListener {
                val intentToDetailOrder = Intent(holder.itemView.context, DetailOrderActivity::class.java)
                intentToDetailOrder.putExtra(DetailOrderActivity.ID, order.id)
                holder.itemView.context.startActivity(intentToDetailOrder)
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderEntity>() {
            override fun areItemsTheSame(
                oldItem: OrderEntity,
                newItem: OrderEntity,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: OrderEntity,
                newItem: OrderEntity,
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

}