package com.dxid.publicservice.data.repository

import com.dxid.publicservice.data.local.ServiceDao
import com.dxid.publicservice.data.remote.Api
import com.dxid.publicservice.domain.model.Service
import com.dxid.publicservice.domain.model.toDomain
import com.dxid.publicservice.domain.model.toEntity
import com.dxid.publicservice.domain.repository.ServiceRepository
import com.dxid.publicservice.utils.PreferencesManager

class ServiceRepositoryImpl(
    private val api: Api,
    private val dao: ServiceDao,
    private val preferencesManager: PreferencesManager
) : ServiceRepository {
    override suspend fun getService(): List<Service> {
        val lastSync = preferencesManager.getLastSyncTime()
        val shouldFetchRemote = shouldFetch(lastSync)
        return if (shouldFetchRemote) {
            val remote = api.getService()
            dao.insertAll(remote.map { it.toEntity() })
            preferencesManager.setLastSyncTime(System.currentTimeMillis())
            remote.map { it.toDomain() }
        } else {
            dao.getAll().map { it.toDomain() }
        }
    }

    private fun shouldFetch(lastSyncTime: Long): Boolean {
        val oneDayMillis = 24 * 60 * 60 * 1000 // 1 hari
        val now = System.currentTimeMillis()
        return now - lastSyncTime > oneDayMillis
    }
}