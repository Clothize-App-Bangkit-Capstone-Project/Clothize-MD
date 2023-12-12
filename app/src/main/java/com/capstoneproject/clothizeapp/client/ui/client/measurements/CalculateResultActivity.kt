package com.capstoneproject.clothizeapp.client.ui.client.measurements

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.clothizeapp.databinding.ActivityCalculateResultBinding

class CalculateResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculateResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}