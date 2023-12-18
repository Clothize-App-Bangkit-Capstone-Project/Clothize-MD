package com.capstoneproject.clothizeapp.tailor.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.clothizeapp.databinding.FragmentHomeTailorBinding
import com.capstoneproject.clothizeapp.tailor.data.adapter.TailorOrderAdapter
import com.capstoneproject.clothizeapp.tailor.data.local.preferences.TailorPrefViewModel
import com.capstoneproject.clothizeapp.tailor.data.local.preferences.TailorPreferences
import com.capstoneproject.clothizeapp.tailor.data.local.preferences.TailorPreferencesFactory
import com.capstoneproject.clothizeapp.tailor.data.local.preferences.dataTailorStore

class HomeTailorFragment : Fragment() {
    private lateinit var binding: FragmentHomeTailorBinding
    private lateinit var tailorPrefViewModel: TailorPrefViewModel
    private lateinit var homeTailorViewModel: HomeTailorViewModel
    private lateinit var tailorOrderAdapter: TailorOrderAdapter

    private var storeName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeTailorBinding.inflate(layoutInflater)

        init()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecycleList()
    }

    private fun init() {
        val pref = TailorPreferences.getInstance(requireActivity().dataTailorStore)
        tailorPrefViewModel =
            ViewModelProvider(this, TailorPreferencesFactory(pref))[TailorPrefViewModel::class.java]

        homeTailorViewModel = obtainViewModel(requireActivity())


        binding.apply {
            tailorPrefViewModel.getSessionUser().observe(requireActivity()) { session ->
                if (session != null) {
                    storeName = session.storeName
                    tvTitleHome.text = "Welcome, ${session.storeName}"
                }
            }


            btnFind.setOnClickListener {
                if (edtSearchOrder.text.toString().isNotEmpty()) {
                    showRecycleList(edtSearchOrder.text.toString())
                } else {
                    showRecycleList()
                }
            }
        }

    }

    private fun showRecycleList(clientName: String = "") {
        tailorOrderAdapter = TailorOrderAdapter()
        binding.rvOrder.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = tailorOrderAdapter
        }

        homeTailorViewModel.getAllOrderByTailorName(storeName, clientName).observe(requireActivity()) { orders ->
            if (orders != null) {
                binding.totalOrders.text = "Total Orders : ${orders.size}"
                tailorOrderAdapter.submitList(orders)
            }
        }
    }

    private fun obtainViewModel(activity: Activity): HomeTailorViewModel {
        val factory = HomeTailorViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(
            this, factory
        )[HomeTailorViewModel::class.java]
    }


}