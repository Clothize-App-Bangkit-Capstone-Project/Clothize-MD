package com.capstoneproject.clothizeapp.client.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.capstoneproject.clothizeapp.client.api.measurement.ApiMeasurementService
import com.capstoneproject.clothizeapp.client.data.local.database.AppDatabase
import com.capstoneproject.clothizeapp.client.data.local.entity.MeasurementEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeasurementRepository(
    private val apiMeasurementService: ApiMeasurementService,
    private val appDatabase: AppDatabase,
) {

    fun insertMeasurementToDB(measurementEntity: MeasurementEntity) {
        CoroutineScope(Dispatchers.Main).launch {
            appDatabase.measureDao().insertMeasurement(measurementEntity)
        }
    }

    fun getAllMeasurementClient(): LiveData<PagedList<MeasurementEntity>> {
        val histories = appDatabase.measureDao().getAllMeasurement()
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(5)
            .build()

        return LivePagedListBuilder(histories, config).build()
    }


    companion object {
        @Volatile
        private var instance: MeasurementRepository? = null

        fun getInstance(
            apiMeasurementService: ApiMeasurementService,
            appDatabase: AppDatabase,
        ): MeasurementRepository =
            instance ?: synchronized(this) {
                instance ?: MeasurementRepository(apiMeasurementService, appDatabase)
            }.also { instance = it }
    }
}