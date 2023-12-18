package com.capstoneproject.clothizeapp.client.ui.client.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstoneproject.clothizeapp.client.data.adapter.TailorAdapter
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPrefViewModel
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPreferences
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.ClientPreferencesFactory
import com.capstoneproject.clothizeapp.client.data.local.preferences.client.dataStore
import com.capstoneproject.clothizeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var clientPrefViewModel: ClientPrefViewModel
    private lateinit var tailorAdapter: TailorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecycleList()
    }

    private fun init() {

        val pref = ClientPreferences.getInstance(requireActivity().dataStore)
        clientPrefViewModel =
            ViewModelProvider(this, ClientPreferencesFactory(pref))[ClientPrefViewModel::class.java]

        // Initialize ViewModel
        viewModel = obtainViewModel(requireActivity())
        viewModel.getTailorList()


        binding.apply {
            clientPrefViewModel.getSessionUser().observe(requireActivity()) { session ->
                if (session != null) {
                    binding.tvTitleHome.text = "Welcome, ${session.fullName}"
                }

            }
            btnFind.setOnClickListener {
                if (edtSearchTailor.text.toString().isNotEmpty()){
                    // search tailor
                    viewModel.getTailorByName(edtSearchTailor.text.toString())
                }else{
                    viewModel.getTailorList()
                }
            }
        }

    }

    private fun showRecycleList() {
        tailorAdapter = TailorAdapter()
        binding.rvTailor.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = tailorAdapter
        }

        // Observe changes in data
        viewModel.tailorList.observe(viewLifecycleOwner) { newData ->
            tailorAdapter.setData(newData)
        }
    }

    private fun obtainViewModel(activity: Activity): HomeViewModel {
        val factory = HomeViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(
            this, factory
        )[HomeViewModel::class.java]
    }
}