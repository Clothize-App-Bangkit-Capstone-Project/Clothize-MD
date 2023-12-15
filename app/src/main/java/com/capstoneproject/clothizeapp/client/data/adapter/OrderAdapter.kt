package com.capstoneproject.clothizeapp.client.data.adapter

import android.content.Context
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
import com.capstoneproject.clothizeapp.databinding.ItemOrderClientBinding

class OrderAdapter(private val context: Context) :
    ListAdapter<OrderEntity, OrderAdapter.OrderViewHolder>(DIFF_CALLBACK) {
    class OrderViewHolder(private var binding: ItemOrderClientBinding, private var context: Context) :
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

                tvTailor.text = order.tailor
                tvOrderType.text = "${order.type} - ${order.gender}"
                tvEstimation.text = "Estimate: ${order.estimation} days"

                tvStatus.text = order.status
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.black))
                when(order.status.lowercase()){
                    "finished" -> {
                        tvStatus.background = context.resources.getDrawable(R.drawable.bg_green_finish)
                    }
                    "on-progress" -> {
                        tvStatus.background = context.resources.getDrawable(R.drawable.bg_yellow_on_progress)
                    }
                    "rejected"-> {
                        tvStatus.background = context.resources.getDrawable(R.drawable.bg_red_rejected)
                        tvStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
                    }
                    else -> {
                        tvStatus.background = context.resources.getDrawable(R.drawable.bg_gray_pending)
                    }
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            ItemOrderClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding, context)
    }


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        if (order != null) {
            holder.bind(itemCount, position, order)
            holder.itemView.setOnClickListener {
                val intentToDetailOrder = Intent(context, DetailOrderActivity::class.java)
                intentToDetailOrder.putExtra(DetailOrderActivity.TYPE, order.type)
                intentToDetailOrder.putExtra(DetailOrderActivity.GENDER, order.gender)
                intentToDetailOrder.putExtra(DetailOrderActivity.STATUS, order.status)
                intentToDetailOrder.putExtra(DetailOrderActivity.ESTIMATION, order.estimation.toString())
                context.startActivity(intentToDetailOrder)
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