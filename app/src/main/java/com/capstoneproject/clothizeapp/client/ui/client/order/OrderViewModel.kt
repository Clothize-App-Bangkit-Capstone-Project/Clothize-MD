package com.capstoneproject.clothizeapp.client.ui.client.order

import androidx.lifecycle.ViewModel
import com.capstoneproject.clothizeapp.client.data.repository.OrderRepository

class OrderViewModel(private val orderRepository: OrderRepository): ViewModel() {

//    fun insertMeasurement(measurementEntity: MeasurementEntity) {
//        orderRepository.insertMeasurementToDB(measurementEntity)
//    }

    fun loadOrders() = orderRepository.getAllOrders()
}