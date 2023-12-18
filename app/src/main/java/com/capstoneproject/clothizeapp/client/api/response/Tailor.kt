package com.capstoneproject.clothizeapp.client.api.response

data class Tailor(
    val id: Int,
    val nameTailor: String,
    val photoTailor: String,
    val latitude: Double,
    val longitude: Double,
    val descriptionTailor: String
)
