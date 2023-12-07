package com.capstoneproject.clothizeapp.ui.client.measurements

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.databinding.ActivityMeasurementBinding

class MeasurementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMeasurementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeasurementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init(){
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
                Toast.makeText(this@MeasurementActivity, binding.spType.selectedItem.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}