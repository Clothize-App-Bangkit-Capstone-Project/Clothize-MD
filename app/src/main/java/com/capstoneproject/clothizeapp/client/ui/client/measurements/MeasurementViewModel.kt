package com.capstoneproject.clothizeapp.client.ui.client.measurements

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.clothizeapp.data.repository.MeasurementRepository

class MeasurementViewModel(private val measurementRepository: MeasurementRepository): ViewModel() {
    val imageUri = MutableLiveData<Uri>()
}