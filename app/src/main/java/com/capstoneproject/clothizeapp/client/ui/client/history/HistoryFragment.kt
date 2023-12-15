package com.capstoneproject.clothizeapp.client.ui.client.history

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.clothizeapp.client.data.adapter.HistoryAdapter
import com.capstoneproject.clothizeapp.client.data.local.entity.MeasurementEntity
import com.capstoneproject.clothizeapp.client.ui.client.measurements.MeasurementViewModel
import com.capstoneproject.clothizeapp.client.ui.client.measurements.MeasurementViewModelFactory
import com.capstoneproject.clothizeapp.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: MeasurementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater)

        init()

        return binding.root
    }

    private fun init(){
        viewModel = obtainViewModel(requireActivity())
        val adapter = HistoryAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHistory.adapter = adapter

        viewModel.loadHistory().observe(requireActivity()){histories ->
            if (histories.size > 0){
                showRecyclerView(histories)
            }else{
                binding.titleEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun showRecyclerView(histories: PagedList<MeasurementEntity>) {
        binding.rvHistory.visibility = View.VISIBLE
        binding.titleEmpty.visibility = View.GONE
        val adapter = HistoryAdapter()
        binding.rvHistory.adapter = adapter
        adapter.submitList(histories)
    }

    private fun obtainViewModel(activity: Activity): MeasurementViewModel {
        val factory = MeasurementViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(
            this, factory
        )[MeasurementViewModel::class.java]
    }



}