package com.dxid.publicservice.data.model

data class ServiceDto(
    val id: Int,
    val name: String,
    val description: String,
    val slug: String,
    val price: String,
    val category: String,
    val providerName: String,
    val providerPhone: String,
    val providerAddress: String,
    val imageUrl: String,
    val galleryUrls: List<String>
)
