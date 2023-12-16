package com.capstoneproject.clothizeapp.client.ui.client.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.clothizeapp.client.api.response.Tailor
import com.capstoneproject.clothizeapp.client.data.adapter.TailorAdapter
import com.capstoneproject.clothizeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var tailorAdapter: TailorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.rvTailor
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        tailorAdapter = TailorAdapter()
        recyclerView.adapter = tailorAdapter

        // Initialize ViewModel
        viewModel =
            ViewModelProvider(this, HomeViewModelFactory(requireContext()))
                .get(HomeViewModel::class.java)

        // Observe changes in data
        viewModel.tailorList.observe(viewLifecycleOwner, { newData ->
            tailorAdapter.setData(newData)
        })

        viewModel.getTailorList()
    }
}