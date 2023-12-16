package com.capstoneproject.clothizeapp.client.ui.client.order

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.clothizeapp.databinding.ActivityDetailOrderBinding

class DetailOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderBinding
    private lateinit var viewModel: OrderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        viewModel = obtainViewModel(this)

        val status = intent.getStringExtra(STATUS)
        val gender = intent.getStringExtra(GENDER)
        val type = intent.getStringExtra(TYPE)
        val est = intent.getStringExtra(ESTIMATION)

        if (status != null && gender != null && type != null && est != null) {
            loadContent(status, gender, type, est)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadContent(status: String, gender: String, type: String, est: String){
        binding.tvStatus.text = status
        binding.tvGender.text = gender
        binding.tvService.text = type
        binding.tvEstimation.text = est

        when(status){
            "Finished" -> {
                binding.btnApprove.visibility = View.GONE
                binding.btnRejected.visibility = View.GONE
            }
            "Pending" -> {
                binding.boxNote.visibility = View.GONE
                binding.btnApprove.visibility = View.GONE
                binding.btnRejected.visibility = View.GONE
                binding.boxFeedback.visibility = View.GONE
                binding.boxPrice.visibility = View.GONE
                binding.boxTotalPrice.visibility = View.GONE
            }
            "Rejected" -> {
                binding.btnApprove.visibility = View.GONE
                binding.btnRejected.visibility = View.GONE
                binding.boxNote.visibility = View.GONE
                binding.boxPrice.visibility = View.GONE
                binding.boxTotalPrice.visibility = View.GONE
            }
            "Offer" -> {
                binding.boxNote.visibility = View.GONE
            }
            else -> {
                binding.btnApprove.visibility = View.GONE
                binding.btnRejected.visibility = View.GONE
                binding.boxNote.visibility = View.GONE
            }
        }
    }

    private fun obtainViewModel(activity: Activity): OrderViewModel {
        val factory = OrderViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(
            this, factory
        )[OrderViewModel::class.java]
    }

    companion object{
        const val STATUS = "status"
        const val GENDER = "gender"
        const val TYPE = "type"
        const val ESTIMATION = "estimation"
    }

}