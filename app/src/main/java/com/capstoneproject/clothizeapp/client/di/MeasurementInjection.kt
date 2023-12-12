package com.capstoneproject.clothizeapp.client.di

import android.content.Context
import com.alfares.storyappsubmission1.api.story.ApiMeasurementConfig
import com.capstoneproject.clothizeapp.data.repository.MeasurementRepository

object MeasurementInjection {
    fun provideRepository(context: Context): MeasurementRepository {
//        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiMeasurementConfig.getApiService()
//        val storyDB = StoryDatabase.getInstance(context)
//        val geocoder = Geocoder(context)
//        val locationDefault = context.getString(R.string.no_valid_location)
        return MeasurementRepository.getInstance(apiService)

    }
}