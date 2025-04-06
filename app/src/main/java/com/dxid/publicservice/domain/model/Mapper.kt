package com.dxid.publicservice.domain.model

import com.dxid.publicservice.data.local.ServiceEntity
import com.dxid.publicservice.data.model.ServiceDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun ServiceDto.toEntity(): ServiceEntity = ServiceEntity(
    id, name, description, price, category, providerName, providerPhone, providerAddress, imageUrl,
    galleryUrls = Gson().toJson(galleryUrls)
)

fun ServiceEntity.toDomain(): Service = Service(
    id, name, description, price, category,
    providerName, providerPhone, providerAddress, imageUrl,
    galleryUrls = Gson().fromJson(galleryUrls, object : TypeToken<List<String>>() {}.type)
)

fun ServiceDto.toDomain(): Service = Service(
    id, name, description, price, category,
    providerName, providerPhone, providerAddress, imageUrl,
    galleryUrls
)