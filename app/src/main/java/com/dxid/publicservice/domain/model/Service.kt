package com.dxid.publicservice.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val category: String,
    val providerName: String,
    val providerPhone: String,
    val providerAddress: String,
    val imageUrl: String,
    val galleryUrls: List<String>
)
