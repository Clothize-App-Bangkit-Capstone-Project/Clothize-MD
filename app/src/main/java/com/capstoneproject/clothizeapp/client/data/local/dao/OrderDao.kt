package com.capstoneproject.clothizeapp.client.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstoneproject.clothizeapp.client.data.local.entity.OrderEntity

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrder(orderEntity: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllOrder(orderEntity: List<OrderEntity>)

    @Query("SELECT * FROM client_orders")
    fun getAllOrders(): DataSource.Factory<Int, OrderEntity>

//    @Query("SELECT * FROM measures ORDER BY id DESC LIMIT 1")
//    fun getLastMeasurement(): MeasurementEntity
}