package com.capstoneproject.clothizeapp.client.data.repository

import com.capstoneproject.clothizeapp.api.measurement.ApiMeasurementService

class MeasurementRepository(
    private val apiMeasurementService: ApiMeasurementService,
) {
    companion object {
        @Volatile
        private var instance: MeasurementRepository? = null

        fun getInstance(
            apiMeasurementService: ApiMeasurementService,
//            storyDatabase: StoryDatabase,
        ): MeasurementRepository =
            instance ?: synchronized(this) {
                instance ?: MeasurementRepository(apiMeasurementService)
            }.also { instance = it }
    }
}