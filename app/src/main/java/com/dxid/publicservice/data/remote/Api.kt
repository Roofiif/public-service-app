package com.dxid.publicservice.data.remote

import com.dxid.publicservice.data.model.ServiceDto
import retrofit2.http.GET

interface Api {
    @GET("services/mobile")
    suspend fun getService(): List<ServiceDto>
}