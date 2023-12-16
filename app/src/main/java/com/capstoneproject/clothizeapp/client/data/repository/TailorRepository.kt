package com.capstoneproject.clothizeapp.client.data.repository

import android.content.Context
import com.capstoneproject.clothizeapp.R
import com.capstoneproject.clothizeapp.client.api.response.Tailor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TailorRepository(
    private val context: Context
) {
    fun getTailorList(): List<Tailor>{
        val inputStream = context.resources.openRawResource(R.raw.tailor)
        val jsonText = inputStream.bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<Tailor>>() {}.type
        return Gson().fromJson(jsonText, listType)
    }
}