package com.dxid.publicservice.presentation.service_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dxid.publicservice.domain.model.Service
import com.dxid.publicservice.domain.usecase.GetServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceListViewModel @Inject constructor(
    private val getServiceUseCase: GetServiceUseCase
): ViewModel() {
    private val _state = MutableStateFlow(ServiceListState())
    val state =_state.asStateFlow()

    init {
        viewModelScope.launch {
            onEvent(ServiceListEvent.LoadServices)
        }
    }

    fun onEvent(event: ServiceListEvent) {
        when (event) {
            is ServiceListEvent.LoadServices -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val service = getServiceUseCase()
                    val categories = service.map { it.category }.distinct()
                    _state.update {
                        it.copy(
                            services = service,
                            categories = categories,
                            isLoading = false
                        )
                    }
                }
            }
            is ServiceListEvent.OnCategorySelected -> {
                _state.update { it.copy(selectedCategory = event.category) }
            }
            is ServiceListEvent.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = event.query) }
            }
        }
    }

    fun getFilteredServices(): List<Service> {
        val currentState = _state.value
        return currentState.services.filter {
            val matchQuery = it.name.contains(currentState.searchQuery, ignoreCase = true)
            val matchCategory = currentState.selectedCategory?.let { cat ->
                it.category == cat
            } ?: true
            matchQuery && matchCategory
        }
    }
}