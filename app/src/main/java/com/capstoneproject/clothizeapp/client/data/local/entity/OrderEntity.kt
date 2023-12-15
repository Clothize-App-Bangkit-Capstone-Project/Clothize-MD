package com.capstoneproject.clothizeapp.client.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("client_orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id : Int = 0,

    @ColumnInfo("tailor_name")
    val tailor: String = "",

    @ColumnInfo("clothes_type")
    val type: String = "",

    @ColumnInfo("gender")
    val gender: String = "",

    @ColumnInfo("estimation")
    val estimation: Int,

    @ColumnInfo("status")
    val status: String = "",
)
