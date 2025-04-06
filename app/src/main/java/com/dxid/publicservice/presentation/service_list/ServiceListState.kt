package com.dxid.publicservice.presentation.service_list

import com.dxid.publicservice.domain.model.Service

data class ServiceListState (
    val services: List<Service> = emptyList(),
    val categories: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class ServiceListEvent {
    data class OnSearchQueryChange(val query: String) : ServiceListEvent()
    data class OnCategorySelected(val category: String?) : ServiceListEvent()
    object LoadServices : ServiceListEvent()
}