package com.dxid.publicservice.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val category: String,
    val providerName: String,
    val providerPhone: String,
    val providerAddress: String,
    val imageUrl: String,
    val galleryUrls: String
)
