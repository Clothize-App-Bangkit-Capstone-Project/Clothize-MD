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

    private fun init() {
        viewModel = obtainViewModel(this)

        val id = intent.getIntExtra(ID, 0)

        loadContent(id)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadContent(id: Int) {
        binding.apply {
            viewModel.getOrder(id).observe(this@DetailOrderActivity) { order ->
                order?.apply {
                    tvTailorName.text = tailorName
                    tvStatus.text = status
                    tvGender.text = gender
                    tvService.text = service
                    tvClothingColor.text = color
                    tvClothingSize.text = size
                    tvQty.text = "$qty pcs"
                    tvEstimation.text = "$estimation days"
                    tvOrderDate.text = orderDate

                    when (status) {
                        "Finished" -> setupFinished()

                        "Pending" -> setupPending()

                        "Rejected" -> setupRejected()

                        "Offer" -> setupOffer()

                        else -> setupOnProgress()
                    }
                }

            }
        }


    }

    private fun obtainViewModel(activity: Activity): OrderViewModel {
        val factory = OrderViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(
            this, factory
        )[OrderViewModel::class.java]
    }

    private fun setupFinished() {
        binding.apply {
            btnApprove.visibility = View.GONE
            btnRejected.visibility = View.GONE
        }
    }

    private fun setupPending() {
        binding.apply {
            boxNote.visibility = View.GONE
            btnApprove.visibility = View.GONE
            btnRejected.visibility = View.GONE
            boxFeedback.visibility = View.GONE
            boxPrice.visibility = View.GONE
            boxTotalPrice.visibility = View.GONE
        }
    }

    private fun setupRejected() {
        binding.apply {
            btnApprove.visibility = View.GONE
            btnRejected.visibility = View.GONE
            boxNote.visibility = View.GONE
            boxPrice.visibility = View.GONE
            boxTotalPrice.visibility = View.GONE
        }
    }

    private fun setupOffer() {
        binding.apply {
            boxNote.visibility = View.GONE

            btnApprove.setOnClickListener {

            }

            btnRejected.setOnClickListener {

            }
        }
    }

    private fun setupOnProgress() {
        binding.apply {
            btnApprove.visibility = View.GONE
            btnRejected.visibility = View.GONE
            boxNote.visibility = View.GONE
        }
    }

    companion object {
        const val ID = "id"
        const val STATUS = "status"
        const val GENDER = "gender"
        const val TYPE = "type"
        const val ESTIMATION = "estimation"
    }

}