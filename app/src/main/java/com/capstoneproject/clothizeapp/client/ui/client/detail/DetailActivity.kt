package com.capstoneproject.clothizeapp.client.ui.client.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.clothizeapp.client.ui.client.home.OrderFormActivity
import com.capstoneproject.clothizeapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nameTailor = intent.getStringExtra("nameTailor")
        val descriptionTailor = intent.getStringExtra("descriptionTailor")
        if (nameTailor != null || descriptionTailor != null) {
            binding.detailTitleTailor.text = nameTailor
            binding.detailDescTailor.text = descriptionTailor
        }

        init()
    }

    private fun init() {
        binding.btnDetailBack.setOnClickListener {

        }

        binding.btnOrderTailor.setOnClickListener {
            startActivity(Intent(this@DetailActivity, OrderFormActivity::class.java))
        }
    }
}