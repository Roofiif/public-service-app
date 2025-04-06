package com.dxid.publicservice.domain.repository

import com.dxid.publicservice.domain.model.Service

interface ServiceRepository {
    suspend fun getService(): List<Service>
}