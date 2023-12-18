package com.capstoneproject.clothizeapp.client.ui.client.measurements

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.api.response.DetailSize
import com.capstoneproject.clothizeapp.client.data.local.entity.MeasurementEntity
import com.capstoneproject.clothizeapp.client.ui.client.MainClientActivity
import com.capstoneproject.clothizeapp.databinding.ActivityCalculateResultBinding

class CalculateResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculateResultBinding
    private lateinit var viewModel: MeasurementViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {
        viewModel = obtainViewModel(this)

        val size = intent.getStringExtra(SIZE)
        val gender = intent.getStringExtra(GENDER)
        val type = intent.getStringExtra(TYPE)

        if (size != null && gender != null && type != null) {
            val content = viewModel.getDetailSizeForUser(type.lowercase(), size, gender.lowercase())
            if (content != null) {
                loadContent(content, type, gender)
            }
        }
        binding.btnSave.setOnClickListener {
            saveMeasurement()
        }

        binding.btnCancel.setOnClickListener {
            val intentToMain = Intent(this, MainClientActivity::class.java)
            finish()
            startActivity(intentToMain)
        }
    }

    private fun saveMeasurement() {
        val measurement = MeasurementEntity(
            clothingType = binding.tvClothesType.text.toString(),
            size = binding.tvClothesSize.text.toString(),
            gender = binding.tvClothesGender.text.toString(),
            chestGirth = binding.tvChest.text.toString().toInt(),
            shoulderWidth = binding.tvShoulder.text.toString().toInt(),
            bodyLength = binding.tvPbody.text.toString().toInt(),
            bodyGirth = binding.tvLbody.text.toString().toInt(),
        )

        viewModel.insertMeasurement(measurement)
        successDialog()


    }

    private fun loadContent(content: DetailSize, type: String, gender: String) {
        binding.tvClothesType.text = type
        binding.tvClothesSize.text = content.size
        binding.tvClothesGender.text = gender
        binding.tvChest.text = content.chestGirth.toString()
        binding.tvLbody.text = content.bodyGirth.toString()
        binding.tvPbody.text = content.bodyLength.toString()
        binding.tvShoulder.text = content.shoulderWidth.toString()
    }

    private fun obtainViewModel(activity: Activity): MeasurementViewModel {
        val factory = MeasurementViewModelFactory.getInstance(activity.applicationContext)
        return ViewModelProvider(
            this, factory
        )[MeasurementViewModel::class.java]
    }


    private fun successDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_success_add_history, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        view.findViewById<Button>(R.id.btn_dismiss).setOnClickListener {
            dialog.dismiss()
            val intentToMain = Intent(this, MainClientActivity::class.java)
            finish()
            startActivity(intentToMain)
        }
    }

    companion object {
        const val SIZE = "size"
        const val GENDER = "gender"
        const val TYPE = "type"
    }


    //    private fun loadSizeClothes(type: String, size: String, gender: String): DetailSize? {
//        val jsonFile = if (type == "t-shirts" || type == "shirts") {
//            resources.openRawResource(R.raw.tshirts)
//        } else {
//            resources.openRawResource(R.raw.jacket)
//        }
//
//        val builder = StringBuilder()
//        val reader = BufferedReader(InputStreamReader(jsonFile))
//        var line: String?
//        try {
//            while (reader.readLine().also { line = it } != null) {
//                builder.append(line)
//            }
//            val json = Gson().fromJson(builder.toString(), Size::class.java)
//            val sizes = if (gender == "male") json.maleSize else json.femaleSize
//            for (i in sizes.indices) {
//                if (sizes[i].size == size) {
//                    return sizes[i]
//                }
//            }
//        } catch (exception: IOException) {
//            exception.printStackTrace()
//        } catch (exception: JSONException) {
//            exception.printStackTrace()
//        }
//
//        return null
//    }
}