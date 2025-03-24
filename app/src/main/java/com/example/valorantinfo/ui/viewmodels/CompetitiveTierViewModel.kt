package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTiersListResponse
import com.example.valorantinfo.repository.CompetitiveTierRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitiveTierViewModel @Inject constructor(
    private val repository: CompetitiveTierRepository
) : ViewModel() {

    private val _competitiveTiersState = MutableStateFlow<Resource<CompetitiveTiersListResponse>>(Resource.Loading())
    val competitiveTiersState: StateFlow<Resource<CompetitiveTiersListResponse>> = _competitiveTiersState

    init {
        getCompetitiveTiers()
    }

    fun getCompetitiveTiers() {
        viewModelScope.launch {
            repository.fetchCompetitiveTiers().collect { result ->
                _competitiveTiersState.value = result
            }
        }
    }
} 