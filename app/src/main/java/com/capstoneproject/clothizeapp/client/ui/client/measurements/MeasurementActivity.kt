package com.capstoneproject.clothizeapp.client.ui.client.measurements

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.databinding.ActivityMeasurementBinding
import com.capstoneproject.clothizeapp.ui.client.MainClientActivity
import com.capstoneproject.clothizeapp.utils.getImageUri

class MeasurementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeasurementBinding
    private var currentImageUri: Uri? = null
    private lateinit var viewModel: MeasurementViewModel
    private var typeClothes = ""


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeasurementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init(){
        viewModel = obtainViewModel(this)

        // Fill entries to spinner
        val items = resources.getStringArray(R.array.type)
        val adapter = object : ArrayAdapter<String>(this, R.layout.spinner_items, items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                if (position == 0) {
                    view.visibility = View.GONE
                } else {
                    view.visibility = View.VISIBLE
                }
                return view
            }
        }

        adapter.setDropDownViewResource(R.layout.spinner_items)

        binding.spType.adapter = adapter

        binding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long,
            ) {
                typeClothes = binding.spType.selectedItem.toString()
                Toast.makeText(this@MeasurementActivity, binding.spType.selectedItem.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@MeasurementActivity, "Please select clothes type", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            val intentToMainClient = Intent(this, MainClientActivity::class.java)
            startActivity(intentToMainClient)
        }

        binding.btnOpenCamera.setOnClickListener {
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri)
        }

        binding.btnGallery.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.userPhoto.setImageURI(it)
            viewModel.imageUri.postValue(it)
        }
    }

    private fun calculateUserPhoto(){

    }

    private fun obtainViewModel(appCompatActivity: AppCompatActivity): MeasurementViewModel {
        val factory = MeasurementViewModelFactory.getInstance(appCompatActivity.applicationContext)
        return ViewModelProvider(
            appCompatActivity, factory
        )[MeasurementViewModel::class.java]
    }
}