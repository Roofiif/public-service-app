package com.dxid.publicservice.domain.usecase

import com.dxid.publicservice.domain.model.Service
import com.dxid.publicservice.domain.repository.ServiceRepository

class GetServiceUseCase (
    private val repository: ServiceRepository
) {
    suspend operator fun invoke() : List<Service> = repository.getService()
}