package com.capstoneproject.clothizeapp.client.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstoneproject.clothizeapp.client.api.client_order.ApiClientOrderService
import com.capstoneproject.clothizeapp.client.data.local.database.AppDatabase
import com.capstoneproject.clothizeapp.client.data.local.entity.OrderEntity

class OrderRepository(
    private val apiClientOrderService: ApiClientOrderService,
    private val appDatabase: AppDatabase,
) {
    val dummyData = arrayListOf<OrderEntity>(
        OrderEntity(
            tailor = "Tampan Tailors",
            type = "Shirts (Short)",
            gender = "Male",
            estimation = 3,
            status = "Finished",
        ),OrderEntity(
            tailor = "Bujank Tailors",
            type = "T-shirts (Long)",
            gender = "Female",
            estimation = 6,
            status = "Pending",
        ),OrderEntity(
            tailor = "Female Tailors",
            type = "Hoodie",
            gender = "Male",
            estimation = 4,
            status = "On-progress",
        ),OrderEntity(
            tailor = "Angry Tailors",
            type = "Shirts (Long)",
            gender = "Male",
            estimation = 3,
            status = "Rejected",
        ),OrderEntity(
            tailor = "Bujank Tailors",
            type = "Shirts",
            gender = "Female",
            estimation = 3,
            status = "Offer",
        ),
    )


//    fun insertMeasurementToDB(measurementEntity: MeasurementEntity) {
//        CoroutineScope(Dispatchers.Main).launch {
//            appDatabase.measureDao().insertMeasurement(measurementEntity)
//        }
//    }

//    fun getAllOrders(): LiveData<PagedList<OrderEntity>> {
//        val histories = appDatabase.orderDao().getAllOrders()
//        val config = PagedList.Config.Builder()
//            .setEnablePlaceholders(true)
//            .setPageSize(5)
//            .build()
//
//        return LivePagedListBuilder(histories, config).build()
//    }

    fun getAllOrders(): LiveData<List<OrderEntity>> {
        return MutableLiveData(dummyData)
    }


    companion object {
        @Volatile
        private var instance: OrderRepository? = null

        fun getInstance(
            apiClientOrderService: ApiClientOrderService,
            appDatabase: AppDatabase,
        ): OrderRepository =
            instance ?: synchronized(this) {
                instance ?: OrderRepository(apiClientOrderService, appDatabase)
            }.also { instance = it }
    }
}