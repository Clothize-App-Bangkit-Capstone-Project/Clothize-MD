package com.capstoneproject.clothizeapp.client.data.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.api.response.Tailor
import com.capstoneproject.clothizeapp.client.ui.client.detail.DetailActivity
import com.capstoneproject.clothizeapp.databinding.ItemTailorBinding

class TailorAdapter : ListAdapter<Tailor, TailorAdapter.TailorViewHolder>(TailorDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TailorViewHolder {
        val binding = ItemTailorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TailorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TailorViewHolder, position: Int) {
        val tailor = getItem(position)
        if (tailor != null) {
            holder.bind(tailor)
        }
    }

    fun setData(newList : List<Tailor>){
        submitList(newList.toMutableList() )
    }

    class TailorViewHolder(private val binding: ItemTailorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tailor: Tailor) {
            binding.cardItemTailor.setOnClickListener {
                val intent = Intent(binding.cardItemTailor.context, DetailActivity::class.java)
                intent.putExtra("nameTailor", tailor.nameTailor)
                intent.putExtra("locationTailor", tailor.locationTailor)
                intent.putExtra("descriptionTailor", tailor.descriptionTailor)
                binding.cardItemTailor.context.startActivity(intent)
            }
            val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.dummy_tailor)
            binding.rvPhotoTailor.setImageDrawable(drawable)
            binding.rvNameTailor.text = tailor.nameTailor
            binding.rvLocationTailor.text = tailor.locationTailor
        }
    }

    class TailorDiffCallback : DiffUtil.ItemCallback<Tailor>() {
        override fun areItemsTheSame(oldItem: Tailor, newItem: Tailor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tailor, newItem: Tailor): Boolean {
            return oldItem == newItem
        }
    }
}