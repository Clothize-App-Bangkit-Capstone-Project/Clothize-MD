package com.capstoneproject.clothizeapp.client.ui.client.order

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.clothizeapp.client.data.adapter.OrderAdapter
import com.capstoneproject.clothizeapp.client.data.local.entity.OrderEntity
import com.capstoneproject.clothizeapp.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var viewModel: OrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOrderBinding.inflate(layoutInflater)

        init()

        return binding.root
    }

    private fun init(){
        viewModel = obtainViewModel(requireActivity())
        val adapter = OrderAdapter(requireContext())
        binding.rvHistory.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHistory.adapter = adapter

        viewModel.loadOrders().observe(requireActivity()){histories ->
            if (histories.isNotEmpty()){
                showRecyclerView(histories)
            }else{
                binding.titleEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun showRecyclerView(orders: List<OrderEntity>) {
        binding.rvHistory.visibility = View.VISIBLE
        binding.titleEmpty.visibility = View.GONE
        val adapter = OrderAdapter(requireContext())
        binding.rvHistory.adapter = adapter
        adapter.submitList(orders)
    }

    private fun obtainViewModel(activity: Activity): OrderViewModel {
        val factory = OrderViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(
            this, factory
        )[OrderViewModel::class.java]
    }

}