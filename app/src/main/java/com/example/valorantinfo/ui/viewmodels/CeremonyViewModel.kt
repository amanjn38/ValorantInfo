package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.ceremony.CeremoniesListResponse
import com.example.valorantinfo.repository.CeremonyRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CeremonyViewModel @Inject constructor(
    private val repository: CeremonyRepository,
) : ViewModel() {

    private val _ceremonies = MutableStateFlow<Resource<CeremoniesListResponse>>(Resource.Loading())
    val ceremonies: StateFlow<Resource<CeremoniesListResponse>> = _ceremonies

    init {
        getCeremonies()
    }

    private fun getCeremonies() {
        viewModelScope.launch {
            repository.fetchCeremonies().onEach { result ->
                _ceremonies.value = result
            }.launchIn(viewModelScope)
        }
    }
}
