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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.ui.client.MainClientActivity
import com.capstoneproject.clothizeapp.databinding.ActivityMeasurementBinding
import com.capstoneproject.clothizeapp.utils.getImageUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MeasurementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeasurementBinding
    private lateinit var viewModel: MeasurementViewModel
    private lateinit var loadingDialog : AlertDialog
    private var currentImageUri: Uri? = null
    private var typeClothes = ""
    private var gender = ""


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
        loadingDialog = loadingDialog()
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

        binding.btnCalculate.setOnClickListener {
            calculateUserPhoto()
        }

        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rd_male -> gender = binding.rdMale.text.toString()
                R.id.rd_female -> gender = binding.rdFemale.text.toString()
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.userPhoto.setImageURI(it)
            viewModel.imageUri.postValue(it)
        }
    }

    private fun calculateUserPhoto(){
        if (checkFillOrNot()){
            loadingDialog.show()

            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                loadingDialog.dismiss()
                val size = "XXL"
                val parsingSize = when(size){
                    "XXL" -> "X2L"
                    "XXXL" -> "X3L"
                    else -> {size}
                }

                val intentToResult = Intent(this@MeasurementActivity, CalculateResultActivity::class.java)
                intentToResult.putExtra(CalculateResultActivity.SIZE, parsingSize)
                intentToResult.putExtra(CalculateResultActivity.GENDER, gender)
                intentToResult.putExtra(CalculateResultActivity.TYPE, typeClothes)
                startActivity(intentToResult)
            }
        }else{
            Toast.makeText(this, "Please fill all input before calculate!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainViewModel(appCompatActivity: AppCompatActivity): MeasurementViewModel {
        val factory = MeasurementViewModelFactory.getInstance(appCompatActivity.applicationContext)
        return ViewModelProvider(
            appCompatActivity, factory
        )[MeasurementViewModel::class.java]
    }


    private fun checkFillOrNot(): Boolean{

        if (binding.ageEdt.text.toString().isEmpty() || binding.ageEdt.text.toString() == "0"){
            binding.ageTIL.error = getString(R.string.not_fill)
            binding.ageEdt.error = null
        }else{
            binding.ageTIL.error = null
        }

        if (binding.heightEdt.text.toString().isEmpty() || binding.heightEdt.text.toString() == "0"){
            binding.heightTIL.error = getString(R.string.not_fill)
            binding.heightEdt.error = null
        }else{
            binding.heightTIL.error = null
        }

        if (binding.weightEdt.text.toString().isEmpty() || binding.weightEdt.text.toString() == "0"){
            binding.weightTIL.error = getString(R.string.not_fill)
            binding.weightEdt.error = null
        }else{
            binding.weightTIL.error = null
        }

        return (typeClothes != "Clothing type" &&
                gender != "" &&
                (binding.ageEdt.text.toString().isNotEmpty() && binding.ageEdt.text.toString() != "0") &&
                (binding.heightEdt.text.toString().isNotEmpty() && binding.heightEdt.text.toString() != "0") &&
                (binding.weightEdt.text.toString().isNotEmpty() && binding.weightEdt.text.toString() != "0") &&
                currentImageUri != null)
    }

    private fun loadingDialog(): AlertDialog {
        val view = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}